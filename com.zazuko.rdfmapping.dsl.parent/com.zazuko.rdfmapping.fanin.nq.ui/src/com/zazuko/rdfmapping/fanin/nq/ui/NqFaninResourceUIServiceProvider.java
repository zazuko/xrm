package com.zazuko.rdfmapping.fanin.nq.ui;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.resource.DefaultResourceUIServiceProvider;

import com.google.inject.Inject;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninRuntimeModule;

public class NqFaninResourceUIServiceProvider extends DefaultResourceUIServiceProvider {

	@Inject
	public NqFaninResourceUIServiceProvider(IResourceServiceProvider delegate) {
		super(delegate);
	}
	
	@Override
	public boolean canHandle(URI uri) {
		return NqFaninRuntimeModule.NQ_FANIN_FILE_EXTENSION.equals(uri.fileExtension());
	}

	@Override
	public boolean canHandle(URI uri, IStorage storage) {
		return this.canHandle(uri);
	}
	
	
}
