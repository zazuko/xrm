package com.zazuko.rdfmapping.dsl.generator.common

import com.zazuko.rdfmapping.dsl.generator.common.extractors.CsvNullValueExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.DialectGroupExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.IsQueryResolvedExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.SourceExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.XmlNamespaceExtensionExtractor
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.Element
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.NullValueDeclaration
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
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.TermTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary
import com.zazuko.rdfmapping.dsl.rdfMapping.VocabularyElement
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
import jakarta.inject.Inject
import java.net.URL
import java.util.LinkedHashSet
import java.util.List
import java.util.Set
import org.eclipse.emf.ecore.EObject

class ModelAccess {

	@Inject
	DialectGroupExtractor dialectExtractor;

	@Inject
	IsQueryResolvedExtractor isQueryResolvedExtractor;

	@Inject
	CsvNullValueExtractor csvNullValueExtractor;

	@Inject
	SourceExtractor sourceExtractor;

	@Inject
	XmlNamespaceExtensionExtractor xmlNamespaceExtensionExtractor

	def String sourceResolved(LogicalSource it) {
		return sourceExtractor.extractC(it);
	}

	def boolean sourceIsQueryResolved(LogicalSource it) {
		return isQueryResolvedExtractor.extractC(it);
	}

	// we are in an editor runtime - so also cover the null case :-(
	def dispatch SourceType typeResolved(Void it) {
		return null;
	}

	// we can cope with null as an answer - this is better than swallowed exceptions like text.validation.CompositeEValidator  - Error executing EValidator
	def dispatch SourceType typeResolved(EObject it) {
		return null;
	}

	def dispatch SourceType typeResolved(LogicalSource it) {
		if (typeRef !== null && typeRef?.type !== null) {
			return typeRef.type;
		}
		return typeResolved(eContainer);
	}

	def dispatch SourceType typeResolved(SourceGroup it) {
		if (typeRef?.type !== null) {
			return typeRef.type;
		}
		return null;
	}

	def dispatch SourceType typeResolved(Referenceable it) {
		return eContainer.typeResolved;
	}

	def dispatch SourceType typeResolved(NullValueDeclaration it) {
		return eContainer.typeResolved;
	}

	def dispatch SourceType typeResolved(Element it) {
		return null; // last resort
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

	def Vocabulary vocabulary(VocabularyElement it) {
		return eContainer as Vocabulary;
	}

	def boolean isValidURI(String url) {
		try {
			new URL(url).toURI();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	def Set<Vocabulary> prefixesUsed(Mapping it) {
		val Set<Vocabulary> result = new LinkedHashSet();
		result.addAll(subjectTypeMappings.map[m|m.type.vocabulary]);
		result.addAll(poMappings.map[m|m.property.vocabulary]);
		
		result.addAll(graphMappings.map[m|m.constant?.constantVocabularyElement?.vocabulary]);
		result.removeIf(e|e === null);

		for (PredicateObjectMapping poMapping : poMappings) {
			val ValuedTerm term = poMapping.term
			if (term instanceof ReferenceValuedTerm) {
				if (term.datatype !== null) {
					result.add(term.datatype.vocabulary)
				}
			}
			if (term instanceof ConstantValuedTerm) {
				if (term.constantVocabularyElement !== null) {
					result.add(term.constantVocabularyElement.vocabulary)
				}
			}
		}

		return result
	}

	def Set<Vocabulary> prefixesUsed(Iterable<Mapping> mappings) {
		return mappings.map[m|m.prefixesUsed].flatten.toSet;
	}

	def List<Vocabulary> inDeterministicOrder(Iterable<Vocabulary> prefixHolders) {
		return prefixHolders.toSet.toList.sortBy[s|s.prefix.label];
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

	def String valueResolved(VocabularyElement it) {
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

	def String csvNullValue(LogicalSource it) {
		return csvNullValueExtractor.extractC(it);
	}
	
	def TermTypeRef termTypeRef0(ValuedTerm it) {
		switch it {
			ReferenceValuedTerm : termTypeRef
			MultiReferenceValuedTerm : termTypeRef
			TemplateValuedTerm : termTypeRef
		    default : null
  		}
	}

}
