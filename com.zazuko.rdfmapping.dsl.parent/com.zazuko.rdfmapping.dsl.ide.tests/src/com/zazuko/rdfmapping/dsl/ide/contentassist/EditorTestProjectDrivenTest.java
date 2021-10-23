package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class EditorTestProjectDrivenTest extends AbstractServerTest {

	private static final String PATH = "../../runtime-EclipseXtext/editor-test";
	private static final String TESTMARKER = "AUTOTEST";

	private final String fileContent;
	private final List<String> allLines;
	private final Map<String, Integer> marker2LineNo;

	public EditorTestProjectDrivenTest(String fileName) {
		Path xrmFile = Paths.get(PATH, fileName);
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
			if (index > 0) {
				if (line.length() < index + TESTMARKER.length() + 1) {
					throw new RuntimeException("line with testmarker too short '" + line + "'");
				}
				String payload = line.substring(index + TESTMARKER.length()+1).trim();
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

	}

	public String getFileContent() {
		return this.fileContent;
	}
	
	public MarkerContext getContext(String testKey) {
		Integer lineNo = this.marker2LineNo.get(testKey);
		if (lineNo == null) {
			throw new RuntimeException(
					"no marker found for key '" + testKey + "'. known markers are " + this.listMarkers());
		}
		return new MarkerContext(lineNo);
	}

	private String listMarkers() {
		return this.marker2LineNo.keySet().stream().collect(Collectors.joining(", ", "[ ", " ]"));
	}

	class MarkerContext {

		private final int lineNo;

		public MarkerContext(int lineNo) {
			this.lineNo = lineNo;
		}
		
		public int getLineNo() {
			return this.lineNo;
		}
		
		public PositionContext first() {
			return new PositionContext(0);
		}

		class PositionContext {
			private final int position;
			
			private PositionContext(int position) {
				this.position = position;
			}

			public String toExpectationDescription() {
				return String.format("[%d, %d]", MarkerContext.this.lineNo, this.position);
			}
		}

	}

}
