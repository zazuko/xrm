package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
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
			rml:source "«sourceResolved»" [
				a carml:Stream ;
				carml:streamName "«name»" ;
			] ;
		«ENDIF»
	'''

	override objectMapMultiReferencePredicate() '''carml:multiReference'''

}
