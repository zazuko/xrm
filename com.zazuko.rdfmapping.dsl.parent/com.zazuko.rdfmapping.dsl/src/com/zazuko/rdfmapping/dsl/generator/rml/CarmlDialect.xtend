package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import javax.inject.Inject

class CarmlDialect extends RmlDialect implements IRmlDialect {

	@Inject
	extension ModelAccess

	override staticPrefixes() '''
		«super.staticPrefixes»
		PREFIX carml: <http://carml.taxonic.com/carml/>
	'''

	override sourceStatement(LogicalSource it, Mapping mapping) '''
		«IF sourceIsQueryResolved»
			rml:query """«sourceResolved»""" ;
		«ELSE»
			rml:source [
				a carml:Stream ;
				carml:streamName "«sourceResolved»" ;
				«IF xmlNamespaceExtension !== null»
				«FOR Prefix p : xmlNamespaceExtension.prefixesUsed(mapping)»
				«p.declareNamespace»
				«ENDFOR»
				«ENDIF»
			] ;
		«ENDIF»
	'''
	
	def private declareNamespace(Prefix it) '''
		 carml:declaresNamespace [
		 	carml:namespacePrefix "«label»" ;
		 	carml:namespaceName "«iri»" ;
		 ] ;
	'''

	override objectMapMultiReferencePredicate() '''carml:multiReference'''

}
