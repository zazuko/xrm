package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.LinkedResourceTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import java.text.MessageFormat
import java.util.List
import javax.inject.Inject

class RmlDialectGenerator {

	/*
	 * RML broadens R2RML's scope of application beyond that of relational databases.
	 * For details: http://rml.io/RML_R2RML.html
	 * 
	 * In places where RML and R2RML differ, this generator dispatches to the respective dialect. 
	 */
	
	extension IRmlDialect dialect;
	
	@Inject
	extension ModelAccess

	new(IRmlDialect dialect) {
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
	
	def triplesMap(Mapping it) '''
		<«localId»>
			«logicalSource»
			
			«subjectMap()»«IF ! poMappings.empty»;«ENDIF»
			
			«FOR pom : poMappings SEPARATOR ";"»
				«pom.predicateObjectMap»
			«ENDFOR»
		.'''
	
	def subjectMap(Mapping it) '''
		rr:subjectMap [
			rr:template "«subjectIri»";
			«FOR stm : subjectTypeMappings»
				rr:class «stm.type.vocabulary.prefix.label»«stm.type.valueResolved» ;
			«ENDFOR»
			«IF getSubjectIriMapping.termTypeRef?.type !== null»
				rr:termType rr:«getSubjectIriMapping.termTypeRef.type» ;
			«ENDIF»
		]'''
	
	def predicateObjectMap(PredicateObjectMapping it) '''
		rr:predicateObjectMap [
			rr:predicate «property.vocabulary.prefix.label»«property.valueResolved» ;
			rr:objectMap [
				«term.objectTermMap»
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
		«IF termTypeRef?.type !== null»
			rr:termType rr:«termTypeRef.type» ;
		«ENDIF»
	'''
	
	def dispatch objectTermMap(LinkedResourceTerm it) '''
		rr:template "«toTemplateString»" ;
	'''
	
	def dispatch objectTermMap(ParentTriplesMapTerm it) '''
		rr:parentTriplesMap  <«mapping.localId»> ;
	'''
	
	def termMapAnnex(ReferenceValuedTerm it) '''
		«IF languageTag !== null»
			rr:language "«languageTag.name»" ;
		«ELSEIF datatype !== null»
			rr:datatype «datatype.prefix.label»«datatype.valueResolved» ;
		«ENDIF»
		«IF termTypeRef?.type !== null»
			rr:termType rr:«termTypeRef.type» ;
		«ENDIF»
	'''
	
	def private localId(Mapping it) '''#«name»'''
	
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