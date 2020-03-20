package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class LanguageTagValueParser implements ValueParser {

	private static final char FIRST_CHARACTER = '@';

	@Override
	public boolean canParse(LineContext ctx) {
		return FIRST_CHARACTER == ctx.peekChar();
	}

	@Override
	public String parse(LineContext ctx) {
		ctx.consume(FIRST_CHARACTER);
		return ctx.consumeUntil(' ');
	}

}
