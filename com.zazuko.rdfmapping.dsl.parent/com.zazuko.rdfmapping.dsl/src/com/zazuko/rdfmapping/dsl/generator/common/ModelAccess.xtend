package com.zazuko.rdfmapping.dsl.generator.common

import com.zazuko.rdfmapping.dsl.generator.common.extractors.DialectGroupExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.IsQueryResolvedExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.SourceExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.SourceTypeExtractor
import com.zazuko.rdfmapping.dsl.generator.common.extractors.XmlNamespaceExtensionExtractor
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.DatatypesDefinition
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import com.zazuko.rdfmapping.dsl.rdfMapping.PrefixHolder
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
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

	def Vocabulary vocabulary(RdfClass it) {
		return eContainer as Vocabulary;
	}

	def Vocabulary vocabulary(RdfProperty it) {
		return eContainer as Vocabulary;
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

	def Set<PrefixHolder> prefixesUsed(Mapping it) {
		val Set<PrefixHolder> result = new LinkedHashSet();
		result.addAll(subjectTypeMappings.map[m|m.type.vocabulary]);
		result.addAll(poMappings.map[m|m.property.vocabulary]);

		for (PredicateObjectMapping poMapping : poMappings) {
			val ValuedTerm term = poMapping.term
			if (term instanceof ReferenceValuedTerm) {
				if (term.datatype !== null) {
					result.add(term.datatype.datatypesDefinition)
				}
			}
		}

		return result
	}
	
	def Iterable<Prefix> prefixesUsed(XmlNamespaceExtension ext, Mapping mapping) {
		val Set<String> usedPrefixLabelsInMapping = mapping.prefixesUsed()
			.map[h | h.prefix.label]
			.map[label | label.replaceAll("\\:", "")] // TODO see proposal #73 (unify Prefix.label)
			.toSet;
		val Set<Prefix> result = new LinkedHashSet;
		for (Prefix candidate : ext.prefixes) {
			if (usedPrefixLabelsInMapping.contains(candidate.label)) {
				result.add(candidate);
			}
		}
		return result;
	}

	def Set<PrefixHolder> prefixesUsed(Iterable<Mapping> mappings) {
		return mappings.map[m|m.prefixesUsed].flatten.toSet;
	}

	def List<PrefixHolder> inDeterministicOrder(Iterable<PrefixHolder> prefixHolders) {
		return prefixHolders.toSet.toList.sortBy[s|s.prefix.label];
	}

	def DatatypesDefinition datatypesDefinition(Datatype it) {
		return eContainer as DatatypesDefinition;
	}

	def Prefix prefix(Datatype it) {
		return datatypesDefinition.prefix;
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

}
