package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.generic.GenericResourceDescriptionManager;
import org.eclipse.xtext.util.IResourceScopeCache;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NqFaninResourceDescriptionManager extends GenericResourceDescriptionManager {

	@Inject
	private IDefaultResourceDescriptionStrategy resourceDescriptionStrategy;

	@Inject
	private IResourceScopeCache cache = new IResourceScopeCache.NullImpl();

	@Override
	public IResourceDescription getResourceDescription(Resource resource) {
		return new NqFaninResourceDescription(resource, resourceDescriptionStrategy, cache);
	}

}
