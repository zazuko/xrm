package com.zazuko.rdfmapping.dsl.tests.generator.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.zazuko.rdfmapping.dsl.generator.common.GlueingContext;

public class GlueingContextTest {

	private GlueingContext ctx;

	@BeforeAll
	public void before() {
		this.ctx = new GlueingContext();
	}

	@ParameterizedTest
	@MethodSource("data")
	public void test(Params params) {
		for (boolean current : params.segments) {
			this.ctx.registerSegmentMetadata(current);
		}
		boolean[] actual = new boolean[params.expectedResults.length];
		for (int a = 0; a < params.expectedResults.length; a++) {
			actual[a] = this.ctx.needsGlueing();
		}

		Assertions.assertArrayEquals(params.expectedResults, actual);
	}

	static class Params {
		private boolean[] segments;
		private boolean[] expectedResults;

		public Params(boolean[] segments, boolean[] expectedResults) {
			super();
			this.segments = segments;
			this.expectedResults = expectedResults;
		}

	}

	public static Stream<Params> data() {
		List<Params> result = new ArrayList<>();
		class Builder {
			public void register(boolean[] segments, boolean[] expectedGlues) {
				result.add(new Params(segments, expectedGlues));
			}
		}
		Builder b = new Builder();

		// cheating here for the sake of better readability
		final boolean y = true, n = false;

		b.register(new boolean[] { n, n, n }, new boolean[] { n, n });
		b.register(new boolean[] { n, n, y }, new boolean[] { n, n });
		b.register(new boolean[] { n, y, n }, new boolean[] { n, n });
		b.register(new boolean[] { n, y, y }, new boolean[] { n, y });

		b.register(new boolean[] { y, n, n }, new boolean[] { n, n });
		b.register(new boolean[] { y, n, y }, new boolean[] { y, n });
		b.register(new boolean[] { y, y, n }, new boolean[] { y, n });
		b.register(new boolean[] { y, y, y }, new boolean[] { y, y });

		return result.stream();

	}
}
