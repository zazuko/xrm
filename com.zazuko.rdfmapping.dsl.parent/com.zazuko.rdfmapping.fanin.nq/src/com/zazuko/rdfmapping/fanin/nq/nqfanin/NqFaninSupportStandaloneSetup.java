package com.zazuko.rdfmapping.fanin.nq.nqfanin;

/**
 * Can be used to cause a side effect in workflow. As soon as this bean
 * is instantiated in a workflow, the {@link org.eclipse.xtext.resource.IResourceServiceProvider services}
 * for {@code *.ecore} files are registered. Existing services will not be replaced.
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class NqFaninSupportStandaloneSetup {

	public static void setup() {
		new NqFaninSupportStandaloneSetup();
	}
	
	public NqFaninSupportStandaloneSetup() {
		new NqFaninSupport().createInjectorAndDoEMFRegistration();
	}
	
}
