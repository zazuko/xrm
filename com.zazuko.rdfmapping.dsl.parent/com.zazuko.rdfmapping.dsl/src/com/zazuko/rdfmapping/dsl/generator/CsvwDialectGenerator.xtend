package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.LinkedResourceTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import java.text.MessageFormat
import java.util.List

import static extension com.zazuko.rdfmapping.dsl.generator.ModelAccess.*

class CsvwDialectGenerator {

	final extension CsvwDialect dialect;

	new(CsvwDialect dialect) {
		this.dialect = dialect;
	}
	
	def generateJson(Iterable<Mapping> mappings) '''
		{
			«context()»
			"tables": [
				«mappings.map[tableSchema].join('\n,')»
			]
		}
	'''
	def dialect(DialectGroup d) '''
		"dialect": {
			"delimiter": "«d.delimiter»"
			«IF d.commentPrefix !== null»
				,"commentPrefix": "«d.commentPrefix»"
			«ENDIF»
			«IF d.doubleQuote !== null»
				,"doubleQuote": «d.doubleQuote.value»
			«ENDIF»
			«IF d.encoding !== null»
				,"encoding": "«d.encoding»"
			«ENDIF»
			«IF d.header !== null»
				,"header": «d.header.value»
			«ENDIF»
			«IF d.headerRowCount !== 0»
				,"headerRowCount": "«d.headerRowCount»"
			«ENDIF»
			«IF d.lineTerminators !== null»
				,"lineTerminators": "«d.lineTerminators»"
			«ENDIF»
			«IF d.quoteChar !== null»
				,"quoteChar": "«d.quoteChar»"
			«ENDIF»
			«IF d.skipBlankRows !== null»
				,"skipBlankRows": «d.skipBlankRows.value»
			«ENDIF»
			«IF d.skipColumns !== 0»
				,"skipColumns": "«d.skipColumns»"
			«ENDIF»
			«IF d.skipInitialSpace !== null»
				,"skipInitialSpace": «d.skipInitialSpace.value»
			«ENDIF»
			«IF d.skipRows !== 0»
				,"skipRows": "«d.skipRows»"
			«ENDIF»
			«IF d.trim !== null»
				,"trim": «d.trim.value»
			«ENDIF»
		},
	'''
	
	def tableSchema(Mapping m) '''
	{
		"url": "«m.source.source»",
		«IF m.source.dialect !== null»
			«m.source.dialect.dialect()»
		«ENDIF»
		"tableSchema": {
			«m.subjectMap()»,
			"columns": [
				«m.subjectTypeMappings()»«IF ! m.subjectTypeMappings.empty && (m.poMappings.length + m.notUsedReferencables.length > 0)»,«ENDIF»
				«m.columns()»«IF ! m.poMappings.empty && (m.notUsedReferencables.length > 0)»,«ENDIF»
				«m.suppressOutput()»
			] 
		}
	}
	'''
	
	def suppressOutput(Mapping m)'''
		«FOR ref : m.notUsedReferencables SEPARATOR ","»
			{
				"suppressOutput": true,
				"titles": "«ref.valueResolved»"
			}
		«ENDFOR»
	'''
	
	def subjectTypeMappings(Mapping m)'''
		«FOR stm : m.subjectTypeMappings SEPARATOR ","»
			{
				"virtual": true,
				"propertyUrl": "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"valueUrl": "«stm.type.vocabulary.prefix.iri»«stm.type.valueResolved»"
			}
		«ENDFOR»
	'''
	
	def subjectMap(Mapping m) '''"aboutUrl": "«m.subjectIri»"'''
	
	def columns(Mapping m) '''
		«FOR pom : m.poMappings SEPARATOR ","»
			{
				"propertyUrl": "«pom.property.vocabulary.prefix.iri»«pom.property.valueResolved»",
				«pom.term.valueReference»
			}
		«ENDFOR»
	'''
	
	def dispatch valueReference(ReferenceValuedTerm it) '''
		«termMapAnnex»
		"titles": "«reference.valueResolved»"
	'''
	
	def dispatch valueReference(ConstantValuedTerm it) '''
		"virtual": true,
		"valueUrl": "«constant»"
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