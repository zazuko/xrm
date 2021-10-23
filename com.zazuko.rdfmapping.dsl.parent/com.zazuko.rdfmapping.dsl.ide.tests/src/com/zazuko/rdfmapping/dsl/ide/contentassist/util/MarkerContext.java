package com.zazuko.rdfmapping.dsl.ide.contentassist.util;

public class MarkerContext {

	private final FileContext fileContext;
	private final int lineNo;

	public MarkerContext(FileContext fileContext, int lineNo) {
		this.lineNo = lineNo;
		this.fileContext = fileContext;
	}

	public int getLineNo() {
		return this.lineNo;
	}

	public PositionContext first() {
		return new PositionContext(this.fileContext, this.lineNo, 0);
	}

	public PositionContext nextLineAfter(String fragment) {
		for (int a = this.lineNo + 1; a < this.fileContext.getAllLines().size(); a++) {
			String candidate = this.fileContext.getAllLines().get(a);
			int index = candidate.indexOf(fragment);
			if (index >= 0) {
				int endIndex = index + fragment.length() + 1;
				FileContext.checkLength(candidate, endIndex);
				return new PositionContext(this.fileContext, a, endIndex);
			}
		}
		throw new RuntimeException("fragment not found '" + fragment + "'");
	}

}
