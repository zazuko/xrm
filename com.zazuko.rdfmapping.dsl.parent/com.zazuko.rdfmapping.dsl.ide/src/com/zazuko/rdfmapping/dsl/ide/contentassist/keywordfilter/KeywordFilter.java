package com.zazuko.rdfmapping.dsl.ide.contentassist.keywordfilter;

import java.util.Collections;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ide.editor.contentassist.ContentAssistContext;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;

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

}
