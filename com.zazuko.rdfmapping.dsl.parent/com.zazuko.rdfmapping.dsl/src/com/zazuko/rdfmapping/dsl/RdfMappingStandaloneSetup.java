package com.zazuko.rdfmapping.dsl;

/**
 * Initialization support for running Xtext languages without Equinox extension
 * registry.
 */
public class RdfMappingStandaloneSetup extends RdfMappingStandaloneSetupGenerated {

	public static void doSetup() {
		new RdfMappingStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
