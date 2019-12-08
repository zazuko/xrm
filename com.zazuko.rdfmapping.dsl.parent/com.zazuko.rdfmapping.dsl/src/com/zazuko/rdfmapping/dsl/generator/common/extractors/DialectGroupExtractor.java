package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import javax.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;

public class DialectGroupExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, DialectGroup> {

	@Inject
	public DialectGroupExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(LogicalSource::getDialect, SourceGroup::getDialect, nav);
	}

}
