package com.zazuko.rdfmapping.dsl.ide.contentassist.util;

import org.eclipse.xtext.testing.TestCompletionConfiguration;

public class PositionContext {

	private final FileContext fileContext;
	private final int line;
	private final int column;

	public PositionContext(FileContext fileContext, int line, int column) {
		this.fileContext = fileContext;
		this.line = line;
		this.column = column;
	}

	public String toExpectationDescription() {
		return String.format("[%d, %d]", this.line, this.column);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.toExpectationDescription());
		b.append(":\t");
		b.append(this.fileContext.getAllLines().get(this.line).substring(this.column));
		return b.toString();
	}

	public void configure(TestCompletionConfiguration it) {
		it.setFilesInScope(this.fileContext.getOtherFilesInScope());
		it.setModel(this.fileContext.getFileContent());
		it.setLine(this.line);
		it.setColumn(this.column);
	}
}
