package com.zazuko.rdfmapping.fanin.nq.nqfanin.tests;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.LazyStringInputStream;

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninRuntimeModule;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.util.RealNqFaninResourceImpl;

public class NqFaninParseHelper {

	public Resource parse(CharSequence text) throws Exception {
		XtextResourceSet set = new XtextResourceSet();
		URI uri = URI.createURI("testskos.nq");
		RealNqFaninResourceImpl result = new RealNqFaninResourceImpl(uri);
		set.getResources().add(result);

		InputStream in = new LazyStringInputStream(text == null ? "" : text.toString());
		result.load(in, null);
		return result;
	}

	public <T extends EObject> T extractOne(Resource resource, Class<T> expectedType) {
		@SuppressWarnings("unchecked")
		List<T> candidates = resource.getContents().stream().filter(eo -> expectedType.isInstance(eo)).map(eo -> (T) eo)
				.collect(Collectors.toList());
		if (candidates.size() != 1) {
			throw new RuntimeException("expected exactly one element, but found " + candidates.size());
		}
		return candidates.get(0);
	}

	public ResourceSet parseFolder(String folder) throws Exception {
		XtextResourceSet result = new XtextResourceSet();
		Path dir = Paths.get(folder);
		List<Path> nqFiles = Files.list(dir).peek(f -> System.out.println(f.getFileName())).filter(f -> f.getFileName().toString().endsWith("." + NqFaninRuntimeModule.NQ_FANIN_FILE_EXTENSION)).collect(Collectors.toList());
		
		for (Path nqFile : nqFiles) {
			String absolute = nqFile.toAbsolutePath().toString();
			URI uri = URI.createFileURI(absolute);

			RealNqFaninResourceImpl resource = new RealNqFaninResourceImpl(uri);
			result.getResources().add(resource);

			resource.load(null);
		}
		return result;
	}

}
