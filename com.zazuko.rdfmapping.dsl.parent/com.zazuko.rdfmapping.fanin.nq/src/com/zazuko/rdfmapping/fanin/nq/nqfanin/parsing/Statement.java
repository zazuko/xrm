package com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing;

public class Statement {
	private final LineContext ctx;

	private Value subject;
	private Value predicate;
	private Value object;
	private Value languageTag;
	private Value graphLabel;
	private Value literal;

	public Statement(LineContext ctx) {
		this.ctx = ctx;
	}

	public Value getSubject() {
		return subject;
	}

	public void setSubject(Value subject) {
		this.subject = subject;
	}

	public Value getPredicate() {
		return predicate;
	}

	public void setPredicate(Value predicate) {
		this.predicate = predicate;
	}

	public Value getObject() {
		return object;
	}

	public void setObject(Value object) {
		this.object = object;
	}

	public Value getGraphLabel() {
		return graphLabel;
	}

	public void setGraphLabel(Value graphLabel) {
		this.graphLabel = graphLabel;
	}

	public Value getLiteral() {
		return literal;
	}

	public void setLiteral(Value literal) {
		this.literal = literal;
	}

	public LineContext getCtx() {
		return ctx;
	}

	public Value getLanguageTag() {
		return languageTag;
	}

	public void setLanguageTag(Value languageTag) {
		this.languageTag = languageTag;
	}

}
