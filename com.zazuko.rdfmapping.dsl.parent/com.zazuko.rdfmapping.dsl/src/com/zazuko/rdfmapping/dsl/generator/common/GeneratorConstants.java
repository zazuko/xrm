package com.zazuko.rdfmapping.dsl.generator.common;

import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
import com.zazuko.rdfmapping.dsl.util.EnumMapper;
import com.zazuko.rdfmapping.dsl.util.EnumMapperBuilder;

public class GeneratorConstants {
	
	public static final String DIALECT_RML = "RML";
	public static final String DIALECT_R2RML = "R2RML";

	public static final EnumMapper<SourceType> REFERENCE_FORMULATION = new EnumMapperBuilder<SourceType>() //
			.add("ql:XPath", SourceType.XML) //
			.add("rr:SQL2008", SourceType.RDB) //
			.add("ql:CSV", SourceType.CSV) //
			.build();
}
