package com.zazuko.rdfmapping.dsl.ui.quickfix;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.zazuko.rdfmapping.dsl.common.RdfMappingValidationCodes;
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputTypeRef;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingFactory;

public class RealRdfMappingQuickfixProvider extends DefaultQuickfixProvider {

	@Inject
	private RdfMappingFactory modelFactory;
	
	
	@Fix(RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS)
	public void removeOutputType(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Remove", "Remove declaration of outputType.", null, new ISemanticModification() {
			
			@Override
			public void apply(EObject element, IModificationContext context) throws Exception {
				if (!(element instanceof Domainmodel)) {
					return;
				}
				Domainmodel target = (Domainmodel)element;
				target.setOutputType(null);
			}
		});
	}
	
	@Fix(RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_MISSING)
	public void mappingAddOutputType(Issue issue, IssueResolutionAcceptor acceptor) {
		for (final OutputType candidate : OutputType.VALUES) {
			String msg = String.format("Set output to '%s'", candidate.getLiteral());
			acceptor.accept(issue, msg, msg, null, new ISemanticModification() {
				
				@Override
				public void apply(EObject element, IModificationContext context) throws Exception {
					if (!(element instanceof Mapping)) {
						return;
					}
					if (!(element.eContainer() instanceof Domainmodel)) {
						return;
					}
					OutputTypeRef result = modelFactory.createOutputTypeRef();
					result.setType(candidate);

					Domainmodel model = (Domainmodel)element.eContainer();
					model.setOutputType(result);
				}
			});
		}
		
	}
}
