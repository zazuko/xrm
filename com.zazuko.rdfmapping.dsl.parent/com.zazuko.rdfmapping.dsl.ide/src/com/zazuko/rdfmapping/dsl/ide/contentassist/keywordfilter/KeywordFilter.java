package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import java.util.Collections;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.GraphMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm;

import jakarta.inject.Inject;

public class KeywordFilter {

	@Inject
	private ModelAccess modelAccess;

	public boolean filter(PredicateObjectMapping in, Keyword keyword, ContentAssistContext context) {
		OutputType type = modelAccess.outputType(in);
		Predicate<Keyword> filter = new RmlishOutputTypeCompletionProposalPredicate("parent-map", type)//
				.and(new WhitelistedEnumTypeCompletionProposalPredicate<OutputType>("multi-reference",
						Collections.singleton(OutputType.CARML), type));
		return filter.test(keyword);
	}

	public boolean filter(ValuedTerm in, Keyword keyword, ContentAssistContext context) {
		Predicate<Keyword> filter = this.valuedTermFilter(in);

		EObject container = in.eContainer();

		if (container instanceof Mapping) {
			Mapping mapping = (Mapping) container;
			
			// make sure we really have subjetMapping in our hands
			if (mapping.getSubjectMapping() == in) {
				
				// #30 for a subjectMapping, don't offer 'Literal'
				filter = filter.and(new BlacklistedCompletionProposalPredicate("Literal"));
			}
		}

		if (container instanceof GraphMapping) {
			// within a graphMapping, 'as' is not a viable option for a ValuedTerm
			filter = filter.and(new BlacklistedCompletionProposalPredicate("as"));
		}

		return filter.test(keyword);
	}
	
	private Predicate<Keyword> valuedTermFilter(EObject in) {
		OutputType type = modelAccess.outputType(in);
		return new RmlishOutputTypeCompletionProposalPredicate("as", type);
	}

	public boolean filter(SourceGroup in, Keyword keyword, ContentAssistContext context) {
		return this.completeKeywordForSourceDefinition(keyword,
				in.getTypeRef() != null ? in.getTypeRef().getType() : null);
	}

	public boolean filter(LogicalSource in, Keyword keyword, ContentAssistContext context) {
		return this.completeKeywordForSourceDefinition(keyword,
				in.getTypeRef() != null ? in.getTypeRef().getType() : null);
	}

	public boolean filter(Mapping in, Keyword keyword, ContentAssistContext context) {
		OutputType outputType = this.modelAccess.outputType(in);
		
		Predicate<Keyword> filter = new RmlishOutputTypeCompletionProposalPredicate("graphs", outputType);
		filter = filter.and(new RmlishOutputTypeCompletionProposalPredicate("from", outputType));
		filter = filter.and(new RmlishOutputTypeCompletionProposalPredicate("constant", outputType));
		
		return filter.test(keyword);
	}

	private boolean completeKeywordForSourceDefinition(Keyword keyword, SourceType type) {
		Predicate<Keyword> filter = //
				new WhitelistedEnumTypeCompletionProposalPredicate<SourceType>("xml-namespace-extension", //
						Collections.singleton(SourceType.XML), //
						type) //
								.and( //
										new WhitelistedEnumTypeCompletionProposalPredicate<SourceType>("dialect",
												Collections.singleton(SourceType.CSV), //
												type)//
								);
		return filter.test(keyword);
	}

}
