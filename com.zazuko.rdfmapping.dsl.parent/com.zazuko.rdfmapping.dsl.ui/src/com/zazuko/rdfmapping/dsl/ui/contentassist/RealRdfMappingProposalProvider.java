package com.zazuko.rdfmapping.dsl.ui.contentassist;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;

import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.services.RdfDslConverters.RdfQualifiedNameConverter;

public class RealRdfMappingProposalProvider extends AbstractRdfMappingProposalProvider {

	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;

	@Inject
	private RdfQualifiedNameConverter rdfDslConverter;

	protected StyledString getStyledDisplayString(IEObjectDescription description) {
		EObject eo = description.getEObjectOrProxy();
		if (eo instanceof RdfProperty) {
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
}
