package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

public class NsURIQualifiedNameProvider extends NqFaninQualifiedNameProvider {

	@Override
	protected String name(EPackage ePackage) {
		return ePackage.getNsURI();
	}

	@Override
	protected String getCacheKey() {
		return "nsURIfqn";
	}
	
	@Override
	protected boolean isRecurseParent(EObject obj) {
		return !(obj instanceof EPackage) && super.isRecurseParent(obj);
	}
}
