package com.zazuko.rdfmapping.dsl.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;

/**
 * Always generate impact.
 *
 */
public class RandomUserDataProvider implements UserDataProvider {

	@Override
	public Map<String, String> provideUserData(EObject target) {
		Map<String, String> userdata = new HashMap<>();
		userdata.put(this.getClass().getSimpleName(), UUID.randomUUID().toString());
		return userdata;
	}

}
