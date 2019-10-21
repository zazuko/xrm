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
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm

class RmlDialectGenerator {

	/*
	 * RML broadens R2RML's scope of application beyond that of relational databases.
	 * For details: http://rml.io/RML_R2RML.html
	 * 
	 * In places where RML and R2RML differ, this generator dispatches to the respective dialect. 
	 */
	final extension RmlDialect dialect;

	new(RmlDialect dialect) {
		this.dialect = dialect;
	}
	
	def generateTurtle(Iterable<Mapping> mappings) {
		mappings.prefixes +
		mappings
			.map[triplesMap]
			.join('\n')
	}
	
	def prefixes(Iterable<Mapping> mappings) '''
		«staticPrefixes»
		«FOR prefixHolder:mappings.prefixesUsed.inDeterministicOrder»
			PREFIX «prefixHolder.prefix.label» <«prefixHolder.prefix.iri»>
		«ENDFOR»
		
	'''
	
	def triplesMap(Mapping m) '''
		<#«m.name»>
			«m.logicalSource»
			
			«m.subjectMap()»«IF ! m.poMappings.empty»;«ENDIF»
			
			«FOR pom : m.poMappings SEPARATOR ";"»
				«pom.predicateObjectMap»
			«ENDFOR»
		.'''
	
	def subjectMap(Mapping m) '''
		rr:subjectMap [
			rr:template "«m.subjectIri»";
			«FOR stm : m.subjectTypeMappings»
				rr:class «stm.type.vocabulary.prefix.label»«stm.type.valueResolved» ;
			«ENDFOR»	
		]'''
	
	def predicateObjectMap(PredicateObjectMapping pom) '''
		rr:predicateObjectMap [
			rr:predicate «pom.property.vocabulary.prefix.label»«pom.property.valueResolved» ;
			rr:objectMap [
				«pom.term.objectTermMap»
			];
		]
	'''
	
	def dispatch objectTermMap(ValuedTerm it) '''
		# TODO: implementation missing for «class.name»
	'''
	
	def dispatch objectTermMap(ReferenceValuedTerm it) '''
		«objectMapReferencePredicate» "«reference.valueResolved»" ;
		«termMapAnnex»
	'''
	
	def dispatch objectTermMap(ConstantValuedTerm it) '''
		rr:constant «toConstantValue» ;
	'''
	
	def dispatch objectTermMap(TemplateValuedTerm it) '''
		rr:template "«toTemplateString»" ;
	'''
	
	def dispatch objectTermMap(LinkedResourceTerm it) '''
		rr:template "«toTemplateString»" ;
	'''
	
	def termMapAnnex(ReferenceValuedTerm it) '''
		«IF languageTag !== null»
			rr:language "«languageTag.name»" ;
		«ELSEIF datatype !== null»
			rr:datatype «datatype.prefix.label»«datatype.valueResolved» ;
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