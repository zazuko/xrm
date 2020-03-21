package com.zazuko.rdfmapping.fanin.nq.ui;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninRuntimeModule;

// example taken from: org.eclipse.xtext.ui.ecore
public class Activator extends AbstractUIPlugin {

	private static final Logger logger = Logger.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.zazuko.rdfmapping.fanin.nq.ui";

	// The shared instance
	private static Activator plugin;

	private Injector injector;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public Injector getInjector() {
		return injector;
	}

	private void initializeEcoreInjector() {
		injector = Guice.createInjector(
				Modules.override(Modules.override(new NqFaninRuntimeModule()).with(new NqFaninUiModule(plugin)))
						.with(new SharedStateModule()));
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		try {
			initializeEcoreInjector();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		injector = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
