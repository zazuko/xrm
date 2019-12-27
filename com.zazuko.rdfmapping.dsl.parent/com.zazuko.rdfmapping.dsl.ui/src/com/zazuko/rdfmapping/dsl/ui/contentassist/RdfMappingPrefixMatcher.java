package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher;

public class RdfMappingPrefixMatcher extends PrefixMatcher {

	private static final String DOT = ".";
	private static final String COLON = ":";
	public static final String DELIMITERS_REGEX = "[\\" + DOT + "\\:" + COLON + "]";

	@Override
	public boolean isCandidateMatchingPrefix(String name, String prefix) {
		if (prefix == null || "".equals(prefix)) {
			return true;
		}
		List<String> segments = Arrays.asList(name.toLowerCase().split(DELIMITERS_REGEX));
		List<String> prefixSegments = Arrays.asList(prefix.toLowerCase().split(DELIMITERS_REGEX));
		boolean qualified = prefix.endsWith(DOT) || prefix.endsWith(COLON);
		if (prefixSegments.size() == 1 && !qualified) {
			String oneSegment = prefixSegments.get(0);
			for (String candidate : segments) {
				if (candidate.contains(oneSegment)) {
					return true;
				}
			}
		} else {
			// path matching
			boolean traceMatches = segments.size() >= prefixSegments.size();
			for (int a = 0; traceMatches && a < prefixSegments.size(); a++) {
				String prefixToMatch = prefixSegments.get(a);
				String segment = segments.get(a);
				traceMatches &= a < prefixSegments.size() - 1 || qualified ? segment.equals(prefixToMatch)
						: segment.contains(prefixToMatch);
			}
			return traceMatches;

		}
		return false;
	}

}
