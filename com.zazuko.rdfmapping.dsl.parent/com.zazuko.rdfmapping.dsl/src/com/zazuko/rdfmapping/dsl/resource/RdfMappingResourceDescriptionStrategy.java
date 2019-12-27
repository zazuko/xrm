package com.zazuko.rdfmapping.dsl.resource;

import javax.inject.Inject;

import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage;

/**
 * Attach user specific data to a ResourceDescription - changes will cause
 * impact on incoming dependencies and e.g. re-generate artifacts
 *
 */
public class RdfMappingResourceDescriptionStrategy extends CustomResourceDescriptionStrategy {

	@Inject
	public void init(RandomUserDataProvider rnd) {
		// LogicalSource always publish impact on any CTRL+S for now
		this.addUserDataProvider(RdfMappingPackage.eINSTANCE.getLogicalSource(), rnd);
	}
}
