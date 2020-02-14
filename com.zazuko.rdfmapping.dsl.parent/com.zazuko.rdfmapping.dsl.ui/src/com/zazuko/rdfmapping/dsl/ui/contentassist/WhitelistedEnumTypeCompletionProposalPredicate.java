package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Keyword;

public class WhitelistedEnumTypeCompletionProposalPredicate<T extends Enumerator> implements Predicate<ICompletionProposal> {

	private final String keywordLiteral;
	private final Keyword keyword;
	private final T actualType;
	private final Set<T> expectedTypes;

	public WhitelistedEnumTypeCompletionProposalPredicate(String keywordLiteral, Keyword keyword, Set<T> expectedTypes, T actualType) {
		this.keywordLiteral = keywordLiteral;
		this.keyword = keyword;
		this.expectedTypes = expectedTypes;
		this.actualType = actualType;
	}

	@Override
	public boolean test(ICompletionProposal proposal) {
		if (this.actualType == null) {
			return true;
		}
		// hmm... hiding keywords seems to be nasty.
		// reason: there is no data driven grammar...
		if (this.keywordLiteral.equals(keyword.getValue())) {
			return this.expectedTypes.contains(this.actualType);
		}
		return true;
	}

}
