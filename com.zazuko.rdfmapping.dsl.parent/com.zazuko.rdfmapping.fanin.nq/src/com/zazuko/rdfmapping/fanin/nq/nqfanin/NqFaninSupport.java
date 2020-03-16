package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceSupport;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 
 * @see org.eclipse.xtext.ecore.EcoreSupport
 * {@linkplain https://www.eclipse.org/forums/index.php?t=msg&th=215999/}
 *
 */
public class NqFaninSupport extends AbstractGenericResourceSupport implements ISetup {

	@Override
	protected Module createGuiceModule() {
		return new NqFaninRuntimeModule();
	}

	/**
	 * @since 2.5
	 */
	@Override
	public Injector createInjectorAndDoEMFRegistration() {
		Injector injector = Guice.createInjector(getGuiceModule());
		injector.injectMembers(this);
		registerInRegistry(false);
		return injector;
	}

}