package com.zazuko.rdfmapping.dsl.ide;

import org.eclipse.xtext.util.Modules2;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zazuko.rdfmapping.dsl.RdfMappingRuntimeModule;
import com.zazuko.rdfmapping.dsl.RdfMappingStandaloneSetup;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class RdfMappingIdeSetup extends RdfMappingStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new RdfMappingRuntimeModule(), new RdfMappingIdeModule()));
	}

}
