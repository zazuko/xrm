package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public enum ValueType {
	IRI_REF(new IriRefValueParser()),
	BLANK_NODE_LABEL(new BlankNodeValueParser()), 
	LITERAL(new LiteralValueParser()),
	LANGUAGE_TAG(new LanguageTagValueParser());
	
	private ValueParser parser;

	private ValueType(ValueParser parser) {
		this.parser = parser;
	}

	public ValueParser getParser() {
		return parser;
	}
	
	
}
