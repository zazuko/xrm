package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import java.util.Collections;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;

public class KeywordFilter {
	
//	@Inject
//	private IQualifiedNameConverter qualifiedNameConverter;
//
//	@Inject
//	private RdfPrefixedNameConverter rdfDslConverter;

	@Inject
	private ModelAccess modelAccess;

//	@Inject
//	private OmniMapKeyProposalGenerator omniMapKeyProposalGenerator;

	public boolean filter(PredicateObjectMapping in, Keyword keyword, ContentAssistContext context) {
		OutputType type = modelAccess.outputType(in);
		Predicate<Keyword> filter = new RmlishOutputTypeCompletionProposalPredicate("parent-map",
				type)//
						.and(new WhitelistedEnumTypeCompletionProposalPredicate<OutputType>("multi-reference",
								Collections.singleton(OutputType.CARML), type));
		return filter.test(keyword);
	}

}
