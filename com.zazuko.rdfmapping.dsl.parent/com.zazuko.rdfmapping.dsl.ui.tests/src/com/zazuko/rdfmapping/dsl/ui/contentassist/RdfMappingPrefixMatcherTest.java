package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;

public class RdfMappingPrefixMatcherTest {

	private RdfMappingPrefixMatcher matcher;

	@BeforeAll
	public void before() {
		this.matcher = new RdfMappingPrefixMatcher();
	}

	@ParameterizedTest
	@MethodSource("data")
	public void test(Params params) {
		boolean actualMatch = this.matcher.isCandidateMatchingPrefix(params.name, params.prefix);
		Assertions.assertEquals(params.expectMatch, actualMatch, params.name + " " + params.prefix);
	}

	static class Params {
		private final String name;
		private final String prefix;
		private final boolean expectMatch;

		public Params(String name, String prefix, boolean expectMatch) {
			super();
			this.name = name;
			this.prefix = prefix;
			this.expectMatch = expectMatch;
		}

	}

	public static Stream<Params> data() {
		List<Params> result = new ArrayList<>();
		class Builder {
			public void register(String name, String prefix, boolean expectMatch) {
				result.add(new Params(name, prefix, expectMatch));
			}
		}
		Builder b = new Builder();

		// @formatter:off
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "ex", true);
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "ex" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX, false);
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", true);
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "ar", true);
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "plz", true);
		b.register("exPLZ" + RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX + "bar", "ar", true);
		// @formatter:off
		return result.stream();
	}
}
