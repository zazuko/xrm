package com.zazuko.rdfmapping.dsl.validation;

import java.util.Set;

public class IriFormatAnalysis {

	private Set<Integer> usedKeys, skippedKeys;

	public IriFormatAnalysis(Set<Integer> usedKeys, Set<Integer> skippedKeys) {
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
		return "IriFormatValidationDiagnose [usedKeys=" + usedKeys + ", skippedKeys=" + skippedKeys + "]";
	}
	
	
	
}
