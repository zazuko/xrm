package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class BlankNodeValueParser implements ValueParser {

	private static final char FIRST_CHARACTER_A = '_';
	private static final char SECOND_CHARACTER_A = ':';
	
	private static final char FIRST_CHARACTER_B = '.';
	@Override
	public boolean canParse(LineContext ctx) {
		char peekChar = ctx.peekChar();
		if (FIRST_CHARACTER_A == peekChar) {
			return ctx.peekChar(1) == SECOND_CHARACTER_A;
		}
		return FIRST_CHARACTER_B == peekChar;
	}

	@Override
	public String parse(LineContext ctx) {
		return ctx.consumeUntil(' ');
	}

}
