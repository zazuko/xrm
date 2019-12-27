package com.zazuko.rdfmapping.dsl.resource;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

public interface UserDataProvider {

	/**
	 * Do not navigate over references within this method
	 * 
	 * @param target
	 * @return userData
	 */
	public Map<String, String> provideUserData(EObject target);

}
