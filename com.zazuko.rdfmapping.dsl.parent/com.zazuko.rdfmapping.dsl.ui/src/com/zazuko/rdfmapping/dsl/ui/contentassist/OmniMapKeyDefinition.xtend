package com.zazuko.rdfmapping.dsl.ui.contentassist

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty
import java.util.HashSet
import java.util.Set
import org.eclipse.emf.ecore.EObject

class OmniMapKeyDefinition {

	def dispatch Set<String> knownKeys(Void it) {
		return new HashSet();
	}
	
	def dispatch Set<String> knownKeys(EObject it) {
		return new HashSet();
	}

	def dispatch Set<String> knownKeys(RdfClass it) {
		return newLinkedHashSet(
			RdfMappingConstants.OMNIMAP_KEY_LABEL,
			RdfMappingConstants.OMNIMAP_KEY_COMMENT
		);
	}

	def dispatch Set<String> knownKeys(RdfProperty it) {
		return newLinkedHashSet(
			RdfMappingConstants.OMNIMAP_KEY_LABEL,
			RdfMappingConstants.OMNIMAP_KEY_COMMENT
		);
	}
	
	def dispatch Set<String> knownKeys(Datatype it) {
		return newLinkedHashSet(
			RdfMappingConstants.OMNIMAP_KEY_LABEL,
			RdfMappingConstants.OMNIMAP_KEY_COMMENT
		);
	}
}
