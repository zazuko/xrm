package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public interface ValueParser {

	boolean canParse(LineContext ctx);

	String parse(LineContext ctx);

}
