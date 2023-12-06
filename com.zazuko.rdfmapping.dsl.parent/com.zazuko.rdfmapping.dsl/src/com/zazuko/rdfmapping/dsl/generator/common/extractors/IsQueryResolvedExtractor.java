package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import jakarta.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;

public class IsQueryResolvedExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, Boolean> {

	@Inject
	public IsQueryResolvedExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(LogicalSource::isSourceIsQuery, SourceGroup::isSourceIsQuery, nav);
		this.setNullValueSupplier(() -> false);
	}

}
