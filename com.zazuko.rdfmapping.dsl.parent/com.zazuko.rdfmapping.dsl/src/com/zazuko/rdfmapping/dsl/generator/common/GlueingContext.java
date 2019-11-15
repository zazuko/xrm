package com.zazuko.rdfmapping.dsl.generator.common;

import java.util.ArrayList;
import java.util.List;

/**
 * You generate things belonging together - let's call them segments. In the
 * generated stuff, you need to glue segments together by some characters, e.g.
 * a comma. So if this sequence of segments come from more than source such as
 * one single list, then it gets hard to decide whether to print these glueing
 * characters is needed.
 * <p>
 * Usage: Feed your instance of a GlueingContext with metadata about your
 * segments, tell whether the segments do contain data. The GlueingContext is
 * now initialized. Now simply ask the context for each transistion, whether
 * glueing is needed.
 * 
 *
 */
public class GlueingContext {

	private List<Boolean> knownSegmentData;
	private int index;

	public GlueingContext() {
		this.knownSegmentData = new ArrayList<>();
	}

	public void registerSegmentMetadata(boolean hasContent) {
		this.knownSegmentData.add(hasContent);
	}

	/**
	 * No parameters here, not even an index. This is built to be used within
	 * RichString of xtend, you don't want to use local variables there. It would
	 * most probably mess with your generated stuff.
	 * 
	 * @return
	 */
	public boolean needsGlueing() {
		int nrOfMaxCalls = this.knownSegmentData.size() - 1;
		if (this.index >= nrOfMaxCalls) {
			throw new IllegalStateException(
					String.format("nrOfMaxCalls is %s and this is call number %s", nrOfMaxCalls, this.index + 1));
		}
		int currentIndex = this.index++;
		boolean meActivated = this.knownSegmentData.get(currentIndex);
		if (!meActivated) {
			return false;
		}

		// is one of my successors activated?
		for (int a = currentIndex + 1; a < this.knownSegmentData.size(); a++) {
			if (this.knownSegmentData.get(a)) {
				return true; // a successor has data, so we need to glue
			}
		}

		// nope, no successor with data found - nothing to glue here
		return false;
	}
}
