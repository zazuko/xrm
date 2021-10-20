package com.zazuko.rdfmapping.dsl.ide;

import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;

import com.zazuko.rdfmapping.dsl.ide.contentassist.RdfMappingIdeProposalProvider;

/**
 * Use this class to register ide components.
 */
public class RdfMappingIdeModule extends AbstractRdfMappingIdeModule {

	public Class<? extends IdeContentProposalProvider> bindIdeContentProposalProvider() {
		debug("binding RdfMappingIdeProposalProvider");
		return RdfMappingIdeProposalProvider.class;
	}

	// in VS-code, this is visible in the tab "OUTPUT" for Xtext Server
	@Deprecated
	// TODO remove this
	public static void debug(String message) {
		System.err.println("############### " + message);
	}
}
