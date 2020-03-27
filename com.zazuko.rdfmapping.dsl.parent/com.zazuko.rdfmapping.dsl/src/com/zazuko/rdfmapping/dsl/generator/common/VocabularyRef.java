package com.zazuko.rdfmapping.dsl.generator.common;

import org.eclipse.emf.ecore.EObject;

import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqVocabulary;

public class VocabularyRef {

	private final String label;
	private final String iri;
	
	private final EObject trace;
	
	public VocabularyRef(Vocabulary in) {
		this.label = in.getPrefix().getLabel();
		this.iri = in.getPrefix().getIri();
		this.trace = in;
	}

	public VocabularyRef(NqVocabulary in) {
		this.label = in.getLabel();
		this.iri = in.getIri();
		this.trace = in;
	}

	public String getLabel() {
		return label;
	}

	public String getIri() {
		return iri;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trace == null) ? 0 : trace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VocabularyRef other = (VocabularyRef) obj;
		if (trace == null) {
			if (other.trace != null)
				return false;
		} else if (!trace.equals(other.trace))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VocabularyRef [label=" + label + ", iri=" + iri + ", trace=" + trace + "]";
	}
	
	
	
}
