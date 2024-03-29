/*
 * generated by Xtext 2.25.0
 */
package com.zazuko.rdfmapping.dsl.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider;
import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher;
import org.eclipse.xtext.ui.editor.contentassist.UiToIdeContentProposalProvider;
import org.eclipse.xtext.ui.editor.hover.html.IEObjectHoverDocumentationProvider;

import com.google.inject.Binder;
import com.zazuko.rdfmapping.dsl.ide.contentassist.RdfMappingIdeProposalProvider;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingFactory;
import com.zazuko.rdfmapping.dsl.ui.contentassist.RdfMappingPrefixMatcher;
import com.zazuko.rdfmapping.dsl.ui.labeling.RdfHoverDocumentationProvider;
import com.zazuko.rdfmapping.dsl.ui.labeling.RdfMappingLabelProvider;

/**
 * Use this class to register components to be used within the Eclipse IDE.
 */
public class RdfMappingUiModule extends AbstractRdfMappingUiModule {

	public RdfMappingUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	public Class<? extends LabelProvider> bindLabelProvider() {
		return RdfMappingLabelProvider.class;
	}

	@Override
	public Class<? extends IContentProposalProvider> bindIContentProposalProvider() {
		// *not* return RdfMappingProposalProvider.class;
		return UiToIdeContentProposalProvider.class;
	}
	
	public Class<? extends IdeContentProposalProvider> bindIdeContentProposalProvider() {
		return RdfMappingIdeProposalProvider.class;
	}

	@Override
	public Class<? extends PrefixMatcher> bindPrefixMatcher() {
		return RdfMappingPrefixMatcher.class;
	}

	@Override
	public void configure(Binder binder) {
		super.configure(binder);

		binder.bind(RdfMappingFactory.class).toInstance(RdfMappingFactory.eINSTANCE);
		binder.bind(IEObjectHoverDocumentationProvider.class).to(RdfHoverDocumentationProvider.class);
	}

}
