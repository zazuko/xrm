package com.zazuko.rdfmapping.fanin.nq.ui;

import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.resource.DefaultResourceUIServiceProvider;

import com.google.inject.Inject;

public class NqFaninResourceUIServiceProvider extends DefaultResourceUIServiceProvider {

	@Inject
	public NqFaninResourceUIServiceProvider(IResourceServiceProvider delegate) {
		super(delegate);
	}

}
