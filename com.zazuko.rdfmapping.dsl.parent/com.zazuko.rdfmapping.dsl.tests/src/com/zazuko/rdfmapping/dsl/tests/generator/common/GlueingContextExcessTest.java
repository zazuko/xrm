package com.zazuko.rdfmapping.dsl.tests.generator.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.generator.common.GlueingContext;

public class GlueingContextExcessTest {

	@Test
	public void exceedNrOfRequests() {
		GlueingContext ctx = new GlueingContext();

		// register three segments
		ctx.registerSegmentMetadata(true);
		ctx.registerSegmentMetadata(true);
		ctx.registerSegmentMetadata(true);

		// ask for glueing twice is ok
		ctx.needsGlueing();
		ctx.needsGlueing();

		// but for three segments, there are at most three transitions. so this must
		// fail
		try {
			ctx.needsGlueing();
			Assertions.fail();
		} catch (IllegalStateException e) {
			Assertions.assertEquals("nrOfMaxCalls is 2 and this is call number 3", e.getMessage());
		}
	}
}
