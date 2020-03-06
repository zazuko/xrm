package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.function.Predicate;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Keyword;

public class BlacklistedCompletionProposalPredicate implements Predicate<ICompletionProposal> {

	private final String keywordLiteral;
	private final Keyword keyword;

	public BlacklistedCompletionProposalPredicate(String keywordLiteral, Keyword keyword) {
		this.keywordLiteral = keywordLiteral;
		this.keyword = keyword;
	}

	@Override
	public boolean test(ICompletionProposal proposal) {
		return !this.keywordLiteral.equals(this.keyword.getValue());
	}

}
