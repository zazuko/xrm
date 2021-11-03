package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import java.util.function.Predicate;

import org.eclipse.xtext.Keyword;

public class BlacklistedCompletionProposalPredicate implements Predicate<Keyword> {

	private final String keywordLiteral;

	public BlacklistedCompletionProposalPredicate(String keywordLiteral) {
		this.keywordLiteral = keywordLiteral;
	}

	@Override
	public boolean test(Keyword proposal) {
		return !this.keywordLiteral.equals(proposal.getValue());
	}

}
