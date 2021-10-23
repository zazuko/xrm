package com.zazuko.rdfmapping.dsl.ide.contentassist;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.FileContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;

public abstract class EditorTestProjectDrivenTest extends AbstractServerTest {

	private static final String PATH = "../../runtime-EclipseXtext/editor-test";

	private final FileContext fileContext;

	public EditorTestProjectDrivenTest(String fileName) {
		this.fileContext = new FileContext(PATH, fileName);
	}

	public FileContext getFileContext() {
		return this.fileContext;
	}

	public MarkerContext marker(String marker) {
		return this.fileContext.getMarker(marker);
	}

}
