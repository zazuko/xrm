package com.zazuko.rdfmapping.dsl.formatting2.smuggler;

import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;

public class TemplateValuedTermSmuggler extends AbstractSmuggler<TemplateValuedTerm> {

	private boolean onNewLine;

	public TemplateValuedTermSmuggler(TemplateValuedTerm target) {
		super(target);
	}

	public boolean isOnNewLine() {
		return onNewLine;
	}

	public TemplateValuedTermSmuggler onNewLine() {
		this.onNewLine = true;
		return this;
	}

}
