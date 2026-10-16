package com.zazuko.rdfmapping.dsl.ide.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.generator.GeneratorContext;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Injector;
import com.zazuko.rdfmapping.dsl.RdfMappingStandaloneSetup;

/**
 * Command line entry point: validates *.xrm files and generates the mapping
 * output (RML, R2RML, CARML, CSVW) without Eclipse.
 */
public class XrmCli {

	private static final String EXTENSION = ".xrm";
	private static final String DEFAULT_OUTPUT_DIR = "src-gen";

	private static final String USAGE = String.join("\n", //
			"Usage: xrm <command> [options] <path>...", //
			"", //
			"Commands:", //
			"  generate   validate and generate output for all *.xrm files", //
			"  validate   only validate, do not write any output", //
			"", //
			"Arguments:", //
			"  <path>     *.xrm files or directories (searched recursively).", //
			"             All files are loaded together so cross-file references resolve.", //
			"", //
			"Options:", //
			"  -o, --output <dir>  output directory (default: <first directory>/src-gen,", //
			"                      or ./src-gen if only files are given)", //
			"  -h, --help          show this help", //
			"", //
			"Exit codes: 0 = ok, 1 = validation errors, 2 = usage error");

	public static void main(String[] args) {
		System.exit(new XrmCli().run(args, System.out, System.err));
	}

	public int run(String[] args, PrintStream out, PrintStream err) {
		if (args.length == 0 || "-h".equals(args[0]) || "--help".equals(args[0])) {
			out.println(USAGE);
			return args.length == 0 ? 2 : 0;
		}
		String command = args[0];
		if (!"generate".equals(command) && !"validate".equals(command)) {
			err.println("Unknown command: " + command);
			err.println(USAGE);
			return 2;
		}

		Path output = null;
		List<Path> inputs = new ArrayList<>();
		for (int i = 1; i < args.length; i++) {
			String arg = args[i];
			if ("-o".equals(arg) || "--output".equals(arg)) {
				if (++i >= args.length) {
					err.println("Missing value for " + arg);
					return 2;
				}
				output = Paths.get(args[i]);
			} else if ("-h".equals(arg) || "--help".equals(arg)) {
				out.println(USAGE);
				return 0;
			} else if (arg.startsWith("-")) {
				err.println("Unknown option: " + arg);
				return 2;
			} else {
				inputs.add(Paths.get(arg));
			}
		}
		if (inputs.isEmpty()) {
			err.println("No input paths given.");
			err.println(USAGE);
			return 2;
		}

		Set<Path> files;
		try {
			files = collectFiles(inputs, output);
		} catch (IOException e) {
			err.println("Error: " + e.getMessage());
			return 2;
		}
		if (files.isEmpty()) {
			err.println("No *" + EXTENSION + " files found.");
			return 2;
		}

		if (output == null) {
			Path base = inputs.stream().filter(Files::isDirectory).findFirst().orElse(Paths.get("."));
			output = base.resolve(DEFAULT_OUTPUT_DIR);
		}

		Injector injector = new RdfMappingStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

		List<Resource> resources = new ArrayList<>();
		for (Path file : files) {
			resources.add(resourceSet.getResource(URI.createFileURI(file.toAbsolutePath().toString()), true));
		}
		EcoreUtil.resolveAll(resourceSet);

		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		int errors = 0;
		int warnings = 0;
		Set<Resource> invalid = new LinkedHashSet<>();
		Path cwd = Paths.get("").toAbsolutePath();
		for (Resource resource : resources) {
			List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
			for (Issue issue : issues) {
				if (issue.getSeverity() == Severity.ERROR) {
					errors++;
					invalid.add(resource);
				} else if (issue.getSeverity() == Severity.WARNING) {
					warnings++;
				} else {
					continue;
				}
				err.println(format(issue, resource, cwd));
			}
		}

		if ("generate".equals(command)) {
			GeneratorDelegate generator = injector.getInstance(GeneratorDelegate.class);
			JavaIoFileSystemAccess fsa = injector.getInstance(JavaIoFileSystemAccess.class);
			fsa.setOutputPath(output.toAbsolutePath().toString());
			GeneratorContext context = new GeneratorContext();
			context.setCancelIndicator(CancelIndicator.NullImpl);
			for (Resource resource : resources) {
				if (invalid.contains(resource)) {
					err.println("skipped (has errors): " + display(resource, cwd));
					continue;
				}
				generator.generate(resource, fsa, context);
			}
			out.println("Output written to " + display(output.toAbsolutePath(), cwd));
		}

		out.println(files.size() + " file(s), " + errors + " error(s), " + warnings + " warning(s)");
		return errors > 0 ? 1 : 0;
	}

	private Set<Path> collectFiles(List<Path> inputs, Path output) throws IOException {
		Path outputAbs = output == null ? null : output.toAbsolutePath().normalize();
		Set<Path> result = new LinkedHashSet<>();
		for (Path input : inputs) {
			if (!Files.exists(input)) {
				throw new IOException("No such file or directory: " + input);
			}
			if (Files.isDirectory(input)) {
				try (Stream<Path> walk = Files.walk(input)) {
					result.addAll(walk.filter(Files::isRegularFile) //
							.filter(p -> p.getFileName().toString().endsWith(EXTENSION)) //
							.filter(p -> !isExcluded(input, p, outputAbs)) //
							.map(p -> p.toAbsolutePath().normalize()) //
							.sorted(Comparator.naturalOrder()) //
							.collect(Collectors.toList()));
				}
			} else {
				result.add(input.toAbsolutePath().normalize());
			}
		}
		return result;
	}

	/** Skips output, build and hidden directories below the input root. */
	private boolean isExcluded(Path root, Path file, Path outputAbs) {
		if (outputAbs != null && file.toAbsolutePath().normalize().startsWith(outputAbs)) {
			return true;
		}
		Path relative = root.relativize(file);
		for (int i = 0; i < relative.getNameCount() - 1; i++) {
			String segment = relative.getName(i).toString();
			if (segment.startsWith(".") || DEFAULT_OUTPUT_DIR.equals(segment) || "target".equals(segment)
					|| "node_modules".equals(segment)) {
				return true;
			}
		}
		return false;
	}

	private String format(Issue issue, Resource resource, Path cwd) {
		String severity = issue.getSeverity().name().toLowerCase();
		Integer line = issue.getLineNumber();
		Integer column = issue.getColumn();
		return display(resource, cwd) + ":" + (line == null ? 0 : line) + ":" + (column == null ? 0 : column) + ": "
				+ severity + ": " + issue.getMessage();
	}

	private String display(Resource resource, Path cwd) {
		return display(Paths.get(resource.getURI().toFileString()), cwd);
	}

	private String display(Path path, Path cwd) {
		return path.startsWith(cwd) ? cwd.relativize(path).toString() : path.toString();
	}
}
