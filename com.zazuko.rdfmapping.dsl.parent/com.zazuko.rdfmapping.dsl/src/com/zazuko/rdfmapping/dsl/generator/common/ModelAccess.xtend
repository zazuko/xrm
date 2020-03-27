package com.zazuko.rdfmapping.dsl.generator.common

import com.zazuko.rdfmapping.dsl.common.rdfMappingCore.RdfClass
import com.zazuko.rdfmapping.dsl.common.rdfMappingCore.RdfProperty
import com.zazuko.rdfmapping.dsl.generator.common.extractors.DialectGroupExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.IsQueryResolvedExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.SourceExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.SourceTypeExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.XmlNamespaceExtensionExtractor
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValueDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValueRef
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqClass
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqProperty
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqVocabulary
import java.net.URL
import java.util.LinkedHashSet
import java.util.List
import java.util.Set
import javax.inject.Inject
import org.eclipse.emf.ecore.EObject

class ModelAccess {
	
	@Inject
	DialectGroupExtractor dialectExtractor;

	@Inject
	IsQueryResolvedExtractor isQueryResolvedExtractor;
	
	@Inject
	SourceExtractor sourceExtractor;

	@Inject
	SourceTypeExtractor sourceTypeExtractor;
	
	@Inject
	XmlNamespaceExtensionExtractor xmlNamespaceExtensionExtractor
	

	def String sourceResolved(LogicalSource it) {
		return sourceExtractor.extractC(it);
	}
	
	def boolean sourceIsQueryResolved(LogicalSource it) {
		return isQueryResolvedExtractor.extractC(it);
	}

	def SourceType typeResolved(LogicalSource it) {
		return sourceTypeExtractor.extractC(it);
	}

	def XmlNamespaceExtension xmlNamespaceExtensionResolved(LogicalSource it) {
		return xmlNamespaceExtensionExtractor.extractC(it);
	}
	
	def DialectGroup dialectResolved(LogicalSource it) {
		return dialectExtractor.extractC(it);
	}

	def DialectGroup dialectResolved(SourceGroup it) {
		return dialectExtractor.extractP(it);
	}
	
	def dispatch VocabularyRef vocabularyRef(RdfClass it) {
		return new VocabularyRef(eContainer as Vocabulary);
	}

	def dispatch VocabularyRef vocabularyRef(NqClass it) {
		return new VocabularyRef(eContainer as NqVocabulary);
	}

	def dispatch String vocabularyPrefixIri(RdfClass it) {
		val Vocabulary voca = eContainer as Vocabulary;
		return voca.prefix.iri;		
	}

	def dispatch String vocabularyPrefixIri(NqClass it) {
		val NqVocabulary voca = eContainer as NqVocabulary;
		return voca.iri;		
	}

//	def Vocabulary vocabulary(RdfProperty it) {
//		return eContainer as Vocabulary;
//	}

	def dispatch VocabularyRef vocabularyRef(RdfProperty it) {
		return new VocabularyRef(eContainer as Vocabulary);
	}
	
	def dispatch VocabularyRef vocabularyRef(NqProperty it) {
		return new VocabularyRef(eContainer as NqVocabulary);
	}

	def String toConstantValue(ConstantValuedTerm it) {
		if (constant.isValidURI()) {
			return '''<«constant»>'''
		} else {
			return '''"«constant»"'''
		}
	}

	def boolean isValidURI(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	def Set<VocabularyRef> prefixesUsed(Mapping it) {
		val Set<VocabularyRef> result = new LinkedHashSet();
		result.addAll(subjectTypeMappings.map[m|m.type.vocabularyRef]);
		result.addAll(poMappings.map[m|m.property.vocabularyRef]);

		for (PredicateObjectMapping poMapping : poMappings) {
			val ValuedTerm term = poMapping.term
			if (term instanceof ReferenceValuedTerm) {
				if (term.datatype !== null) {
					result.add(term.datatype.vocabularyRef)
				}
			}
		}

		return result
	}
	
	def Set<VocabularyRef> prefixesUsed(Iterable<Mapping> mappings) {
		return mappings.map[m|m.prefixesUsed].flatten.toSet;
	}

	def List<VocabularyRef> inDeterministicOrder(Iterable<VocabularyRef> prefixHolders) {
		return prefixHolders.toSet.toList.sortBy[s|s.label];
	}

	def private Vocabulary vocabulary(Datatype it) {
		return eContainer as Vocabulary;
	}

	def dispatch VocabularyRef vocabularyRef(Datatype it) {
		return new VocabularyRef(eContainer as Vocabulary);
	}

	def Prefix prefix(Datatype it) {
		return vocabulary.prefix;
	}

	def String valueResolved(Referenceable it) {
		if (value !== null) {
			return value;
		} else {
			return name;
		}
	}

	def String valueResolved(RdfClass it) {
		if (value !== null) {
			return value;
		} else {
			return name;
		}
	}

	def String valueResolved(RdfProperty it) {
		if (value !== null) {
			return value;
		} else {
			return name;
		}
	}

	def String valueResolved(Datatype it) {
		if (value !== null) {
			return value;
		} else {
			return name;
		}
	}
	
	def String referenceFormulation(SourceTypeRef it) {
		return it?.type?.referenceFormulation;
	}
	
	def String referenceFormulation(SourceType it) {
		return GeneratorConstants.REFERENCE_FORMULATION.toStringValue(it);
	}
	
	def OutputType outputType(EObject it) {
		return findParent(Domainmodel)?.outputType?.type;
	}
	
	def <C extends EObject> C findParent(EObject it, Class<C> clazz) {
		var EObject tmp = it;
		while (tmp !== null) {
			if (clazz.isInstance(tmp)) {
				return clazz.cast(tmp);
			}
			tmp = tmp.eContainer;
		}
		return null;
	}
	
	def dispatch String templateValueResolved(TemplateValueRef it) {
		return templateDeclaration?.value?.templateValue;
	}
	
	def dispatch String templateValueResolved(TemplateValueDeclaration it) {
		return templateValue;
	}

}
