package com.zazuko.rdfmapping.dsl.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.zazuko.rdfmapping.dsl.validation.IriFormatAnalysis;
import com.zazuko.rdfmapping.dsl.validation.IriFormatAnalyzer;

@RunWith(Parameterized.class)
public class IriFormatAnalyzerTest {

	private IriFormatAnalyzer analyzer;
	private int[] usedKeys;
	private int[] unusedKeys;
	private String input;

	public IriFormatAnalyzerTest(int[] usedKeys, int[] unusedKeys, String input) {
		this.usedKeys = usedKeys;
		this.unusedKeys = unusedKeys;
		this.input = input;
	}

	@Before
	public void before() {
		this.analyzer = new IriFormatAnalyzer();
	}

	@Test
	public void test() throws Exception {
		IriFormatAnalysis actual = this.analyzer.analyzeFormats(this.input);
		compare(this.usedKeys, actual.getUsedKeys());
		compare(this.unusedKeys, actual.getSkippedKeys());
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> result = new ArrayList<>();
		
		result.add(new Object[] {
			new int[] {0, 1, 2},
			new int[] {},
			"http://city.example.com/{0}/{1}/{2}"
		});
		
		result.add(new Object[] {
				new int[] {0, 1, 2},
				new int[] {},
				"http://city.example.com/{2}/{1}/{0}"
		});
		
		result.add(new Object[] {
				new int[] {1, 2},
				new int[] {0},
				"http://city.example.com/{1}/{2}"
			});

		result.add(new Object[] {
				new int[] {0, 1, 3, 5},
				new int[] {2, 4},
				"http://city.example.com/{0}/{5}/{3}/whatever/{1}"
		});
		
		
		return result;
	}

	private void compare(int[] expected, Set<Integer> actual) {
		Assert.assertEquals(expected.length, actual.size());
		for (int a = 0; a < expected.length; a++) {
			int e = expected[a];
			Assert.assertTrue(actual.contains(Integer.valueOf(e)));
		}
	}

}
