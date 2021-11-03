package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistEntry;
import org.eclipse.xtext.ide.editor.contentassist.IIdeContentProposalAcceptor;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalCreator;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalPriorities;
import org.eclipse.xtext.ide.editor.contentassist.IdeContentProposalProvider;

import com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter.KeywordFilter;
import com.zazuko.rdfmapping.dsl.ide.contentassist.omnimap.OmniMapKeyProposalGenerator;
import com.zazuko.rdfmapping.dsl.ide.debug.Debugger;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;
import com.zazuko.rdfmapping.dsl.services.RdfMappingGrammarAccess;

public class RdfMappingIdeProposalProvider extends IdeContentProposalProvider {

	@Inject
	private KeywordFilter keywordFilter;
	@Inject
	private RdfMappingGrammarAccess grammarAccess;
	@Inject
	private IdeContentProposalCreator proposalCreator;
	@Inject
	private SequencerAccess sequencer;
	@Inject
	private IdeContentProposalPriorities proposalPriorities;

	@Inject
	private OmniMapKeyProposalGenerator omniMapKeyProposalGenerator;

	public RdfMappingIdeProposalProvider() {
		Debugger.debug("RdfMappingIdeProposalProvider init");
	}

	@Override
	public void createProposals(Collection<ContentAssistContext> contexts, IIdeContentProposalAcceptor acceptor) {
		Debugger.debug("createProposals hook called");
		// set a breakpoint here in order see when this is invoked

		IIdeContentProposalAcceptor delegate = new IIdeContentProposalAcceptor() {

			@Override
			public void accept(ContentAssistEntry entry, int priority) {
				// to sniff proposals coming in - add a (conditional) breakpoint on next line
				acceptor.accept(entry, priority);
			}

			@Override
			public boolean canAcceptMoreProposals() {
				return acceptor.canAcceptMoreProposals();
			}

		};
		super.createProposals(contexts, delegate);
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

	@Override
	protected void _createProposals(RuleCall ruleCall, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
		if (ruleCall.getRule() == this.grammarAccess.getBLOCK_BEGINRule()) {
			ContentAssistEntry proposal = this.proposalCreator.createProposal(this.sequencer.getBLOCK_BEGINToken(),
					context);
			acceptor.accept(proposal, this.proposalPriorities.getDefaultPriority(proposal));

		} else if (ruleCall.getRule() == this.grammarAccess.getBLOCK_ENDRule()) {
			ContentAssistEntry proposal = this.proposalCreator.createProposal(this.sequencer.getBLOCK_ENDToken(),
					context);
			acceptor.accept(proposal, this.proposalPriorities.getDefaultPriority(proposal));

		} else if (ruleCall.getRule() == this.grammarAccess.getLINE_ENDRule()) {
			if (context.getCurrentModel() instanceof PredicateObjectMapping) {
				// deal with PredicateObjectMapping+LINE_END in
				// _createProposals(Assignment assignment, ...
				return;
			}
			ContentAssistEntry proposal = this.proposalCreator.createProposal(this.sequencer.getLINE_ENDToken(),
					context);
			acceptor.accept(proposal, this.proposalPriorities.getDefaultPriority(proposal));
		}

	}

	@Override
	protected void _createProposals(Assignment assignment, ContentAssistContext context,
			IIdeContentProposalAcceptor acceptor) {
		if (context.getCurrentModel() instanceof PredicateObjectMapping
				&& (assignment.getTerminal() instanceof RuleCall)
				&& ((RuleCall) assignment.getTerminal()).getRule() == this.grammarAccess.getLINE_ENDRule()) {
			PredicateObjectMapping pom = (PredicateObjectMapping) context.getCurrentModel();
			if (pom.getTerm() == null || pom.isLineEnd()) {
				return; // do not offer ';' if there is no valuedTerm or there is already a ';'
			} else {
				ContentAssistEntry proposal = this.proposalCreator.createProposal(this.sequencer.getLINE_ENDToken(),
						context);
				acceptor.accept(proposal, this.proposalPriorities.getDefaultPriority(proposal));
			}

		} else if (context.getCurrentModel() instanceof OmniMap && (assignment.getTerminal() instanceof RuleCall)
				&& ((RuleCall) assignment.getTerminal()).getRule() == this.grammarAccess.getOmniMapEntryRule()) {
			this.completeOmniMapEntries((OmniMap) context.getCurrentModel(), context, acceptor);

		} else {
			super._createProposals(assignment, context, acceptor);
		}
	}

	private void completeOmniMapEntries(OmniMap in, ContentAssistContext context, IIdeContentProposalAcceptor acceptor) {
		Set<String> proposalTexts = this.omniMapKeyProposalGenerator.createKeyProposals(in);
		for (String text : proposalTexts) {
			if (!acceptor.canAcceptMoreProposals()) {
				return;
			}
			
			ContentAssistEntry proposal = this.proposalCreator.createProposal(text,
					context);
			acceptor.accept(proposal, this.proposalPriorities.getDefaultPriority(proposal));
		}
	}

}
