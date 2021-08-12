package com.zazuko.rdfmapping.dsl.generator.shacl

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.NodeShape
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShape
import com.zazuko.rdfmapping.dsl.rdfMapping.Shape
import javax.inject.Inject

class ShaclGenerator {

	@Inject
	extension ModelAccess

	@Inject
	extension ShapeModelAccess

	def generateTurtle(Iterable<Shape> shapes, String base) {
		val baseDeclaration = !base.isNullOrEmpty ? '''BASE <«base»>''' + '\n' : '';

		return baseDeclaration + shapes.prefixes + shapes.map[shape].join('\n')
	}

	def prefixes(Iterable<Shape> shapes) '''
		«staticPrefixes»
		«FOR prefixHolder : shapes.prefixesUsed.inDeterministicOrder»
			PREFIX «prefixHolder.prefix.label»: <«prefixHolder.prefix.iri»>
		«ENDFOR»
		
	'''

	def dispatch shape(NodeShape it) '''
		<«name»> a sh:NodeShape .
		«FOR cls : targetClasses»
			<«name»> sh:targetClass «cls.vocabulary.prefix.label»:«cls.valueResolved» .
		«ENDFOR»
	'''

	def dispatch shape(PropertyShape it) '''
		 <«name»> a sh:PropertyShape .
	'''

	def staticPrefixes() '''
		PREFIX sh: <http://www.w3.org/ns/shacl#>
	'''

}
