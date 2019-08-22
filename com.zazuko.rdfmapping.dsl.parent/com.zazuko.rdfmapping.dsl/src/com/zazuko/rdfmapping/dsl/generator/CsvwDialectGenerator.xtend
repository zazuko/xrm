package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.rdfMapping.LinkedResourceTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import java.text.MessageFormat
import java.util.List

import static extension com.zazuko.rdfmapping.dsl.generator.ModelAccess.*

class CsvwDialectGenerator {

	/*
	 * RML broadens R2RML's scope of application beyond that of relational databases.
	 * For details: http://rml.io/RML_R2RML.html
	 * 
	 * In places where RML and R2RML differ, this generator dispatches to the respective dialect. 
	 */
	final extension CsvwDialect dialect;

	new(CsvwDialect dialect) {
		this.dialect = dialect;
	}
	
	def generateJson(Iterable<Mapping> mappings) '''
		{
			«context()»
			«dialect()»
			«mappings.map[tableSchema].join('\n')»
		}
	'''
	
	def tableSchema(Mapping m) '''
		"url": "«m.source.source»",
		"tableSchema": {
			«m.subjectMap()»
			"columns": [
				«m.subjectTypeMappings()»
				«FOR pom : m.poMappings SEPARATOR ","»
					«pom.column»
				«ENDFOR»
			] 
		}
	'''
	
	def subjectTypeMappings(Mapping m)'''
		«FOR stm : m.subjectTypeMappings SEPARATOR "," AFTER ","»
			{
				"virtual": true,
				"propertyUrl": "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"valueUrl": "«stm.type.vocabulary.prefix.iri»«stm.type.valueResolved»"
			}
		«ENDFOR»
	'''
	
	def subjectMap(Mapping m) '''
		"aboutUrl": "«m.subjectIri»",
	'''
	
	def column(PredicateObjectMapping pom) '''
		{
			"propertyUrl": "«pom.property.vocabulary.prefix.iri»«pom.property.valueResolved»",
			«pom.term.valueReference»
		}
	'''
	
	def dispatch valueReference(ReferenceValuedTerm it) '''
		«termMapAnnex»
		"titles": "«reference.valueResolved»"
	'''
	
	def dispatch valueReference(TemplateValuedTerm it) '''
		"titles": "«references.toList.get(0).valueResolved»",
		"valueUrl": "«toTemplateString»"
	'''
	
	def dispatch valueReference(LinkedResourceTerm it) '''
		"titles": "«references.toList.get(0).valueResolved»",
		"valueUrl": "«toTemplateString»"
	'''
	
	def termMapAnnex(ReferenceValuedTerm it) '''
		«IF languageTag !== null»
			"lang": "«languageTag.name»",
		«ELSEIF datatype !== null»
			"datatype": "«datatype.valueResolved»",
		«ENDIF»
	'''
	
	def subjectIri(Mapping it) {
		subjectIriMapping.toTemplateString
	}
	
	def toTemplateString(TemplateValuedTerm it) {		
		template.apply(references);
	}
	
	def toTemplateString(LinkedResourceTerm it) {		
		mapping.subjectIriMapping.template.apply(references);
	}
	
	def apply(String template, List<Referenceable> refs) {
		MessageFormat.format(template, refs.toMessageFormatArguments());
	}
	
	def toMessageFormatArguments(List<Referenceable> refs) {
		refs.map[ref | '''{«ref.valueResolved»}'''].toArray
	}
	
}