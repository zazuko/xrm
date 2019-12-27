package com.zazuko.rdfmapping.dsl.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public interface RdfMappingConstants {

	public static final Set<OutputType> RMLISH_OUTPUTTYPES = Collections
			.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(new OutputType[] { OutputType.RML, OutputType.R2RML, OutputType.CARML })));

}
