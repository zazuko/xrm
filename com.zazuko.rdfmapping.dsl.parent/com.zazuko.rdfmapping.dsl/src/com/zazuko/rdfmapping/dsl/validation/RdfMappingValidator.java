package com.zazuko.rdfmapping.dsl.validation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.common.RdfMappingValidationCodes;
import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess;
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype;
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel;
import com.zazuko.rdfmapping.dsl.rdfMapping.Element;
import com.zazuko.rdfmapping.dsl.rdfMapping.LanguageTag;
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource;
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.NullValueDeclaration;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateDeclaration;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValueDeclaration;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.TermType;
import com.zazuko.rdfmapping.dsl.rdfMapping.TermTypeRef;
import com.zazuko.rdfmapping.dsl.rdfMapping.ValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary;
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension;
import com.zazuko.rdfmapping.dsl.services.InputOutputCompatibility;
import com.zazuko.rdfmapping.dsl.util.LazyMap;
import com.zazuko.rdfmapping.dsl.validation.TemplateFormatAnalyzer.TemplateFormatAnalyzerException;

/**
 * This class contains custom validation rules.
 * 
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
public class RdfMappingValidator extends AbstractRdfMappingValidator {

	@Inject
	private ModelAccess modelAccess;

	@Inject
	private InputOutputCompatibility inputOutputCompatibility;

	@Inject
	private TemplateFormatAnalyzer templateAnalyzer;

	@Inject
	private DuplicatedQualifiedNameValidator duplicatedQNameValidator;

	@Check(CheckType.NORMAL)
	public void checkDuplicatedSourceGroup(SourceGroup it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.SOURCE_GROUP,
				msg -> error(msg, RdfMappingPackage.Literals.SOURCE_GROUP__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedLogicalSource(LogicalSource it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.LOGICAL_SOURCE,
				msg -> error(msg, RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedReferenceable(Referenceable it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.REFERENCEABLE,
				msg -> error(msg, RdfMappingPackage.Literals.REFERENCEABLE__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedVocabulary(Vocabulary it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.VOCABULARY,
				msg -> error(msg, RdfMappingPackage.Literals.VOCABULARY__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedRdfClass(RdfClass it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.RDF_CLASS,
				msg -> error(msg, RdfMappingPackage.Literals.VOCABULARY_ELEMENT__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedRdfProperty(RdfProperty it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.RDF_PROPERTY,
				msg -> error(msg, RdfMappingPackage.Literals.VOCABULARY_ELEMENT__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedDatatype(Datatype it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.DATATYPE,
				msg -> error(msg, RdfMappingPackage.Literals.VOCABULARY_ELEMENT__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedMapping(Mapping it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.MAPPING,
				msg -> error(msg, RdfMappingPackage.Literals.MAPPING__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedLanguageTag(LanguageTag it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.LANGUAGE_TAG,
				msg -> error(msg, RdfMappingPackage.Literals.LANGUAGE_TAG__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedDialectGroup(DialectGroup it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.DIALECT_GROUP,
				msg -> error(msg, RdfMappingPackage.Literals.DIALECT_GROUP__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedXmlNamespaceExtension(XmlNamespaceExtension it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.XML_NAMESPACE_EXTENSION,
				msg -> error(msg, RdfMappingPackage.Literals.XML_NAMESPACE_EXTENSION__NAME));
	}

	@Check(CheckType.NORMAL)
	public void checkDuplicatedTemplateDeclaration(TemplateDeclaration it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.TEMPLATE_DECLARATION,
				msg -> error(msg, RdfMappingPackage.Literals.TEMPLATE_DECLARATION__NAME));
	}

	@Check
	public void checkTypeDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer() instanceof SourceGroup) {
			SourceGroup sourceGroup = (SourceGroup) logicalSource.eContainer();
			if (sourceGroup.getTypeRef() != null) {
				if (logicalSource.getTypeRef() != null) {
					warning("Type declared on source-group level is shadowed by type declared on logical-source.",
							RdfMappingPackage.Literals.LOGICAL_SOURCE__TYPE_REF);
				}
			}
			if (sourceGroup.getTypeRef() == null) {
				if (logicalSource.getTypeRef() == null) {
					error("No type declared for the logical-source or source-group",
							RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
				}
			}
		} else {
			if (logicalSource.getTypeRef() == null) {
				error("No type declared for the logical-source", RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
			}
		}
	}

	@Check
	public void checkSourceDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer() instanceof SourceGroup) {
			SourceGroup sourceGroup = (SourceGroup) logicalSource.eContainer();
			if (sourceGroup.getSource() != null) {
				if (logicalSource.getSource() != null) {
					warning("Source declared on source-group level is shadowed by source declared on logical-source.",
							RdfMappingPackage.Literals.LOGICAL_SOURCE__SOURCE);
				}
			}
			if (sourceGroup.getSource() == null) {
				if (logicalSource.getSource() == null) {
					error("No source declared for the logical-source or source-group",
							RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
				}
			}
		} else {
			if (logicalSource.getSource() == null) {
				error("No source declared for the logical-source", RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
			}
		}
	}

	@Check
	public void checkSourceTypeSpecificRules(SourceGroup it) {
		SourceType type = it.getTypeRef() != null ? it.getTypeRef().getType() : null;
		if (type == null) {
			return;
		}

		if (!SourceType.CSV.equals(type) && it.getDialect() != null) {
			error("Dialect is for sourceType CSV only", RdfMappingPackage.Literals.SOURCE_GROUP__DIALECT);
		}
		if (!SourceType.XML.equals(type) && it.getXmlNamespaceExtension() != null) {
			error("xml-namespace-extension is for sourceType XML only",
					RdfMappingPackage.Literals.SOURCE_GROUP__XML_NAMESPACE_EXTENSION);
		}
	}

	@Check
	public void checkSourceTypeSpecificRules(LogicalSource it) {
		SourceType type = this.modelAccess.typeResolved(it);
		if (type == null) {
			return;
		}

		if (!SourceType.CSV.equals(type) && it.getDialect() != null) {
			error("Dialect is for sourceType CSV only", RdfMappingPackage.Literals.LOGICAL_SOURCE__DIALECT);
		}
		if (!SourceType.XML.equals(type) && it.getXmlNamespaceExtension() != null) {
			error("xml-namespace-extension is for sourceType XML only",
					RdfMappingPackage.Literals.LOGICAL_SOURCE__XML_NAMESPACE_EXTENSION);
		}
	}

	@Check
	public void checkReferenceableDeclarationNullValueMarker(NullValueDeclaration it) {
		SourceType type = this.modelAccess.typeResolved(it);
		if (type != null && !SourceType.CSV.equals(type)) {
			error("Type 'csv' required for null value declaration, but was '" + type + "'", it.eContainer(),
					resolveNullValueFeature(it.eContainer()));
		}
	}

	private EStructuralFeature resolveNullValueFeature(EObject it) {
		if (it instanceof Referenceable) {
			return RdfMappingPackage.Literals.REFERENCEABLE__NULL_VALUE_MARKER;
		} else if (it instanceof LogicalSource) {
			return RdfMappingPackage.Literals.LOGICAL_SOURCE__NULL_VALUE_MARKER;
		} else if (it instanceof SourceGroup) {
			return RdfMappingPackage.Literals.SOURCE_GROUP__NULL_VALUE_MARKER;
		} else {
			throw new RuntimeException("uknown type: " + it);
		}
	}

	@Check
	public void check(Domainmodel it) {
		if (it.getOutputType() != null
				&& it.getElements().stream().filter(current -> current instanceof Mapping).findAny().isEmpty()) {
			warning("No outputType needed when no mapping is declared.",
					RdfMappingPackage.Literals.DOMAINMODEL__OUTPUT_TYPE,
					RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS);
		}

		List<TemplateDeclaration> templateDeclarations = it.getElements().stream()//
				.filter(current -> current instanceof TemplateDeclaration).map(current -> (TemplateDeclaration) current)
				.collect(Collectors.toList());
		new DuplicationCheck<TemplateDeclaration>().check(templateDeclarations, //
				TemplateDeclaration::getName, //
				(name, declaration) -> error("Declaration name already in use: " + name, declaration,
						RdfMappingPackage.Literals.TEMPLATE_DECLARATION__NAME));

	}

	@Check
	public void check(Mapping it) {
		Domainmodel domainModel = this.modelAccess.findParent(it, Domainmodel.class);
		if (domainModel == null) {
			return;
		}
		if (domainModel.getElements().isEmpty()) {
			return;
		}

		List<Element> nameOccurrences = new ArrayList<>();
		domainModel.getElements().stream() //
				.filter(current -> current instanceof Mapping) //
				.map(current -> (Mapping) current).filter(candidate -> it.getName().equals(candidate.getName()))
				.forEach(candidate -> nameOccurrences.add(candidate));
		if (nameOccurrences.size() > 1) {
			error("Mapping name already in use.", RdfMappingPackage.Literals.MAPPING__NAME);
		}

		SourceType ownSourceType = it.getSource() != null ? this.modelAccess.typeResolved(it.getSource()) : null;
		if (domainModel.getOutputType() == null) {
			String msg = "A mapping requires an outputType.";
			if (ownSourceType != null) {
				msg += " Choose from " + this.inputOutputCompatibility
						.serialize2Message(this.inputOutputCompatibility.getCompatibleOutputTypes(ownSourceType));
			}
			error(msg, RdfMappingPackage.Literals.MAPPING__NAME, RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_MISSING);
		} else {
			// having an outputType
			if (ownSourceType != null) {
				Set<OutputType> compatibleOutputTypes = this.inputOutputCompatibility
						.getCompatibleOutputTypes(ownSourceType);
				if (!compatibleOutputTypes.contains(domainModel.getOutputType().getType())) {
					String msg = "Output of type " + domainModel.getOutputType().getType().getLiteral()
							+ " is incompatible. Expected one of "
							+ this.inputOutputCompatibility.serialize2Message(compatibleOutputTypes);
					error(msg, RdfMappingPackage.Literals.MAPPING__NAME,
							RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE);
				}
			}

			if (it.getSource() != null && this.modelAccess.xmlNamespaceExtensionResolved(it.getSource()) != null) {
				if (!OutputType.CARML.equals(domainModel.getOutputType().getType())) {
					String msg = "Source with xml-namespace-extension requires OutputType "
							+ this.inputOutputCompatibility.serialize2Message(OutputType.CARML);
					error(msg, RdfMappingPackage.Literals.MAPPING__SOURCE,
							RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE);
				}
			}
			
			if (!it.getGraphMappings().isEmpty()) {
				onlyOnRmlishType(domainModel.getOutputType().getType(), RdfMappingPackage.eINSTANCE.getMapping_GraphMappings());
			}
		}

		// no literal on subjectIriMapping
		if (it.getSubjectIriMapping() != null && it.getSubjectIriMapping().getTermTypeRef() != null
				&& TermType.LITERAL.equals(it.getSubjectIriMapping().getTermTypeRef().getType())) {
			error("Literal is invalid on the subject", it.getSubjectIriMapping().getTermTypeRef(),
					RdfMappingPackage.Literals.TERM_TYPE_REF__TYPE);
		}
		
		
	}

	@Check
	public void check(ParentTriplesMapTerm it) {
		onlyOnRmlishType(this.modelAccess.outputType(it), null);
	}

	private void onlyOnRmlishType(OutputType outputType, EStructuralFeature feature) {
		if (outputType == null) {
			return;
		}
		if (!RdfMappingConstants.RMLISH_OUTPUTTYPES.contains(outputType)) {
			warning("Not on output of type '" + outputType.getLiteral() + "' - only valid on "
					+ this.inputOutputCompatibility.serialize2Message(RdfMappingConstants.RMLISH_OUTPUTTYPES), feature,
					RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS);
		}
	}

	@Check
	public void check(TermTypeRef it) {
		onlyOnRmlishType(this.modelAccess.outputType(it), null);
	}

	@Check
	public void check(PredicateObjectMapping it) {
		if (it.getTerm() == null) {
			error("ValuedTerm missing", null, "missing");
		}
	}

	@Check
	public void check(MultiReferenceValuedTerm it) {
		carmlOnly(this.modelAccess.outputType(it));
	}

	private void carmlOnly(OutputType outputType) {
		if (outputType == null) {
			return;
		}
		if (!OutputType.CARML.equals(outputType)) {
			error("Not on output of type '" + outputType.getLiteral() + "' - only valid on "
					+ this.inputOutputCompatibility.serialize2Message(OutputType.CARML), null,
					// no quickfix - it would be complicated and the case should not happen at all
					// (omitted completion)
					RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS_NOFIX);
		}
	}

	@Check
	public void prefixLabelWithoutSeparator(Prefix it) {
		if (it.getLabel() != null && it.getLabel().contains(RdfMappingConstants.PREFIX_LABEL_SEPARATOR_CHARACTER)) {
			error("No separator characters allowed", it, RdfMappingPackage.eINSTANCE.getPrefix_Label(),
					RdfMappingValidationCodes.PREFIX_LABEL_SEPARATOR);
		}
	}

	@Check
	public void xmlNamespaceExtension(XmlNamespaceExtension it) {
		LazyMap<String, List<Prefix>> label2Prefix = new LazyMap<>(new TreeMap<>(), LinkedList::new);
		for (Prefix current : it.getPrefixes()) {
			if (current.getLabel() != null) {
				label2Prefix.getOrInit(current.getLabel()).add(current);
			}
		}

		label2Prefix.values().stream() //
				.filter(list -> list.size() > 1) //
				.flatMap(List::stream) //
				.forEach(duplicatedPrefix -> error("DuplicatedLabel", duplicatedPrefix,
						RdfMappingPackage.eINSTANCE.getPrefix_Label()));
	}

	@Check
	public void templateFormat(TemplateValueDeclaration it) {
		if (it.getTemplateValue() == null) {
			return;
		}
		TemplateFormatAnalysis data;
		try {
			data = templateAnalyzer.analyzeFormats(it.getTemplateValue());
		} catch (TemplateFormatAnalyzerException e) {
			error("Pattern invalid: " + e.getMessage(), it,
					RdfMappingPackage.eINSTANCE.getTemplateValueDeclaration_TemplateValue());
			return;
		}
		if (!data.getSkippedKeys().isEmpty()) {
			error("Pattern invalid, skipped keys "
					+ data.getSkippedKeys().stream().map(String::valueOf).collect(Collectors.joining(", ", "[", "]")),
					it, RdfMappingPackage.eINSTANCE.getTemplateValueDeclaration_TemplateValue());
		}
	}

	@Check
	public void templateValuedTerm_parameterSatisfied(TemplateValuedTerm it) {
		if (it.getTemplate() == null) {
			return;
		}
		String templateValue = this.modelAccess.templateValueResolved(it.getTemplate());
		TemplateFormatAnalysis data;
		try {
			data = templateAnalyzer.analyzeFormats(templateValue);
		} catch (TemplateFormatAnalyzerException e) {
			// this is not an issue to be marked here - just go away
			return;
		}

		if (data.getUsedKeys().size() != it.getReferences().size()) {
			warning("Pattern '" + templateValue + "' requires " + data.getUsedKeys().size()
					+ " argument(s), but there are " + it.getReferences().size(), it, null);
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Check
	public void termTypeRef_notIn_MappingGraphMappings(TermTypeRef it) {
		if (!(it.eContainer() instanceof ValuedTerm)) {
			return;
		}
		ValuedTerm valuedTerm = (ValuedTerm) it.eContainer();
		
		if (!(valuedTerm.eContainer() instanceof Mapping)) {
			return;
		}
		Mapping mapping = (Mapping) valuedTerm.eContainer();
		
		if (mapping.getGraphMappings().contains(valuedTerm)) {
			error("No TermTypeRef in a GraphMapping", it, null);
		}
	}

	@Check
	public void omniMap(OmniMap it) {
		LazyMap<String, List<OmniMapEntry>> key2entry = new LazyMap<>(new TreeMap<>(), LinkedList::new);
		for (OmniMapEntry current : it.getEntries()) {
			if (current.getKey() != null) {
				key2entry.getOrInit(current.getKey().trim()).add(current);
			}
		}

		key2entry.values().stream() //
				.filter(list -> list.size() > 1) //
				.flatMap(List::stream) //
				.forEach(current -> error("Duplicated entry", current,
						RdfMappingPackage.eINSTANCE.getOmniMapEntry_Key()));

	}

	@Check
	public void omniMapEntry(OmniMapEntry it) {
		if (it.getKey() == null) {
			return;
		}
		String keyTrimmed = it.getKey().trim();
		if (keyTrimmed.isBlank()) {
			error("Key missing", it, RdfMappingPackage.eINSTANCE.getOmniMapEntry_Key());
		} else {
			if (!it.getKey().equals(keyTrimmed)) {
				error("Key must not be surrounded whith whitespaces", it,
						RdfMappingPackage.eINSTANCE.getOmniMapEntry_Key(),
						RdfMappingValidationCodes.OMNIMAPENTRY_KEY_UNTRIMMED);
			}
		}
	}

}
