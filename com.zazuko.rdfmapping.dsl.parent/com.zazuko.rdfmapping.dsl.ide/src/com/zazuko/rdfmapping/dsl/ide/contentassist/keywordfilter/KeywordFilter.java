package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import java.util.Collections;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;

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

	public boolean filter(ReferenceValuedTerm in, Keyword keyword, ContentAssistContext context) {
		Predicate<Keyword> filter = this.referencedValueTermFilter(in);
		return filter.test(keyword);
	}

	private Predicate<Keyword> referencedValueTermFilter(EObject in) {
		OutputType type = modelAccess.outputType(in);
		return new RmlishOutputTypeCompletionProposalPredicate("as", type);
	}

	public boolean filter(TemplateValuedTerm in, Keyword keyword, ContentAssistContext context) {
		Predicate<Keyword> filter = this.referencedValueTermFilter(in);

		// #30 for a subjectIriMapping, don't offer 'Literal'
		EObject container = in.eContainer();
		if (container instanceof Mapping) {
			Mapping mapping = (Mapping) container;
			// make sure we really have subjetIriMapping in our hands
			if (mapping.getSubjectIriMapping() == in) {
				filter = filter.and(new BlacklistedCompletionProposalPredicate("Literal"));
			}
		}

		return filter.test(keyword);
	}

	public boolean filter(SourceGroup in, Keyword keyword, ContentAssistContext context) {
		return this.completeKeywordForSourceDefinition(keyword,
				in.getTypeRef() != null ? in.getTypeRef().getType() : null);
	}

	public boolean filter(LogicalSource in, Keyword keyword, ContentAssistContext context) {
		return this.completeKeywordForSourceDefinition(keyword,
				in.getTypeRef() != null ? in.getTypeRef().getType() : null);
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
