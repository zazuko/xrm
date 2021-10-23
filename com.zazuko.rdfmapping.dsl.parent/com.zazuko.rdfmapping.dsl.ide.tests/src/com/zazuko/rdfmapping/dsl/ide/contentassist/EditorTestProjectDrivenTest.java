package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.FileContext;

public abstract class EditorTestProjectDrivenTest extends AbstractServerTest {

	private static final String PATH = "../../runtime-EclipseXtext/editor-test";

	private final FileContext fileContext;

	public EditorTestProjectDrivenTest(String fileName) {
		Path xrmFile = Paths.get(PATH, fileName);
		this.fileContext = new FileContext(xrmFile);
	}

	public FileContext getFileContext() {
		return this.fileContext;
	}

}
