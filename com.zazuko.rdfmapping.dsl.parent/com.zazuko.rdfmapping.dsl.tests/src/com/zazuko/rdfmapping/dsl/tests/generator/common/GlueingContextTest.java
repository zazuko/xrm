package com.zazuko.rdfmapping.dsl.tests.generator.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.zazuko.rdfmapping.dsl.generator.common.GlueingContext;

@RunWith(Parameterized.class)
public class GlueingContextTest {

	private boolean[] segments;
	private boolean[] expectedResults;
	private GlueingContext ctx;

	public GlueingContextTest(boolean[] segments, boolean[] expectedResults) {
		this.segments = segments;
		this.expectedResults = expectedResults;
	}

	@Before
	public void before() {
		this.ctx = new GlueingContext();
		for (boolean current : this.segments) {
			this.ctx.registerSegmentMetadata(current);
		}
	}

	@Test
	public void test() {
		boolean[] actual = new boolean[this.expectedResults.length];
		for (int a = 0; a < this.expectedResults.length; a++) {
			actual[a] = this.ctx.needsGlueing();
		}

		Assert.assertArrayEquals(this.expectedResults, actual);
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> result = new ArrayList<>();
		class Builder {
			public void register(boolean[] segments, boolean[] expectedGlues) {
				result.add(new Object[] { segments, expectedGlues });
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

		return result;

	}
}
