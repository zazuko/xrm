package com.zazuko.rdfmapping.dsl.generator.shacl

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.NodeShape
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShape
import com.zazuko.rdfmapping.dsl.rdfMapping.Shape
import javax.inject.Inject
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShape
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeAny
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeAny

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
		«IF deactivated»<«name»> sh:deactivated true .«ENDIF»
		«FOR cls : targetClasses»
			<«name»> sh:targetClass «cls.vocabulary.prefix.label»:«cls.valueResolved» .
		«ENDFOR»
		«IF closed»<«name»> sh:closed true .«ENDIF»
		«IF !ignoredProperties.empty»
			<«name»> sh:ignoredProperties («FOR p : ignoredProperties SEPARATOR ' '»«p.vocabulary.prefix.label»:«p.valueResolved»«ENDFOR») .
		«ENDIF»
		«FOR p : embeddedPropertyShapes»
			«p.propertyConstraint(it)»
		«ENDFOR»
		«FOR p : propertyShapes»
			<«name»> sh:property <«p.name»> .
		«ENDFOR»
	'''
	
	def propertyConstraint(EmbeddedPropertyShape it, NodeShape subject) '''
		<«subject.name»> sh:property [
			sh:path «property.vocabulary.prefix.label»:«property.valueResolved» ;
			«IF ! (it instanceof EmbeddedPropertyShapeAny)»sh:nodeKind «nodeKind» ;«ENDIF»
			«IF cardinality.min > 0»sh:minCount «cardinality.min» ;«ENDIF»
			«IF cardinality.max != 0»sh:maxCount «cardinality.max» ;«ENDIF»
			«IF datatype !== null»sh:datatype «datatype.vocabulary.prefix.label»:«datatype.valueResolved» ;«ENDIF»
			«IF pattern !== null»sh:pattern "«pattern»" ;«ENDIF»
			«FOR cls : classes»
				sh:class «cls.vocabulary.prefix.label»:«cls.valueResolved» ;
			«ENDFOR»
			«FOR nodeShape : nodeShapes»
				sh:node <«nodeShape.name»> ;
			«ENDFOR»
		] .
	'''	

	def dispatch shape(PropertyShape it) '''
		 <«name»> a sh:PropertyShape .
		 «IF deactivated»<«name»> sh:deactivated true .«ENDIF»
		 <«name»> sh:path «property.vocabulary.prefix.label»:«property.valueResolved» .
		 «IF ! (it instanceof PropertyShapeAny)»<«name»> sh:nodeKind «nodeKind» .«ENDIF»
		 «IF cardinality.min > 0»<«name»> sh:minCount «cardinality.min» .«ENDIF»
		 «IF cardinality.max != 0»<«name»> sh:maxCount «cardinality.max» .«ENDIF»
		 «IF datatype !== null»<«name»> sh:datatype «datatype.vocabulary.prefix.label»:«datatype.valueResolved» .«ENDIF»
		 «IF pattern !== null»<«name»> sh:pattern "«pattern»" .«ENDIF»
		 «FOR cls : classes»
		 	<«name»> sh:class «cls.vocabulary.prefix.label»:«cls.valueResolved» .
		 «ENDFOR»
		 «FOR nodeShape : nodeShapes»
		 	<«name»> sh:node <«nodeShape.name»> .
		 «ENDFOR»
	'''

	def staticPrefixes() '''
		PREFIX sh: <http://www.w3.org/ns/shacl#>
	'''

}
