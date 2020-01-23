package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.function.Predicate;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

public class FilteringCompletionProposalAcceptor implements ICompletionProposalAcceptor {

	private final ICompletionProposalAcceptor delegate;
	private final Predicate<ICompletionProposal> filter;
	
	public FilteringCompletionProposalAcceptor(ICompletionProposalAcceptor delegate,
			Predicate<ICompletionProposal> filter) {
		this.delegate = delegate;
		this.filter = filter;
	}

	@Override
	public void accept(ICompletionProposal proposal) {
		if (this.filter.test(proposal)) {
			this.delegate.accept(proposal);
		}
	}

	@Override
	public boolean canAcceptMoreProposals() {
		return this.delegate.canAcceptMoreProposals();
	}

}
