package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping

import static extension com.zazuko.rdfmapping.dsl.generator.ModelAccess.*

class RmlDialect {

	def staticPrefixes() '''
		PREFIX rr: <http://www.w3.org/ns/r2rml#>
		PREFIX rml: <http://semweb.mmlab.be/ns/rml#>
		PREFIX ql: <http://semweb.mmlab.be/ns/ql#>
	'''

	// TODO: rml:iterator
	def logicalSource(Mapping m) '''
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

	def objectMapReferencePredicate() '''rml:reference'''

}
