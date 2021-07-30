package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;

import com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter.KeywordFilter;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;

public class RdfMappingIdeProposalProvider extends IdeContentProposalProvider {

	@Inject
	private KeywordFilter keywordFilter;

	public RdfMappingIdeProposalProvider() {
		debug("RdfMappingIdeProposalProvider init");
	}

	@Override
	public void createProposals(Collection<ContentAssistContext> contexts, IIdeContentProposalAcceptor acceptor) {
		debug("createProposals hook called");
		// set a breakpoint here in order see when this is invoked
		super.createProposals(contexts, acceptor);
	}

	// in VS-code, this is visible in the tab "OUTPUT" for Xtext Server
	@Deprecated
	// TODO remove this
	public static void debug(String message) {
		System.err.println("############### " + message);
	}

	@Override
	protected boolean filterKeyword(Keyword keyword, ContentAssistContext context) {
		if (context.getCurrentModel() instanceof PredicateObjectMapping) {
			return this.keywordFilter.filter((PredicateObjectMapping) context.getCurrentModel(), keyword, context);
			
		} else if (context.getCurrentModel() instanceof ReferenceValuedTerm) {
			return this.keywordFilter.filter((ReferenceValuedTerm) context.getCurrentModel(), keyword, context);
			
		} else if (context.getCurrentModel() instanceof TemplateValuedTerm) {
			return this.keywordFilter.filter((TemplateValuedTerm) context.getCurrentModel(), keyword, context);
			
		} else if (context.getCurrentModel() instanceof SourceGroup) {
			return this.keywordFilter.filter((SourceGroup) context.getCurrentModel(), keyword, context);
			
		} else if (context.getCurrentModel() instanceof LogicalSource) {
			return this.keywordFilter.filter((LogicalSource) context.getCurrentModel(), keyword, context);
		}
		return true;
	}
}
