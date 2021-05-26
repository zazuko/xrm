package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry;

public class OmniMapKeyProposalGenerator {
	
	@Inject
	private OmniMapKeyDefinition keyDefinition;

	
	public Set<String> createKeyProposals(OmniMap map) {
		if (map == null) {
			return new HashSet<>();
		}
		
		Set<String> result = this.keyDefinition.knownKeys(map.eContainer());
		Set<String> existing = new HashSet<>(map.getEntries().stream()
				.filter(Objects::nonNull)
				.map(OmniMapEntry::getKey)
				.filter(Objects::nonNull)
				.map(String::trim)
				.collect(Collectors.toList())); // not toSet, this will explode on duplicated entries (which we do not care here)
		result.removeAll(existing);
		
		// quote the proposals, so the user does not have to add String quotes manually
		result = new HashSet<>(result.stream()
				.map(proposal -> "\"" + proposal + "\"")
				.collect(Collectors.toList()));
		
		return result;
	}
}
