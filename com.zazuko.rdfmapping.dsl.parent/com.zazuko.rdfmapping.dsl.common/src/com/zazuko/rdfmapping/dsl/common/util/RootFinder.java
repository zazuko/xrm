package com.zazuko.rdfmapping.dsl.common.util;

import org.eclipse.emf.ecore.EObject;

public class RootFinder {

	@SuppressWarnings("unchecked")
	public <E extends EObject> E findRoot(EObject candidate, Class<E> clazz) {
		if (candidate == null) {
			return null;
		}
		if (clazz.isInstance(candidate)) {
			return (E) candidate;
		}
		return this.findRoot(candidate.eContainer(), clazz);
	}
}
