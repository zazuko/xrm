package com.zazuko.rdfmapping.dsl.formatting2.smuggler;

import org.eclipse.emf.ecore.EObject;

public abstract class AbstractSmuggler<E extends EObject> {

	private final E _target;

	public AbstractSmuggler(E target) {
		super();
		this._target = target;
	}

	public E getTarget() {
		return _target;
	}

}
