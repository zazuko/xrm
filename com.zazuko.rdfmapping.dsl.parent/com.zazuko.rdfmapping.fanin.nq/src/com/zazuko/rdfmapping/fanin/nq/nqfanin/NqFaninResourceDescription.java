package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescription;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.util.IResourceScopeCache;

public class NqFaninResourceDescription extends DefaultResourceDescription {
	
	private final static Logger log = Logger.getLogger(NqFaninResourceDescription.class);
	
	private IDefaultResourceDescriptionStrategy strategy;

	public NqFaninResourceDescription(Resource resource, IDefaultResourceDescriptionStrategy strategy,
			IResourceScopeCache cache) {
		super(resource, strategy, cache);
		this.strategy = strategy;
	}

	@Override
	protected List<IEObjectDescription> computeExportedObjects() {
		if (!getResource().isLoaded()) {
			try {
				getResource().load(null);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				return Collections.<IEObjectDescription> emptyList();
			}
		}
		final List<IEObjectDescription> exportedEObjects = newArrayList();
		IAcceptor<IEObjectDescription> acceptor = new IAcceptor<IEObjectDescription>() {
			@Override
			public void accept(IEObjectDescription eObjectDescription) {
				exportedEObjects.add(eObjectDescription);
			}
		};
		
		TreeIterator<EObject> allEObjects = getResource().getAllContents();
		while (allEObjects.hasNext()) {
			EObject content = allEObjects.next();
			// this is where the objects really get published on the index
			strategy.createEObjectDescriptions(content, acceptor);
		}
		return exportedEObjects;
	}
}
