package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Keyword;

import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public class WhitelistedOutputTypeCompletionProposalPredicate implements Predicate<ICompletionProposal> {

	private final String keywordLiteral;
	private final Keyword keyword;
	private final OutputType actualOutputType;
	private final Set<OutputType> expectedOutputTypes;

	public WhitelistedOutputTypeCompletionProposalPredicate(String keywordLiteral, Keyword keyword, Set<OutputType> expectedOutputTypes, OutputType actualOutputType) {
		this.keywordLiteral = keywordLiteral;
		this.keyword = keyword;
		this.expectedOutputTypes = expectedOutputTypes;
		this.actualOutputType = actualOutputType;
	}

	@Override
	public boolean test(ICompletionProposal proposal) {
		if (this.actualOutputType == null) {
			return true;
		}
		// hmm... hiding keywords seems to be nasty.
		// reason: there is no data driven grammar...
		if (this.keywordLiteral.equals(keyword.getValue())) {
			return this.expectedOutputTypes.contains(this.actualOutputType);
		}
		return true;
	}

}
