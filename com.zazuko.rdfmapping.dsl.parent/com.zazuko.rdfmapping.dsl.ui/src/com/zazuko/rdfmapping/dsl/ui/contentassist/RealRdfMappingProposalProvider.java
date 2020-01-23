package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Collections;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;
import com.zazuko.rdfmapping.dsl.services.RdfPrefixedNameConverter;

public class RealRdfMappingProposalProvider extends AbstractRdfMappingProposalProvider {

	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;

	@Inject
	private RdfPrefixedNameConverter rdfDslConverter;

	@Inject
	private ModelAccess modelAccess;

	protected StyledString getStyledDisplayString(IEObjectDescription description) {
		EObject eo = description.getEObjectOrProxy();
		if (eo instanceof RdfProperty //
				|| eo instanceof RdfClass //
		) {
			String origQualifiedNameString = this.qualifiedNameConverter.toString(description.getQualifiedName());
			String qualifiedNameString = this.rdfDslConverter.toString(origQualifiedNameString);
			return getStyledDisplayString(eo, qualifiedNameString,
					qualifiedNameConverter.toString(description.getName()));
		}
		return super.getStyledDisplayString(description);
	}

	class RdfProposalCreator extends DefaultProposalCreator {

		public RdfProposalCreator(ContentAssistContext contentAssistContext, String ruleName,
				IQualifiedNameConverter qualifiedNameConverter) {
			super(contentAssistContext, ruleName, qualifiedNameConverter);
		}

	}

	@Override
	public void completeKeyword(Keyword keyword, ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor) {
		if (contentAssistContext.getCurrentModel() instanceof PredicateObjectMapping) {
			OutputType type = modelAccess.outputType(contentAssistContext.getCurrentModel());
			Predicate<ICompletionProposal> filter = new RmlishCompletionProposalPredicate("parent-map", keyword, type)//
					.and(new WhitelistedOutputTypeCompletionProposalPredicate("multi-reference", keyword,
							Collections.singleton(OutputType.CARML), type));
			super.completeKeyword(keyword, contentAssistContext,
					new FilteringCompletionProposalAcceptor(acceptor, filter));

		} else if (contentAssistContext.getCurrentModel() instanceof ReferenceValuedTerm
				|| contentAssistContext.getCurrentModel() instanceof TemplateValuedTerm) {
			OutputType type = modelAccess.outputType(contentAssistContext.getCurrentModel());
			super.completeKeyword(keyword, contentAssistContext, new FilteringCompletionProposalAcceptor(acceptor,
					new RmlishCompletionProposalPredicate("as", keyword, type)));

		} else {
			super.completeKeyword(keyword, contentAssistContext, acceptor);
		}
	}

}
