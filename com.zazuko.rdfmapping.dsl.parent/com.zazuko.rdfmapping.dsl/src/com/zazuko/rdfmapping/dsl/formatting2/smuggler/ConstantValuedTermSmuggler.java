package com.zazuko.rdfmapping.dsl.formatting2.smuggler;

import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm;

public class ConstantValuedTermSmuggler extends AbstractSmuggler<ConstantValuedTerm> {

	private boolean onNewLine;

	public ConstantValuedTermSmuggler(ConstantValuedTerm target) {
		super(target);
	}

	public boolean isOnNewLine() {
		return onNewLine;
	}

	public ConstantValuedTermSmuggler onNewLine() {
		this.onNewLine = true;
		return this;
	}

}
