package com.zazuko.rdfmapping.dsl.ide.contentassist.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FileContext {

	private static final String TESTMARKER = "AUTOTEST";

	private final String fileContent;
	private final List<String> allLines;
	private final Map<String, Integer> marker2LineNo;
	private final Map<String, CharSequence> otherFilesInScope;

	public FileContext(String projectRootString, String xrmFileString) {
		Path xrmFile = Paths.get(projectRootString, xrmFileString);
		if (!Files.exists(xrmFile)) {
			throw new RuntimeException("file does not exist: " + xrmFile);
		}

		try {
			this.fileContent = Files.readString(xrmFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		try {
			this.allLines = Files.readAllLines(xrmFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		this.marker2LineNo = new TreeMap<>();
		for (int a = 0; a < this.allLines.size(); a++) {
			String line = this.allLines.get(a);
			line = line.trim();

			int index = line.indexOf(TESTMARKER);
			if (index >= 0) {
				int endIndex = index + TESTMARKER.length() + 1;
				checkLength(line, endIndex);
				if (line.length() < endIndex) {
					throw new RuntimeException("line with testmarker too short '" + line + "'");
				}
				String payload = line.substring(endIndex).trim();
				if (payload.isBlank()) {
					throw new RuntimeException("payload is blank for line '" + line + "'");
				}
				if (this.marker2LineNo.containsKey(payload)) {
					throw new RuntimeException(
							"key '" + payload + "' ambigous on line " + a + " and " + this.marker2LineNo.get(payload));
				}
				this.marker2LineNo.put(payload, a);
			}
		}

		this.otherFilesInScope = findOtherFilesInScope(projectRootString, xrmFileString);
	}

	private Map<String, CharSequence> findOtherFilesInScope(String projectRootString, String xrmFileString) {
		try {
			List<Path> otherFiles = Files.walk(Paths.get(projectRootString))
					.filter(p -> p.getFileName().toString().endsWith("xrm")).filter(p -> Files.isRegularFile(p))
					.filter(p -> !xrmFileString.equals(p.getFileName().toString())).collect(Collectors.toList());

			Map<String, CharSequence> result = new HashMap<>();
			for (Path file : otherFiles) {
				String content = Files.readString(file);
				result.put(file.getFileName().toString(), content);
			}
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void checkLength(String line, int endIndex) {
		if (line.length() < endIndex) {
			throw new RuntimeException("line too short '" + line + "'");
		}
	}

	public String getFileContent() {
		return this.fileContent;
	}

	public MarkerContext getMarker(String testKey) {
		Integer lineNo = this.marker2LineNo.get(testKey);
		if (lineNo == null) {
			throw new RuntimeException(
					"no marker found for key '" + testKey + "'. known markers are " + this.listMarkers());
		}
		return new MarkerContext(this, lineNo);
	}

	public List<String> getAllLines() {
		return this.allLines;
	}

	private String listMarkers() {
		return this.marker2LineNo.keySet().stream().collect(Collectors.joining(", ", "[ ", " ]"));
	}

	public Map<String, CharSequence> getOtherFilesInScope() {
		return otherFilesInScope;
	}

}
