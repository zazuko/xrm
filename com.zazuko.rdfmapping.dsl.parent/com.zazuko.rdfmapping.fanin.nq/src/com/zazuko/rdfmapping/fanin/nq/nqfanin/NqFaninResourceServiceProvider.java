package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.generic.GenericResourceServiceProvider;

public class NqFaninResourceServiceProvider extends GenericResourceServiceProvider {

	@Override
	public boolean canHandle(URI uri) {
		return NqFaninRuntimeModule.NQ_FANIN_FILE_EXTENSION.equals(uri.fileExtension());
	}

}
