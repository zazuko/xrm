package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
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

	override logicalSource(Mapping it) '''
		rml:logicalSource [
			«source.sourceStatement(it)»
			rml:referenceFormulation «source.typeResolved?.referenceFormulation»«IF source.iterator !== null» ;
			rml:iterator "«source.iterator»" ;«ENDIF»
		];
	'''

	def sourceStatement(LogicalSource it, Mapping mapping) '''
		«IF sourceIsQueryResolved»
			rml:query """«sourceResolved»""" ;
		«ELSE»
			rml:source "«sourceResolved»" ;
		«ENDIF»
	'''

	override objectMapReferencePredicate() '''rml:reference'''

	// Plain RML does no support multiReference. CarmlDialect overrides this with 'carml:multiReference'   
	override objectMapMultiReferencePredicate() {
		return objectMapReferencePredicate()
	}

}
