package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.Collections;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
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

	@Override
	public void completeKeyword(Keyword keyword, ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor) {
		if (contentAssistContext.getCurrentModel() instanceof PredicateObjectMapping) {
			OutputType type = modelAccess.outputType(contentAssistContext.getCurrentModel());
			Predicate<ICompletionProposal> filter = new RmlishOutputTypeCompletionProposalPredicate("parent-map",
					keyword, type)//
							.and(new WhitelistedEnumTypeCompletionProposalPredicate<OutputType>("multi-reference",
									keyword, Collections.singleton(OutputType.CARML), type));
			super.completeKeyword(keyword, contentAssistContext,
					new FilteringCompletionProposalAcceptor(acceptor, filter));

		} else if (contentAssistContext.getCurrentModel() instanceof ReferenceValuedTerm
				|| contentAssistContext.getCurrentModel() instanceof TemplateValuedTerm) {
			OutputType type = modelAccess.outputType(contentAssistContext.getCurrentModel());
			super.completeKeyword(keyword, contentAssistContext, new FilteringCompletionProposalAcceptor(acceptor,
					new RmlishOutputTypeCompletionProposalPredicate("as", keyword, type)));

		} else if (contentAssistContext.getCurrentModel() instanceof SourceGroup) {
			SourceGroup cast = (SourceGroup) contentAssistContext.getCurrentModel();
			this.completeKeywordForSourceDefinition(keyword, contentAssistContext, acceptor,
					cast.getTypeRef() != null ? cast.getTypeRef().getType() : null);

		} else if (contentAssistContext.getCurrentModel() instanceof LogicalSource) {
			LogicalSource cast = (LogicalSource) contentAssistContext.getCurrentModel();
			this.completeKeywordForSourceDefinition(keyword, contentAssistContext, acceptor,
					cast.getTypeRef() != null ? cast.getTypeRef().getType() : null);

		} else {
			super.completeKeyword(keyword, contentAssistContext, acceptor);
		}
	}

	private void completeKeywordForSourceDefinition(Keyword keyword, ContentAssistContext contentAssistContext,
			ICompletionProposalAcceptor acceptor, SourceType type) {

		Predicate<ICompletionProposal> filter = //
				new WhitelistedEnumTypeCompletionProposalPredicate<SourceType>("xml-namespace-extension", keyword, //
						Collections.singleton(SourceType.XML), //
						type) //
								.and( //
										new WhitelistedEnumTypeCompletionProposalPredicate<SourceType>("dialect",
												keyword, //
												Collections.singleton(SourceType.CSV), //
												type)//
								);

		super.completeKeyword(keyword, contentAssistContext, new FilteringCompletionProposalAcceptor(acceptor, filter));

	}

	@Override
	public void complete_BLOCK_BEGIN(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		acceptor.accept(this.createCompletionProposal(RdfMappingConstants.TOKEN_BLOCK_BEGIN,
				RdfMappingConstants.TOKEN_BLOCK_BEGIN, null, context));
	}

	@Override
	public void complete_BLOCK_END(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		acceptor.accept(this.createCompletionProposal(RdfMappingConstants.TOKEN_BLOCK_END,
				RdfMappingConstants.TOKEN_BLOCK_END, null, context));
	}
	
	@Override
	public void complete_LINE_END(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof PredicateObjectMapping) {
			PredicateObjectMapping pom = (PredicateObjectMapping)model;
			if (pom.getTerm() == null || pom.isLineEnd()) {
				return; // do not offer ';' if there is no valuedTerm or there is already a ';'
			}
		}
		acceptor.accept(this.createCompletionProposal(RdfMappingConstants.TOKEN_LINE_END,
				RdfMappingConstants.TOKEN_LINE_END, null, context));
	}

}
