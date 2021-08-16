package com.zazuko.rdfmapping.dsl.generator.shacl

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShape
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeAny
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeBlankNode
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeBlankNodeOrIRI
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeBlankNodeOrLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeIRI
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeIRIOrLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.EmbeddedPropertyShapeLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.NodeShape
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShape
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeAny
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeBlankNode
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeBlankNodeOrIRI
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeBlankNodeOrLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeIRI
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeIRIOrLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.PropertyShapeLiteral
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass
import com.zazuko.rdfmapping.dsl.rdfMapping.Shape
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary
import java.util.LinkedHashSet
import java.util.Set
import javax.inject.Inject
import org.eclipse.emf.common.util.ECollections
import org.eclipse.emf.common.util.EList

class ShapeModelAccess {

	@Inject
	extension ModelAccess

	def dispatch Set<Vocabulary> prefixesUsedInShape(NodeShape it) {
		val Set<Vocabulary> result = new LinkedHashSet();
		result.addAll(targetClasses.map[cls|cls.vocabulary]);
		result.addAll(ignoredProperties.map[p|p.vocabulary]);

		for (EmbeddedPropertyShape eps : embeddedPropertyShapes) {
			result.add(eps.property.vocabulary);

			if (eps.datatype !== null) {
				result.add(eps.datatype.vocabulary);
			}

			result.addAll(eps.classes.map[cls|cls.vocabulary]);
		}

		return result
	}

	def dispatch Set<Vocabulary> prefixesUsedInShape(PropertyShape it) {
		val Set<Vocabulary> result = new LinkedHashSet();
		
		if (property !== null) {
			result.add(property.vocabulary);
		}

		if (datatype !== null) {
			result.add(datatype.vocabulary);
		}

		result.addAll(classes.map[cls|cls.vocabulary]);

		return result
	}

	def Set<Vocabulary> prefixesUsed(Iterable<Shape> shapes) {
		return shapes.map[m|m.prefixesUsedInShape].flatten.toSet;
	}

	def EList<RdfClass> classes(EmbeddedPropertyShape it) {
		switch it {
			EmbeddedPropertyShapeIRI: classes
			EmbeddedPropertyShapeBlankNode: classes
			EmbeddedPropertyShapeBlankNodeOrIRI: classes
			EmbeddedPropertyShapeLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeIRIOrLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeBlankNodeOrLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeAny: classes
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def EList<RdfClass> classes(PropertyShape it) {
		switch it {
			PropertyShapeIRI: classes
			PropertyShapeBlankNode: classes
			PropertyShapeBlankNodeOrIRI: classes
			PropertyShapeLiteral: ECollections.emptyEList()
			PropertyShapeIRIOrLiteral: ECollections.emptyEList()
			PropertyShapeBlankNodeOrLiteral: ECollections.emptyEList()
			PropertyShapeAny: classes
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def EList<NodeShape> nodeShapes(EmbeddedPropertyShape it) {
		switch it {
			EmbeddedPropertyShapeIRI: nodeShapes
			EmbeddedPropertyShapeBlankNode: nodeShapes
			EmbeddedPropertyShapeBlankNodeOrIRI: nodeShapes
			EmbeddedPropertyShapeLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeIRIOrLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeBlankNodeOrLiteral: ECollections.emptyEList()
			EmbeddedPropertyShapeAny: nodeShapes
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def EList<NodeShape> nodeShapes(PropertyShape it) {
		switch it {
			PropertyShapeIRI: nodeShapes
			PropertyShapeBlankNode: nodeShapes
			PropertyShapeBlankNodeOrIRI: nodeShapes
			PropertyShapeLiteral: ECollections.emptyEList()
			PropertyShapeIRIOrLiteral: ECollections.emptyEList()
			PropertyShapeBlankNodeOrLiteral: ECollections.emptyEList()
			PropertyShapeAny: nodeShapes
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def Datatype datatype(EmbeddedPropertyShape it) {
		switch it {
			EmbeddedPropertyShapeIRI: null
			EmbeddedPropertyShapeBlankNode: null
			EmbeddedPropertyShapeBlankNodeOrIRI: null
			EmbeddedPropertyShapeLiteral: datatype
			EmbeddedPropertyShapeIRIOrLiteral: null
			EmbeddedPropertyShapeBlankNodeOrLiteral: null
			EmbeddedPropertyShapeAny: datatype
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def Datatype datatype(PropertyShape it) {
		switch it {
			PropertyShapeIRI: null
			PropertyShapeBlankNode: null
			PropertyShapeBlankNodeOrIRI: null
			PropertyShapeLiteral: datatype
			PropertyShapeIRIOrLiteral: null
			PropertyShapeBlankNodeOrLiteral: null
			PropertyShapeAny: datatype
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def String pattern(EmbeddedPropertyShape it) {
		switch it {
			EmbeddedPropertyShapeIRI: pattern
			EmbeddedPropertyShapeBlankNode: null
			EmbeddedPropertyShapeBlankNodeOrIRI: null
			EmbeddedPropertyShapeLiteral: pattern
			EmbeddedPropertyShapeIRIOrLiteral: pattern
			EmbeddedPropertyShapeBlankNodeOrLiteral: null
			EmbeddedPropertyShapeAny: pattern
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def String pattern(PropertyShape it) {
		switch it {
			PropertyShapeIRI: pattern
			PropertyShapeBlankNode: null
			PropertyShapeBlankNodeOrIRI: null
			PropertyShapeLiteral: pattern
			PropertyShapeIRIOrLiteral: pattern
			PropertyShapeBlankNodeOrLiteral: null
			PropertyShapeAny: pattern
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def nodeKind(EmbeddedPropertyShape it) {
		switch it {
			EmbeddedPropertyShapeIRI: "sh:IRI"
			EmbeddedPropertyShapeBlankNode: "sh:BlankNode"
			EmbeddedPropertyShapeBlankNodeOrIRI: "sh:BlankNodeOrIRI"
			EmbeddedPropertyShapeLiteral: "sh:Literal"
			EmbeddedPropertyShapeIRIOrLiteral: "sh:IRIOrLiteral"
			EmbeddedPropertyShapeBlankNodeOrLiteral: "sh:BlankNodeOrLiteral"
//intentionally unexpected:	EmbeddedPropertyShapeAny: null
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

	def nodeKind(PropertyShape it) {
		switch it {
			PropertyShapeIRI: "sh:IRI"
			PropertyShapeBlankNode: "sh:BlankNode"
			PropertyShapeBlankNodeOrIRI: "sh:BlankNodeOrIRI"
			PropertyShapeLiteral: "sh:Literal"
			PropertyShapeIRIOrLiteral: "sh:IRIOrLiteral"
			PropertyShapeBlankNodeOrLiteral: "sh:BlankNodeOrLiteral"
//intentionally unexpected:	PropertyShapeAny: null
			default: throw new RuntimeException("unexpected type " + class)
		}
	}

}
