package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.IJoinContext
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import jakarta.inject.Inject

class RmlDialect implements IRmlDialect {

	@Inject
	extension ModelAccess

	override staticPrefixes() '''
		PREFIX rr: <http://www.w3.org/ns/r2rml#>
		PREFIX rml: <http://semweb.mmlab.be/ns/rml#>
		PREFIX ql: <http://semweb.mmlab.be/ns/ql#>
	'''

	override logicalSource(Mapping it, IJoinContext jc) '''
		rml:logicalSource [
			«source.sourceStatement(jc)»
			rml:referenceFormulation «source.typeResolved?.referenceFormulation»«jc.acquireMarker»«IF source.iterator !== null»
			rml:iterator "«source.iterator»"«jc.acquireMarker»«ENDIF»
		]'''

	def sourceStatement(LogicalSource it, IJoinContext jc) '''
		«IF sourceIsQueryResolved»
			rml:query """«sourceResolved»"""«jc.acquireMarker»
		«ELSE»
			rml:source "«sourceResolved»"«jc.acquireMarker»
		«ENDIF»
	'''

	override objectMapReferencePredicate() '''rml:reference'''

	// Plain RML does no support multiReference. CarmlDialect overrides this with 'carml:multiReference'   
	override objectMapMultiReferencePredicate() {
		return objectMapReferencePredicate()
	}

}
