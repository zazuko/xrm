package com.zazuko.rdfmapping.dsl.ide.contentassist.util;

import org.eclipse.xtext.testing.TestCompletionConfiguration;

public class PositionContext {

	private final FileContext fileContext;
	private final int line;
	private final int columnStart;
	private final int columnEnd;

	public PositionContext(FileContext fileContext, int line, int column) {
		this(fileContext, line, column, column);
	}

	public PositionContext(FileContext fileContext, int line, int columnStart, int columnEnd) {
		this.fileContext = fileContext;
		this.line = line;
		this.columnStart = columnStart;
		this.columnEnd = columnEnd;
	}

	public String toExpectationDescriptionLeft() {
		return String.format("[%d, %d]", this.line, this.columnStart);
	}

	public String toExpectationDescriptionRight() {
		return String.format("[%d, %d]", this.line, this.columnEnd);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.toExpectationDescriptionLeft());
		if (this.columnStart != this.columnEnd) {
			b.append('-');
			b.append(this.toExpectationDescriptionRight());
		}
		b.append(":\t");
		b.append(this.fileContext.getAllLines().get(this.line).substring(this.columnStart));
		return b.toString();
	}

	public void configure(TestCompletionConfiguration it) {
		it.setFilesInScope(this.fileContext.getOtherFilesInScope());
		it.setModel(this.fileContext.getFileContent());
		it.setLine(this.line);
		it.setColumn(this.columnStart);
	}

	public PositionContext spanLeftForLengthOf(String s) {
		return new PositionContext(this.fileContext, this.line, this.columnEnd - s.length(), this.columnEnd);
	}
}
