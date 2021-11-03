package com.zazuko.rdfmapping.dsl.ide;

import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;

import com.zazuko.rdfmapping.dsl.ide.contentassist.RdfMappingIdeProposalProvider;
import com.zazuko.rdfmapping.dsl.ide.debug.Debugger;

/**
 * Use this class to register ide components.
 */
public class RdfMappingIdeModule extends AbstractRdfMappingIdeModule {

	public Class<? extends IdeContentProposalProvider> bindIdeContentProposalProvider() {
		Debugger.debug("binding RdfMappingIdeProposalProvider");
		return RdfMappingIdeProposalProvider.class;
	}

}
