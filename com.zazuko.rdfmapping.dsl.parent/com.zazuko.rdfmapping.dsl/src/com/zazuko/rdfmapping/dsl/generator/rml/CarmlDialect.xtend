package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
import javax.inject.Inject

class CarmlDialect extends RmlDialect implements IRmlDialect {

	@Inject
	extension ModelAccess

	override staticPrefixes() '''
		«super.staticPrefixes»
		PREFIX carml: <http://carml.taxonic.com/carml/>
	'''

	override sourceStatement(LogicalSource it) '''
		«IF sourceIsQueryResolved»
			rml:query """«sourceResolved»""" ;
		«ELSE»
			rml:source [
				a carml:Stream ;
				carml:streamName "«sourceResolved»" ;
				«IF xmlNamespaceExtension !== null»
				«xmlNamespaceExtension.expand»
				«ENDIF»
			] ;
		«ENDIF»
	'''
	
	def private expand(XmlNamespaceExtension it) '''
		«FOR Prefix p : prefixes»
		 carml:declaresNamespace [
		 	carml:namespacePrefix "«p.label»" ;
		 	carml:namespaceName "«p.iri»" ;
		 ] ;
		«ENDFOR»
	'''

	override objectMapMultiReferencePredicate() '''carml:multiReference'''

}
