package com.zazuko.rdfmapping.fanin.nq.nqfanin.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class RealNqFaninResourceFactoryImpl extends NqFaninResourceFactoryImpl {

	@Override
	public Resource createResource(URI uri) {
		return new RealNqFaninResourceImpl(uri);
	}

}
