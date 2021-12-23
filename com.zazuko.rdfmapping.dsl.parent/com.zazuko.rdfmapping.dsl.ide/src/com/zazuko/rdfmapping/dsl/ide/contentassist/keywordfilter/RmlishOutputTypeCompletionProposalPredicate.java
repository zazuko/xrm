package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;

public class RmlishOutputTypeCompletionProposalPredicate
		extends WhitelistedEnumTypeCompletionProposalPredicate<OutputType> {

	public RmlishOutputTypeCompletionProposalPredicate(String keywordLiteral, OutputType actualOutputType) {
		super(keywordLiteral, RdfMappingConstants.RMLISH_OUTPUTTYPES, actualOutputType);
	}

}
