package com.zazuko.rdfmapping.dsl.ui.quickfix;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.diagnostics.Diagnostic;
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
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingFactory;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary;
import com.zazuko.rdfmapping.dsl.services.InputOutputCompatibility;
import com.zazuko.rdfmapping.dsl.ui.contentassist.RdfMappingPrefixMatcher;

public class RealRdfMappingQuickfixProvider extends DefaultQuickfixProvider {

	@Inject
	private RdfMappingFactory modelFactory;

	@Inject
	private InputOutputCompatibility ioCompat;

	@Fix(RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS)
	public void removeOutputType(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Remove", "Remove declaration of outputType.", null, new ISemanticModification() {

			@Override
			public void apply(EObject element, IModificationContext context) throws Exception {
				if (!(element instanceof Domainmodel)) {
					return;
				}
				Domainmodel target = (Domainmodel) element;
				target.setOutputType(null);
			}
		});
	}

	@Fix(RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_MISSING)
	@Fix(RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE)
	public void mappingAddOutputType(Issue issue, IssueResolutionAcceptor acceptor) {
		// nasty - but there is no access to the eobject right now
		Set<OutputType> compatibleOutputTypes = this.ioCompat.extractFromMessage(issue.getMessage(), OutputType.class);
		if (compatibleOutputTypes == null) {
			compatibleOutputTypes = new LinkedHashSet<>(OutputType.VALUES);
		}
		for (final OutputType candidate : compatibleOutputTypes) {
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

					Domainmodel model = (Domainmodel) element.eContainer();
					model.setOutputType(result);
				}
			});
		}
	}

	@Fix(RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS)
	public void eObjectRemoval(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Remove", "Remove", null, new ISemanticModification() {

			@Override
			public void apply(EObject element, IModificationContext context) throws Exception {
				if (element == null) {
					return;
				}
				EObject container = element.eContainer();
				if (container == null) {
					return;
				}
				EList<EReference> containments = container.eClass().getEAllContainments();
				for (EReference ref : containments) {

					if (ref.isMany()) {
						Collection<?> containingList = (Collection<?>) container.eGet(ref);
						if (containingList.contains(element)) {
							containingList.remove(element);
						}
					} else {
						container.eUnset(ref);
					}
				}

			}
		});
	}

	@Fix(Diagnostic.LINKING_DIAGNOSTIC)
	public void createMissingStuff(Issue issue, IssueResolutionAcceptor acceptor) {
		// parsing the message is ugly, sorry.
		if (issue.getMessage().contains(RdfProperty.class.getSimpleName())) {
			try {
				missingRdfProperty(issue, acceptor);
			} catch (RuntimeException e) {
				// add a breakpoint on the next line
				throw e;
			}
		}

	}

	private void missingRdfProperty(Issue issue, IssueResolutionAcceptor acceptor) {
		String msg = "Add " + RdfProperty.class.getSimpleName();
		acceptor.accept(issue, msg, msg, null, new ISemanticModification() {

			@Override
			public void apply(EObject element, IModificationContext context) throws Exception {
				if (!(element instanceof PredicateObjectMapping)) {
					return;
				}
				PredicateObjectMapping poMapping = (PredicateObjectMapping) element;

				String propertyAddress = context.getXtextDocument().get(issue.getOffset(), issue.getLength());
				String propertyName = "new";
				String vocabularyName = null;
				if (propertyAddress != null && !"".equals(propertyAddress)) {
					String[] segments = propertyAddress.split(RdfMappingPrefixMatcher.DELIMITERS_REGEX);
					propertyName = segments[segments.length - 1];
					if (segments.length > 1) {
						vocabularyName = segments[0];
					}
				}

				Vocabulary vocabulary = null;
				if (vocabularyName != null) {
					vocabulary = findVocabulary(vocabularyName, element);
				}
				final boolean vocabularyInForeignFile = vocabulary != null;

				if (vocabulary == null) {
					vocabulary = RdfMappingFactory.eINSTANCE.createVocabulary();
					EObject mapping = poMapping.eContainer();
					Domainmodel d = (Domainmodel) mapping.eContainer();
					d.getElements().add(vocabulary);
					vocabulary.setName(vocabularyName != null ? vocabularyName : "new");

					Prefix prefix = RdfMappingFactory.eINSTANCE.createPrefix();
					// it has to be valid
					vocabulary.setPrefix(prefix);

					prefix.setLabel(vocabulary.getName().toLowerCase());
					prefix.setIri("http://" + prefix.getLabel() + ".org/TODO");
				}

				RdfProperty newProperty = RdfMappingFactory.eINSTANCE.createRdfProperty();
				vocabulary.getProperties().add(newProperty);

				newProperty.setName(propertyName);

				poMapping.setProperty(newProperty);
				
				if (vocabularyInForeignFile) {
					vocabulary.eResource().save(Collections.emptyMap());
				}
			}

			private Vocabulary findVocabulary(String vocabularyName, EObject element) {
				TreeIterator<Notifier> it = element.eResource().getResourceSet().getAllContents();
				while (it.hasNext()) {
					Notifier current = it.next();
					if (current instanceof Vocabulary) {
						Vocabulary cast = (Vocabulary) current;
						if (vocabularyName.equals(cast.getName())) {
							return cast;
						}
					}
				}
				return null;
			}
		});
	}
}
