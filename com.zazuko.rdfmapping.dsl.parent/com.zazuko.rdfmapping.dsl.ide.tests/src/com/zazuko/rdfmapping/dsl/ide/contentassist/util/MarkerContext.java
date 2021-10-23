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

	/**
	 * @param fragment
	 * @return position after fragment + one character
	 */
	public PositionContext nextLineWithTextAfter(String fragment) {
		return this.nextLineWithText(fragment, index -> index + fragment.length() + 1);
	}
	
	/**
	 * 
	 * @param fragment
	 * @return position after fragment
	 */
	public PositionContext nextLineWithTextAtEnd(String fragment) {
		return this.nextLineWithText(fragment, index -> index + fragment.length());
	}

	/**
	 * 
	 * @param fragment
	 * @return position at fragment
	 */
	public PositionContext nextLineWithTextAtStart(String fragment) {
		return this.nextLineWithText(fragment, index -> index);
	}

	interface IndexCalculation {
		int calculateIndex(int foundIndex);
	}

	private PositionContext nextLineWithText(String fragment, IndexCalculation indexBuilder) {
		for (int a = this.lineNo + 1; a < this.fileContext.getAllLines().size(); a++) {
			String candidate = this.fileContext.getAllLines().get(a);
			int index = candidate.indexOf(fragment);
			if (index >= 0) {
				int finalIndex = indexBuilder.calculateIndex(index);
				FileContext.checkLength(candidate, finalIndex);
				return new PositionContext(this.fileContext, a, finalIndex);
			}
		}
		throw new RuntimeException("fragment not found '" + fragment + "'");
	}

}
