package com.zazuko.rdfmapping.fanin.nq.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.resource.generic.EmfUiModule;

public class NqFaninUiModule extends EmfUiModule {

	public NqFaninUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void configureLanguageSpecificURIEditorOpener(com.google.inject.Binder binder) {
		super.configureLanguageSpecificURIEditorOpener(binder);
		// binder.bind(IURIEditorOpener.class).annotatedWith(LanguageSpecific.class).to(NqFaninEditorOpener.class);
	}
	
}
