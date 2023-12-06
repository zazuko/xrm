package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import jakarta.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension;

public class XmlNamespaceExtensionExtractor extends TwoLevelShadowedConfigExtractor<LogicalSource, SourceGroup, XmlNamespaceExtension> {

	@Inject
	public XmlNamespaceExtensionExtractor(LogicalSource2SourceGroupNavigation nav) {
		super(LogicalSource::getXmlNamespaceExtension, SourceGroup::getXmlNamespaceExtension, nav);
	}

}
