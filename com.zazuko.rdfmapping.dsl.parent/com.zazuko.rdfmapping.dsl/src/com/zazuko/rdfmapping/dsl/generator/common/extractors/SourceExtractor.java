package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import javax.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;

public class SourceExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, String> {

	@Inject
	public SourceExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(LogicalSource::getSource, SourceGroup::getSource, nav);
	}

}
