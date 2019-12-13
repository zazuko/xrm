package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.function.Predicate;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Keyword;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public class RmlishCompletionProposalPredicate implements Predicate<ICompletionProposal> {

	private final String keywordLiteral;
	private final Keyword keyword;
	private final OutputType outputType;

	public RmlishCompletionProposalPredicate(String keywordLiteral, Keyword keyword, OutputType outputType) {
		this.keywordLiteral = keywordLiteral;
		this.keyword = keyword;
		this.outputType = outputType;
	}

	@Override
	public boolean test(ICompletionProposal proposal) {
		if (this.outputType == null) {
			return true;
		}
		// hmm... hiding keywords seems to be nasty.
		// reason: there is no data driven grammar...
		if (this.keywordLiteral.equals(keyword.getValue())) {
			return RdfMappingConstants.RMLISH_OUTPUTTYPES.contains(this.outputType);
		}
		return true;
	}

}
