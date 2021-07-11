package com.zazuko.rdfmapping.dsl.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.zazuko.rdfmapping.dsl.validation.TemplateFormatAnalysis;
import com.zazuko.rdfmapping.dsl.validation.TemplateFormatAnalyzer;

public class TemplateFormatAnalyzerTest {

	private TemplateFormatAnalyzer analyzer;

	@BeforeAll
	public void before() {
		this.analyzer = new TemplateFormatAnalyzer();
	}

	@ParameterizedTest
	@MethodSource("provide")
	public void test(Params params) throws Exception {
		TemplateFormatAnalysis actual = this.analyzer.analyzeFormats(params.input);
		compare(params.usedKeys, actual.getUsedKeys());
		compare(params.unusedKeys, actual.getSkippedKeys());
	}

	static class Params {
		private int[] usedKeys;
		private int[] unusedKeys;
		private String input;

		public Params(int[] usedKeys, int[] unusedKeys, String input) {
			super();
			this.usedKeys = usedKeys;
			this.unusedKeys = unusedKeys;
			this.input = input;
		}

	}

	public static Stream<Params> provide() {
		List<Params> result = new ArrayList<>();

		result.add(new Params(new int[] { 0, 1, 2 }, new int[] {}, "http://city.example.com/{0}/{1}/{2}"));

		result.add(new Params(new int[] { 0, 1, 2 }, new int[] {}, "http://city.example.com/{2}/{1}/{0}"));

		result.add(new Params(new int[] { 1, 2 }, new int[] { 0 }, "http://city.example.com/{1}/{2}"));

		result.add(new Params(new int[] { 0, 1, 3, 5 }, new int[] { 2, 4 },
				"http://city.example.com/{0}/{5}/{3}/whatever/{1}"));

		return result.stream();
	}

	private void compare(int[] expected, Set<Integer> actual) {
		Assertions.assertEquals(expected.length, actual.size());
		for (int a = 0; a < expected.length; a++) {
			int e = expected[a];
			Assertions.assertTrue(actual.contains(Integer.valueOf(e)));
		}
	}

}
