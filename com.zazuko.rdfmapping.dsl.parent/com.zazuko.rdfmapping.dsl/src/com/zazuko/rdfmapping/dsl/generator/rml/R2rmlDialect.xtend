package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping

import static extension com.zazuko.rdfmapping.dsl.generator.common.ModelAccess.*

class R2rmlDialect extends RmlDialect {

	override staticPrefixes() '''
		PREFIX rr: <http://www.w3.org/ns/r2rml#>
	'''

	override logicalSource(Mapping m) '''
		rr:logicalTable [ rr:tableName "«m.source.sourceResolved»" ];
	'''

	override objectMapReferencePredicate() '''rr:column'''

}
