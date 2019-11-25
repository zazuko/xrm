package com.zazuko.rdfmapping.dsl.generator.csvw

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.LinkedResourceTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SubjectTypeMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import java.text.MessageFormat
import java.util.List
import javax.inject.Inject

class CsvwDialectGenerator {

	@Inject
	extension ModelAccess;

	@Inject
	extension CsvwDialect;

	def generateJson(Iterable<Mapping> mappings, CsvwDialectContext ctx) '''
		{
			«context()»
			"tables": [
				«FOR Mapping mapping : mappings SEPARATOR jsonListSeparator»
				«mapping.tableSchema(ctx)»
				«ENDFOR»
			]
		}
	'''
	
	// includes newLine()
	def jsonListSeparator() '''
	,
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
	
	def tableSchema(Mapping it, CsvwDialectContext ctx) '''
	{
		"url": "«source.source»",
		«IF source.dialect !== null»
			«source.dialect.dialect()»
		«ENDIF»
		"tableSchema": {
			«subjectMap()»,
			"columns": [
				«subjectTypeMappings()»«IF ctx.needsColumnsGlueing(it)»,«ENDIF»
				«columns()»«IF ctx.needsColumnsGlueing(it)»,«ENDIF»
				«suppressOutput(ctx)»
			] 
		}
	}
	'''
	
	def suppressOutput(Mapping it, CsvwDialectContext ctx)'''
		«FOR Referenceable ref : ctx.notUsedReferencables(it) SEPARATOR jsonListSeparator»
			{
				"suppressOutput": true,
				"titles": "«ref.valueResolved»"
			}«ENDFOR»
	'''
	
	def subjectTypeMappings(Mapping it)'''
		«FOR SubjectTypeMapping stm : subjectTypeMappings SEPARATOR jsonListSeparator»
			{
				"virtual": true,
				"propertyUrl": "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"valueUrl": "«stm.type.vocabulary.prefix.iri»«stm.type.valueResolved»"
			}«ENDFOR»
	'''
	
	def subjectMap(Mapping it) '''"aboutUrl": "«subjectIri»"'''
	
	def columns(Mapping it) '''
		«FOR pom : poMappings SEPARATOR jsonListSeparator»
			{
				"propertyUrl": "«pom.property.vocabulary.prefix.iri»«pom.property.valueResolved»",
				«pom.term.valueReference»
			}«ENDFOR»
	'''
	
	def dispatch valueReference(ReferenceValuedTerm it) '''
		«termMapAnnex»
		"titles": "«reference.valueResolved»"
		«IF reference.nullValueMarker !== null»"null": "«reference.nullValueMarker.nullValue»"
		«ENDIF»
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
	
	def dispatch valueReference(ParentTriplesMapTerm it) '''
		"virtual": true,
		"valueUrl": "unsupported"
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