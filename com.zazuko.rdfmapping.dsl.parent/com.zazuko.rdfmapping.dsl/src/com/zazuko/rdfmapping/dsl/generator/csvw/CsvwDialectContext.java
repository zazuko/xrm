package com.zazuko.rdfmapping.dsl.generator.csvw;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable;
import com.zazuko.rdfmapping.dsl.util.LazyMap;

public class CsvwDialectContext {

	private final ModelAccess access;

	private final LazyMap<Mapping, Set<Referenceable>> unusedReferenceables;

	public CsvwDialectContext(ModelAccess access, Collection<Mapping> mappings) {
		this.access = access;
		this.unusedReferenceables = buildUnusedReferenceables(mappings);
	}

	public Set<Referenceable> notUsedReferencables(Mapping mapping) {
		return this.unusedReferenceables.getOrInit(mapping);
	}

	private LazyMap<Mapping, Set<Referenceable>> buildUnusedReferenceables(Collection<Mapping> mappings) {
		LazyMap<Mapping, Set<Referenceable>> result = new LazyMap<>(new HashMap<>(), LinkedHashSet::new);  // order matters, see issue_35

		for (Mapping mapping : mappings) {
			
			Set<String> referencedValues = new HashSet<>();
			for (PredicateObjectMapping poMapping : mapping.getPoMappings()) {
				if (poMapping.getTerm() instanceof ReferenceValuedTerm) {
					ReferenceValuedTerm cast = (ReferenceValuedTerm) poMapping.getTerm();
					referencedValues.add(this.access.valueResolved(cast.getReference()));
				}
			}
			
			for (Referenceable refToCheck : mapping.getSource().getReferenceables()) {
				if (!referencedValues.contains(this.access.valueResolved(refToCheck))) {
					result.getOrInit(mapping).add(refToCheck);
				}
			}
		}
		return result;
	}

}
