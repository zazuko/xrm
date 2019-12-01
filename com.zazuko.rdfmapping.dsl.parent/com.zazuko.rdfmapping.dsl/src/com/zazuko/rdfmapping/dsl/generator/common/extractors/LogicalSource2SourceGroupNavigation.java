package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import java.util.function.Function;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;

public class LogicalSource2SourceGroupNavigation implements Function<LogicalSource, SourceGroup> {

	@Override
	public SourceGroup apply(LogicalSource t) {
		if (t != null && t.eContainer() instanceof SourceGroup) {
			return (SourceGroup) t.eContainer();
		}
		return null;
	}

}
