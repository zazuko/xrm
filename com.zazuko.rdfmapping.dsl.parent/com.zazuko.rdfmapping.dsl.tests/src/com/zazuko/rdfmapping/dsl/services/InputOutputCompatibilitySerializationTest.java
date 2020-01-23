package com.zazuko.rdfmapping.dsl.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

@RunWith(Parameterized.class)
public class InputOutputCompatibilitySerializationTest {

	private InputOutputCompatibility compat;

	private final Set<OutputType> types;

	public InputOutputCompatibilitySerializationTest(Set<OutputType> types) {
		this.types = types;
	}

	@Before
	public void before() {
		this.compat = new InputOutputCompatibility();
	}

	@Test
	public void model2text2model() {
		String serialized = this.compat.serialize2Message(this.types);
		Set<OutputType> actual = this.compat.extractFromMessage(serialized, OutputType.class);
		Assert.assertEquals(this.types, actual);
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> result = new ArrayList<>();

		result.add(new Object[] { new LinkedHashSet<>(OutputType.VALUES) });
		result.add(new Object[] { Collections.emptySet() });

		return result;
	}
}
