package com.zazuko.rdfmapping.dsl.generator.rml

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.IJoinContext
import com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner.JoinContextManager
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.GraphMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.LanguageTag
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValue
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.TermTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import jakarta.inject.Inject
import java.text.MessageFormat
import java.util.List

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
	
	def CharSequence generateTurtle(Iterable<Mapping> mappings) {
		val JoinContextManager jc = new JoinContextManager(";", "");
		val CharSequence template = generateTurtle(mappings, jc);
		val CharSequence result = jc.postProcess(template.toString());
		return result;	
	}
	
	def generateTurtle(Iterable<Mapping> mappings, JoinContextManager jc) '''
		«prefixes(mappings)»
		«FOR Mapping current: mappings»
		«triplesMap(current, jc.newContext(";", "."))»
		«ENDFOR»
	'''
	
	def prefixes(Iterable<Mapping> mappings) '''
		«staticPrefixes»
		«FOR prefixHolder:mappings.prefixesUsed.inDeterministicOrder»
			PREFIX «prefixHolder.prefix.label»: <«prefixHolder.prefix.iri»>
		«ENDFOR»
		
	'''
	
	def triplesMap(Mapping it, IJoinContext jc) '''
		<«localId»>
			a rr:TriplesMap«jc.acquireMarker»
			
			«logicalSource(jc.newContext)»«jc.acquireMarker»
			
			«subjectMap(jc.newContext)»«jc.acquireMarker»
			
			«FOR pom : poMappings»
				«pom.predicateObjectMap(jc.newContext)»«jc.acquireMarker»
				
			«ENDFOR»
		'''
	
	def subjectMap(Mapping it, IJoinContext jc) '''
		rr:subjectMap [
			«subjectMapping.termMap(jc)»
			«FOR stm : subjectTypeMappings»
				rr:class «stm.type.vocabulary.prefix.label»:«stm.type.valueResolved»«jc.acquireMarker»
			«ENDFOR»
			«FOR graphMapping : graphMappings»
				«graphMap(graphMapping, jc.newContext)»«jc.acquireMarker»
			«ENDFOR»
		]'''
	
	def graphMap(GraphMapping it, IJoinContext jc) {
		if (template !== null) {
			return graphMap(template, jc);
		} else if (constant !== null) {
			return graphMap(constant, jc);
		} else if (reference !== null) {
			return graphMap(reference, jc);
		} 
		return "";
	}
	
	def graphMap(TemplateValuedTerm it, IJoinContext jc) '''
		rr:graphMap [
		  rr:template "«toTemplateString»"«jc.acquireMarker»
		]'''
	
	
	def graphMap(ConstantValuedTerm it, IJoinContext jc) '''
		rr:graphMap [
		  rr:constant «toConstantValue»«jc.acquireMarker»
		]'''
		
	def graphMap(ReferenceValuedTerm it, IJoinContext jc) '''
		rr:graphMap [
		  «termMapReferencePredicate» "«reference.valueResolved»"«jc.acquireMarker»
		]'''
	
	def predicateObjectMap(PredicateObjectMapping it, IJoinContext jc) '''
		rr:predicateObjectMap [
			rr:predicate «property.vocabulary.prefix.label»:«property.valueResolved»«jc.acquireMarker»
			rr:objectMap [
				«term.termMap(jc.newContext)»
			]«jc.acquireMarker»
		]'''
	
	def dispatch termMap(ValuedTerm it, IJoinContext jc) '''
		# TODO: implementation missing for «class.name»
	'''
	
	def dispatch termMap(ReferenceValuedTerm it, IJoinContext jc) '''
		«termMapReferencePredicate» "«reference.valueResolved»"«jc.acquireMarker»
		«termMapAnnex(jc)»
	'''
	
	def dispatch termMap(MultiReferenceValuedTerm it, IJoinContext jc) '''
		«objectMapMultiReferencePredicate» "«reference.valueResolved»"«jc.acquireMarker»
		«termMapAnnex(jc)»
	'''
	
	def dispatch termMap(ConstantValuedTerm it, IJoinContext jc) '''
		rr:constant «toConstantValue»«jc.acquireMarker»
	'''
	
	def String toConstantValue(ConstantValuedTerm it) {
		if(constant !== null) {
			if (constant.isValidURI()) {
				return '''<«constant»>'''
			} else {
				return '''"«constant»"'''
			}
		} else if (constantVocabularyElement !== null){
			return '''«constantVocabularyElement.vocabulary.prefix.label»:«constantVocabularyElement.valueResolved»''' 
		}
	}
	
	def dispatch termMap(TemplateValuedTerm it, IJoinContext jc) '''
		rr:template "«toTemplateString»"«jc.acquireMarker»
		«IF termTypeRef?.type !== null»
			rr:termType rr:«termTypeRef.type»«jc.acquireMarker»
		«ENDIF»
	'''
	
	def dispatch termMap(ParentTriplesMapTerm it, IJoinContext jc) '''
		rr:parentTriplesMap  <«mapping.localId»>«jc.acquireMarker»
		«FOR join : joinConditions»
			rr:joinCondition [
				rr:child "«join.child.valueResolved»"«jc.acquireMarker»
				rr:parent "«join.parent.valueResolved»"«jc.acquireMarker»
			]«jc.acquireMarker»
		«ENDFOR»
	'''
	
	def termMapAnnex(ReferenceValuedTerm it, IJoinContext jc) {
		termMapAnnex(languageTag, datatype, termTypeRef, jc);
	}
	
	def termMapAnnex(MultiReferenceValuedTerm it, IJoinContext jc) {
		termMapAnnex(languageTag, datatype, termTypeRef, jc);
	}
	
	def termMapAnnex(LanguageTag languageTag, Datatype datatype, TermTypeRef termTypeRef, IJoinContext jc) '''
		«IF languageTag !== null»
			rr:language "«languageTag.name»"«jc.acquireMarker»
		«ELSEIF datatype !== null»
			rr:datatype «datatype.prefix.label»:«datatype.valueResolved»«jc.acquireMarker»
		«ENDIF»
		«IF termTypeRef?.type !== null»
			rr:termType rr:«termTypeRef.type»«jc.acquireMarker»
		«ENDIF»
	'''
	
	def private localId(Mapping it) '''#«name»'''
	
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