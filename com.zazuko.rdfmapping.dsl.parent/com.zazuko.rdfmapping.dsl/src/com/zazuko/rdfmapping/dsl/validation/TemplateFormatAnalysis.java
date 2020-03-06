package com.zazuko.rdfmapping.dsl.validation;

import java.util.Set;

public class TemplateFormatAnalysis {

	private Set<Integer> usedKeys, skippedKeys;

	public TemplateFormatAnalysis(Set<Integer> usedKeys, Set<Integer> skippedKeys) {
		super();
		this.usedKeys = usedKeys;
		this.skippedKeys = skippedKeys;
	}

	public Set<Integer> getUsedKeys() {
		return usedKeys;
	}

	public Set<Integer> getSkippedKeys() {
		return skippedKeys;
	}

	@Override
	public String toString() {
		return "TemplateFormatValidationDiagnose [usedKeys=" + usedKeys + ", skippedKeys=" + skippedKeys + "]";
	}
	
	
	
}
