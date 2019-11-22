package com.zazuko.rdfmapping.dsl.generator.common

import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.DatatypesDefinition
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
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
import java.net.URL
import java.util.LinkedHashSet
import java.util.List
import java.util.Set

class ModelAccess {

	def String sourceResolved(LogicalSource it) {
		if (source !== null) {
			source;
		} else {
			sourceGroup?.source;
		}
	}
	
	def boolean sourceIsQueryResolved(LogicalSource it) {
		if (source !== null) {
			sourceIsQuery;
		} else {
			sourceGroup !== null ? sourceGroup.sourceIsQuery : false;
		}
	}

	def SourceType typeResolved(LogicalSource it) {
		if (typeRef !== null) {
			typeRef.type;
		} else {
			sourceGroup?.typeRef.type;
		}
	}

	def SourceGroup sourceGroup(LogicalSource it) {
		if (eContainer instanceof SourceGroup) {
			return eContainer as SourceGroup;
		} else {
			return null;
		}
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

}
