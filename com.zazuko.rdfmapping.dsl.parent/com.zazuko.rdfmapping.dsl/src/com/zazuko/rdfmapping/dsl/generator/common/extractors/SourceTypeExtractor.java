package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import javax.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;

public class SourceTypeExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, SourceType> {

	@Inject
	public SourceTypeExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(l -> l.getTypeRef() != null ? l.getTypeRef().getType() : null, //
				group -> group.getTypeRef() != null ? group.getTypeRef().getType() : null, //
				nav);
	}

}
