package com.zazuko.rdfmapping.dsl.generator.csvw

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.IJoinContext
import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.JoinContextManager
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroupDescription
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SubjectTypeMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValue
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import java.text.MessageFormat
import java.util.List
import jakarta.inject.Inject
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm

class CsvwDialectGenerator {

	@Inject
	extension ModelAccess;

	def generateJson(Iterable<Mapping> mappings, CsvwDialectContext ctx) {
		val JoinContextManager jc = new JoinContextManager(",", "");
		val CharSequence template = generateJson(mappings, ctx, jc.newContext);
		val CharSequence result = jc.postProcess(template.toString());
		return result;	
	}
	def generateJson(Iterable<Mapping> mappings, CsvwDialectContext ctx, IJoinContext jc) '''
		{
			"@context": "http://www.w3.org/ns/csvw"«jc.acquireMarker»
			«IF mappings.size == 1»
			«mappings.iterator.next.tableSchema(ctx, jc)»
			«ELSE»
			"tables": [
				«FOR Mapping mapping : mappings»
				{
					«mapping.tableSchema(ctx, jc.newContext)»
				}«jc.acquireMarker»
				«ENDFOR»
			]
			«ENDIF»
		}
	'''
	
	def dialectContent(DialectGroupDescription d, IJoinContext jc) '''
			"delimiter": "«d.delimiter»"«jc.acquireMarker»
			«IF d.commentPrefix !== null»
				"commentPrefix": "«d.commentPrefix»"«jc.acquireMarker»
			«ENDIF»
			«IF d.doubleQuote !== null»
				"doubleQuote": «d.doubleQuote.value»«jc.acquireMarker»
			«ENDIF»
			«IF d.encoding !== null»
				"encoding": "«d.encoding»"«jc.acquireMarker»
			«ENDIF»
			«IF d.header !== null»
				"header": «d.header.value»«jc.acquireMarker»
			«ENDIF»
			«IF d.headerRowCount !== 0»
				"headerRowCount": "«d.headerRowCount»"«jc.acquireMarker»
			«ENDIF»
			«IF d.lineTerminators !== null»
				"lineTerminators": "«d.lineTerminators»"«jc.acquireMarker»
			«ENDIF»
			«IF d.quoteChar !== null»
				"quoteChar": "«d.quoteChar»"«jc.acquireMarker»
			«ENDIF»
			«IF d.skipBlankRows !== null»
				"skipBlankRows": «d.skipBlankRows.value»«jc.acquireMarker»
			«ENDIF»
			«IF d.skipColumns !== 0»
				"skipColumns": "«d.skipColumns»"«jc.acquireMarker»
			«ENDIF»
			«IF d.skipInitialSpace !== null»
				"skipInitialSpace": «d.skipInitialSpace.value»«jc.acquireMarker»
			«ENDIF»
			«IF d.skipRows !== 0»
				"skipRows": "«d.skipRows»"«jc.acquireMarker»
			«ENDIF»
			«IF d.trim !== null»
				"trim": «d.trim.value»«jc.acquireMarker»
			«ENDIF»
	'''
	
	def tableSchema(Mapping it, CsvwDialectContext ctx, IJoinContext jc) '''
		"url": "«source.source»"«jc.acquireMarker»
		«IF source.dialectResolved !== null»
			"dialect": {
				«dialectContent(source.dialectResolved.description, jc.newContext)»
			}«jc.acquireMarker»
		«ENDIF»
		"tableSchema": {
			«tableSchemaContent(ctx, jc.newContext)»
		}«jc.acquireMarker»
	'''
	def tableSchemaContent(Mapping it, CsvwDialectContext ctx, IJoinContext jc) '''
		«subjectMap()»«jc.acquireMarker»
		«IF source.csvNullValue !== null»"null": "«source.csvNullValue»"«jc.acquireMarker»«ENDIF»
		"columns": [
			«tableSchemaColumnsContent(ctx, jc.newContext)»
		]«jc.acquireMarker»
	'''

	def tableSchemaColumnsContent(Mapping it, CsvwDialectContext ctx, IJoinContext jc) '''
		«subjectTypeMappings(jc)»
		«columns(jc)»
		«suppressOutputList(ctx, jc)»
	'''
	
	def suppressOutputList(Mapping it, CsvwDialectContext ctx, IJoinContext jc)'''
		«FOR Referenceable ref : ctx.notUsedReferencables(it)»
			{
				"suppressOutput": true,
				"titles": "«ref.valueResolved»"
			}«jc.acquireMarker»
		«ENDFOR»
	'''
	
	def subjectTypeMappings(Mapping it, IJoinContext jc)'''
		«FOR SubjectTypeMapping stm : subjectTypeMappings»
			{
				"virtual": true,
				"propertyUrl": "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				"valueUrl": "«stm.type.vocabulary.prefix.iri»«stm.type.valueResolved»"
			}«jc.acquireMarker»
			«ENDFOR»
	'''

	def subjectMap(Mapping it) '''"aboutUrl": "«subjectMapping.aboutUrl»"'''
	
	def dispatch aboutUrl(TemplateValuedTerm it) {
		return toTemplateString;
	}
	
	def dispatch aboutUrl(ValuedTerm it) '''
		«class.name» not supported'''
	
	def columns(Mapping it, IJoinContext jc) '''
		«FOR pom : poMappings»
			{
				«columnContent(pom, jc.newContext)»
			}«jc.acquireMarker»
			«ENDFOR»
	'''
	
	def columnContent(PredicateObjectMapping it, IJoinContext jc) '''
			"propertyUrl": "«property.vocabulary.prefix.iri»«property.valueResolved»"«jc.acquireMarker»
			«term.valueReference(jc)»
	'''
	
	def dispatch valueReference(ReferenceValuedTerm it, IJoinContext jc) '''
		«termMapAnnex(jc)»
		"titles": "«reference.valueResolved»"«jc.acquireMarker»
		«IF reference.nullValueMarker !== null»"null": "«reference.nullValueMarker.nullValue»"«jc.acquireMarker»
		«ENDIF»
	'''
	
	def dispatch valueReference(ConstantValuedTerm it, IJoinContext jc) '''
		"virtual": true«jc.acquireMarker»
		"valueUrl": "«constant»"«jc.acquireMarker»
	'''
	
	def dispatch valueReference(TemplateValuedTerm it, IJoinContext jc) '''
		"titles": "«references.toList.get(0).valueResolved»"«jc.acquireMarker»
		"valueUrl": "«toTemplateString»"«jc.acquireMarker»
	'''
	
	def dispatch valueReference(ParentTriplesMapTerm it, IJoinContext jc) '''
		"virtual": true«jc.acquireMarker»
		"valueUrl": "unsupported"«jc.acquireMarker»
	'''
	
	def termMapAnnex(ReferenceValuedTerm it, IJoinContext jc) '''
		«IF languageTag !== null»
			"lang": "«languageTag.name»"«jc.acquireMarker»
		«ELSEIF datatype !== null»
			"datatype": "«datatype.valueResolved»"«jc.acquireMarker»
		«ENDIF»
	'''
	
	def toTemplateString(TemplateValuedTerm it) {		
		template.apply(references);
	}
	
	def apply(TemplateValue template, List<Referenceable> refs) {
		MessageFormat.format(template.templateValueResolved, refs.toMessageFormatArguments());
	}
	
	def toMessageFormatArguments(List<Referenceable> refs) {
		refs.map[ref | '''{«ref.valueResolved»}'''].toArray
	}
	
}