package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import jakarta.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.NullValueDeclaration;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;

public class CsvNullValueExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, String> {

	@Inject
	public CsvNullValueExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(//
				ls -> value(ls.getNullValueMarker()), //
				sg -> value(sg.getNullValueMarker()), //
				nav);
	}

	private static String value(NullValueDeclaration in) {
		return in != null ? in.getNullValue() : null;
	}

}
