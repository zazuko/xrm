package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher;

public class RdfMappingPrefixMatcher extends PrefixMatcher {

	@Override
	public boolean isCandidateMatchingPrefix(String name, String prefix) {
		if (prefix == null || "".equals(prefix)) {
			return true;
		}
		List<String> segments = Arrays.asList(name.toLowerCase().split("[\\.\\:]"));
		prefix = prefix.toLowerCase();
		for (String candidate : segments) {
			if (candidate.contains(prefix)) {
				return true;
			}
		}
		return false;
	}

}
