package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import javax.inject.Inject

class R2rmlDialect implements IRmlDialect {

	@Inject
	extension ModelAccess

	override staticPrefixes() '''
		PREFIX rr: <http://www.w3.org/ns/r2rml#>
	'''

	override logicalSource(Mapping it) '''
		rr:logicalTable [ «source.sourceStatement» ];
	'''

	def sourceStatement(
		LogicalSource it) '''«IF sourceIsQueryResolved»rr:sqlQuery """«sourceResolved»"""«ELSE»rr:tableName "«sourceResolved»"«ENDIF»'''

	override objectMapReferencePredicate() '''rr:column'''

	// R2ML does no support multiReference   
	override objectMapMultiReferencePredicate() {
		return objectMapReferencePredicate()
	}
}
