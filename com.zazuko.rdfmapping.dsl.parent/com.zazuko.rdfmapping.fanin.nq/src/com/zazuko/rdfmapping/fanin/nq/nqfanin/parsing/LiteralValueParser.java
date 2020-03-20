package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class LiteralValueParser implements ValueParser {

	private static final char LIMITER = '"';

	@Override
	public boolean canParse(LineContext ctx) {
		return LIMITER == ctx.peekChar();
	}

	@Override
	public String parse(LineContext ctx) {
		ctx.consume(LIMITER);
		return ctx.consumeUntil(new CtxSearchCriteria() {

			private boolean escaped;

			@Override
			public boolean doContinue(char c) {
				if (this.escaped) {
					this.escaped = false;
					return true;
				} else {
					if ('\\' == c) {
						this.escaped = true;
						return true;
					} else {
						return LIMITER != c;
					}
				}
			}

			@Override
			public String toString() {
				return "until seeing an unescaped \". currently escaped=" + this.escaped;
			}

		});

	}

}
