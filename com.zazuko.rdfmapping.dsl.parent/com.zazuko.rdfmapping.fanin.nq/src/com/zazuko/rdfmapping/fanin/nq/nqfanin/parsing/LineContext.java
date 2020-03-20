package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class LineContext {

	private final String line;
	private final int lineNumber;
	
	private int position;


	public LineContext(String line, int lineNumber) {
		this.line = line;
		this.lineNumber = lineNumber;
	}

	public char peekChar() {
		this.assertPositionOK();
		return this.line.charAt(this.position);
	}

	public char peekChar(int n) {
		this.assertPositionOK(n);
		return this.line.charAt(this.position + n);
	}

	private void assertPositionOK() {
		this.assertPositionOK(0);
	}
	private void assertPositionOK(int delta) {
		if (this.position + delta >= this.line.length()) {
			throw new ParseException(this, "out of characters");
		}
	}

	public void consume(char expectedCharacter) {
		if (this.peekChar() != expectedCharacter) {
			throw new ParseException(this, String.format("expected '%s' but was '%s'", expectedCharacter, this.peekChar()));
		}
		this.position++;
	}
	
	public String consumeUntil(char endCharacter) {
		this.assertPositionOK();
		int endCharIndex = this.line.indexOf(endCharacter, this.position);
		if (endCharIndex < 0) {
			throw new ParseException(this, String.format("expected '%s' not found", endCharacter));
		}
		String result = this.line.substring(this.position, endCharIndex);
		this.position = endCharIndex + 1;
		return result;
	}
	
	public String consumeUntil(CtxSearchCriteria criteria) {
		this.assertPositionOK();
		int a = this.position;
		while (a < this.line.length() && criteria.doContinue(this.line.charAt(a))) {
			a++;
		}
		String result = this.line.substring(this.position, a);
		this.position = a + 1;
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("line: ");
		b.append(this.lineNumber);
		b.append(System.lineSeparator());
		b.append(this.line);
		b.append(System.lineSeparator());
		for (int a = 0;a < this.position;a++) {
			b.append(' ');
		}
		b.append('^');
		return b.toString();
	}

	public void skipWhitespaces() {
		while (this.position < this.line.length() && Character.isWhitespace(this.peekChar())) {
			this.position++;
		}
	}

	
	
	
}
