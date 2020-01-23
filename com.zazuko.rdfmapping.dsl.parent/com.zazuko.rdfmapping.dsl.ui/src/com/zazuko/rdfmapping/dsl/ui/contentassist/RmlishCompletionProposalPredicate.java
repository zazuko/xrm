package com.zazuko.rdfmapping.dsl.ui.contentassist;

import org.eclipse.xtext.Keyword;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public class RmlishCompletionProposalPredicate extends WhitelistedOutputTypeCompletionProposalPredicate {

	public RmlishCompletionProposalPredicate(String keywordLiteral, Keyword keyword, OutputType actualOutputType) {
		super(keywordLiteral, keyword, RdfMappingConstants.RMLISH_OUTPUTTYPES, actualOutputType);
	}

}
