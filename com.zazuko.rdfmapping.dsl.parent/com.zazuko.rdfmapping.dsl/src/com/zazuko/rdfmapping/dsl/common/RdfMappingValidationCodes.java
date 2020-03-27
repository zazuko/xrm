package com.zazuko.rdfmapping.dsl.common;

public interface RdfMappingValidationCodes {

	String DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS = "domainmodel.outputtype.superfluous";

	String MAPPING_OUTPUTTYPE_MISSING = "mapping.outputtype.missing";
	String MAPPING_OUTPUTTYPE_INCOMPATIBLE = "mapping.outputtype.incompatible";

	String POM_LINE_END_MISSING = "predicateobjectmapping.linend.missing";
	
	String PARENTTRIPLESMAP_OUTPUTTYPE_INCOMPATIBLE = "parenttriplesmap.outputtype.incompatible";
	
	String EOBJECT_SUPERFLUOUS = "eobject.superfluous";
	String EOBJECT_SUPERFLUOUS_NOFIX = EOBJECT_SUPERFLUOUS + ".nofix";

	String PREFIX_LABEL_SEPARATOR = "prefix.label.with.separator";
}
