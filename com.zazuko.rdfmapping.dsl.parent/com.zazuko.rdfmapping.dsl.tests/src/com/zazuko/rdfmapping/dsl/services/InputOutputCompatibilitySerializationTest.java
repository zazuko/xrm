package com.zazuko.rdfmapping.dsl.services;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.tests.RdfMappingInjectorProvider;

@ExtendWith(InjectionExtension.class)
@InjectWith(RdfMappingInjectorProvider.class)
public class InputOutputCompatibilitySerializationTest {

	private InputOutputCompatibility compat;

	@BeforeEach
	public void before() {
		this.compat = new InputOutputCompatibility();
	}

	@ParameterizedTest
	@MethodSource("provideModel2Text")
	public void model2text2model(Set<OutputType> types) {
		String serialized = this.compat.serialize2Message(types);
		Set<OutputType> actual = this.compat.extractFromMessage(serialized, OutputType.class);
		Assertions.assertEquals(types, actual);
	}

	public static Stream<Set<OutputType>> provideModel2Text() {

		return Stream.of(new LinkedHashSet<>(OutputType.VALUES), Collections.emptySet());
	}
}
