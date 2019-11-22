package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import javax.inject.Inject

class RmlDialect implements IRmlDialect {
	
	@Inject
	extension ModelAccess
	
	override staticPrefixes() '''
		PREFIX rr: <http://www.w3.org/ns/r2rml#>
		PREFIX rml: <http://semweb.mmlab.be/ns/rml#>
		PREFIX ql: <http://semweb.mmlab.be/ns/ql#>
	'''

	// TODO: rml:iterator
	override logicalSource(Mapping m) '''
		«IF m.source.sourceIsQueryResolved»
			rml:logicalSource [
				rml:query """«m.source.sourceResolved»""" ;
				rml:referenceFormulation «m.source.typeResolved?.referenceFormulation»
			];
		«ELSE»
			rml:logicalSource [
				rml:source "«m.source.sourceResolved»" ;
				rml:referenceFormulation «m.source.typeResolved?.referenceFormulation»
			];
		«ENDIF»		
	'''

	override objectMapReferencePredicate() '''rml:reference'''

}
