package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RdfMappingPrefixMatcherTest {

	private RdfMappingPrefixMatcher matcher;

	private final String name;
	private final String prefix;
	private final boolean expectMatch;

	public RdfMappingPrefixMatcherTest(String name, String prefix, boolean expectMatch) {
		super();
		this.name = name;
		this.prefix = prefix;
		this.expectMatch = expectMatch;
	}

	@Before
	public void before() {
		this.matcher = new RdfMappingPrefixMatcher();
	}

	@Test
	public void test() {
		boolean actualMatch = this.matcher.isCandidateMatchingPrefix(this.name, this.prefix);
		Assert.assertEquals(this.name + " " + this.prefix, this.expectMatch, actualMatch);
	}

	@Parameters(name = "{0} {1} {2}")
	public static Collection<Object[]> data() {
		List<Object[]> result = new ArrayList<>();
		class Builder {
			public void register(String name, String prefix, boolean expectMatch) {
				result.add(new Object[] { name, prefix, expectMatch });
			}
		}
		Builder b = new Builder();

		b.register("exPLZ:bar", "ex", true);
		b.register("exPLZ:bar", "ex:", false);
		b.register("exPLZ:bar", "exPLZ:bar", true);
		b.register("exPLZ:bar", "exPLZ:ar", true);
		b.register("exPLZ:bar", "plz", true);
		b.register("exPLZ:bar", "ar", true);
		return result;
	}
}
