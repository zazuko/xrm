package com.zazuko.rdfmapping.dsl.generator.rml;

import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.IJoinContext;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;

public interface IRmlDialect {

	CharSequence staticPrefixes();
	CharSequence logicalSource(Mapping m, IJoinContext jc);
	CharSequence termMapReferencePredicate();
	CharSequence objectMapMultiReferencePredicate();
}
