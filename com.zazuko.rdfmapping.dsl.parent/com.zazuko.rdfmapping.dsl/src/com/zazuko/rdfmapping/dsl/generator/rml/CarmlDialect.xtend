package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.rml.statefuljoiner.IJoinContext
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import javax.inject.Inject

class CarmlDialect extends RmlDialect implements IRmlDialect {

	@Inject
	extension ModelAccess

	override staticPrefixes() '''
		«super.staticPrefixes»
		PREFIX carml: <http://carml.taxonic.com/carml/>
	'''

	override sourceStatement(LogicalSource it, IJoinContext jc) '''
		«IF sourceIsQueryResolved»
			rml:query """«sourceResolved»"""«jc.acquireMarker»
		«ELSE»
			rml:source [
				a carml:Stream«jc.acquireMarker»
				carml:streamName "«sourceResolved»"«jc.acquireMarker»
				«IF xmlNamespaceExtension !== null»
				«FOR Prefix p : xmlNamespaceExtension.prefixes»
				«p.declareNamespace(jc.newContext)»«jc.acquireMarker»
				«ENDFOR»
				«ENDIF»
			]«jc.acquireMarker»
		«ENDIF»
	'''
	
	def private declareNamespace(Prefix it, IJoinContext jc) '''
		 carml:declaresNamespace [
		 	carml:namespacePrefix "«label»"«jc.acquireMarker»
		 	carml:namespaceName "«iri»"«jc.acquireMarker»
		 ]'''

	override objectMapMultiReferencePredicate() '''carml:multiReference'''

}
