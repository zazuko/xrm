package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 5767617032975392874L;

	private LineContext ctx;

	public ParseException(LineContext ctx, String message) {
		super(message);
		this.ctx = ctx;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(super.toString());
		b.append(System.lineSeparator());
		b.append(this.ctx);
		return b.toString();
	}

}
