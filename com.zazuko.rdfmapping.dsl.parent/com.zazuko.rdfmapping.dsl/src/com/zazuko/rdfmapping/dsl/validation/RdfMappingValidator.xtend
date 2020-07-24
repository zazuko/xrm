/*
 * generated by Xtext 2.18.0
 */
package com.zazuko.rdfmapping.dsl.validation

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants
import com.zazuko.rdfmapping.dsl.common.RdfMappingValidationCodes
import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.Element
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.NullValueDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValueDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.TermType
import com.zazuko.rdfmapping.dsl.rdfMapping.TermTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
import com.zazuko.rdfmapping.dsl.services.InputOutputCompatibility
import com.zazuko.rdfmapping.dsl.util.LazyMap
import com.zazuko.rdfmapping.dsl.validation.TemplateFormatAnalyzer.TemplateFormatAnalyzerException
import java.util.ArrayList
import java.util.LinkedList
import java.util.List
import java.util.Set
import java.util.TreeMap
import javax.inject.Inject
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.CheckType

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class RdfMappingValidator extends AbstractRdfMappingValidator {

	@Inject
	extension ModelAccess

	@Inject
	extension InputOutputCompatibility

	@Inject
	TemplateFormatAnalyzer templateAnalyzer

	@Inject
	DuplicatedQualifiedNameValidator duplicatedQNameValidator

	@Check(CheckType.NORMAL)
	def checkDuplicatedTemplateDeclaration(TemplateDeclaration it) {
		duplicatedQNameValidator.validate(it, RdfMappingPackage.Literals.TEMPLATE_DECLARATION, [ msg | error(msg, RdfMappingPackage.Literals.TEMPLATE_DECLARATION__NAME)]);
	}

	@Check
	def void checkTypeDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer instanceof SourceGroup) {
			val SourceGroup sourceGroup = logicalSource.eContainer as SourceGroup;
			if (sourceGroup.typeRef !== null) {
				if (logicalSource.typeRef !== null) {
					warning("Type declared on source-group level is shadowed by type declared on logical-source.",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__TYPE_REF);
				}
			}
			if (sourceGroup.typeRef === null) {
				if (logicalSource.typeRef === null) {
					error("No type declared for the logical-source or source-group",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
				}
			}
		} else {
			if (logicalSource.typeRef === null) {
				error("No type declared for the logical-source", RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
			}
		}
	}

	@Check
	def void checkSourceDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer instanceof SourceGroup) {
			val SourceGroup sourceGroup = logicalSource.eContainer as SourceGroup;
			if (sourceGroup.source !== null) {
				if (logicalSource.source !== null) {
					warning("Source declared on source-group level is shadowed by source declared on logical-source.",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__SOURCE);
				}
			}
			if (sourceGroup.source === null) {
				if (logicalSource.source === null) {
					error("No source declared for the logical-source or source-group",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
				}
			}
		} else {
			if (logicalSource.source === null) {
				error("No source declared for the logical-source", RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME);
			}
		}
	}

	@Check
	def void checkSourceTypeSpecificRules(SourceGroup it) {
		val SourceType type = typeRef?.type;
		if (type === null) {
			return;
		}

		if (!SourceType.CSV.equals(type) && dialect !== null) {
			error("Dialect is for sourceType CSV only", RdfMappingPackage.Literals.SOURCE_GROUP__DIALECT);
		}
		if (!SourceType.XML.equals(type) && xmlNamespaceExtension !== null) {
			error("xml-namespace-extension is for sourceType XML only",
				RdfMappingPackage.Literals.SOURCE_GROUP__XML_NAMESPACE_EXTENSION);
		}
	}

	@Check
	def void checkSourceTypeSpecificRules(LogicalSource it) {
		val SourceType type = typeResolved;
		if (type === null) {
			return;
		}

		if (!SourceType.CSV.equals(type) && dialect !== null) {
			error("Dialect is for sourceType CSV only", RdfMappingPackage.Literals.LOGICAL_SOURCE__DIALECT);
		}
		if (!SourceType.XML.equals(type) && xmlNamespaceExtension !== null) {
			error("xml-namespace-extension is for sourceType XML only",
				RdfMappingPackage.Literals.LOGICAL_SOURCE__XML_NAMESPACE_EXTENSION);
		}
	}

	@Check
	def void checkReferenceableDeclarationNullValueMarker(NullValueDeclaration it) {
		val SourceType type = typeResolved;
		if (type !== null && !SourceType.CSV.equals(type)) {
			error("Type 'csv' required for null value declaration, but was '" + type + "'", eContainer,
				resolveNullValueFeature(eContainer));
		}
	}
	
	def private dispatch resolveNullValueFeature(Referenceable it) {
		return RdfMappingPackage.Literals.REFERENCEABLE__NULL_VALUE_MARKER;
	}
	
	def private dispatch resolveNullValueFeature(LogicalSource it) {
		return RdfMappingPackage.Literals.LOGICAL_SOURCE__NULL_VALUE_MARKER;
	}

	def private dispatch resolveNullValueFeature(SourceGroup it) {
		return RdfMappingPackage.Literals.SOURCE_GROUP__NULL_VALUE_MARKER;
	}

	@Check
	def void check(Domainmodel it) {
		if (outputType !== null && elements.filter(typeof(Mapping)).empty) {
			warning("No outputType needed when no mapping is declared.",
				RdfMappingPackage.Literals.DOMAINMODEL__OUTPUT_TYPE,
				RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS);
		}

		new DuplicationCheck().check(
			elements.filter(TemplateDeclaration).toList,
			[name],
			[ name, declaration |
				error("Declaration name already in use: " + name, declaration,
					RdfMappingPackage.Literals.TEMPLATE_DECLARATION__NAME);
			]
		)

	}

	@Check
	def void check(Mapping it) {
		val Domainmodel domainModel = findParent(Domainmodel);
		if (domainModel === null) {
			return;
		}
		if (domainModel.elements === null || domainModel.elements.empty) {
			return;
		}

		val List<Element> nameOccurrences = new ArrayList();
		for (Mapping candidate : domainModel.elements.filter(typeof(Mapping))) {
			if (candidate.name.equals(name)) {
				nameOccurrences.add(candidate);
			}
		}
		if (nameOccurrences.size > 1) {
			error("Mapping name already in use.", RdfMappingPackage.Literals.MAPPING__NAME);
		}

		val SourceType ownSourceType = source?.typeResolved;
		if (domainModel.outputType === null) {
			var String msg = "A mapping requires an outputType.";
			if (ownSourceType !== null) {
				msg += " Choose from " + ownSourceType.compatibleOutputTypes.serialize2Message;
			}
			error(msg, RdfMappingPackage.Literals.MAPPING__NAME, RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_MISSING);
		} else {
			// having an outputType
			if (ownSourceType !== null) {
				val Set<OutputType> compatibleOutputTypes = ownSourceType.compatibleOutputTypes;
				if (!compatibleOutputTypes.contains(domainModel.outputType.type)) {
					val String msg = "Output of type " + domainModel.outputType.type.literal +
						" is incompatible. Expected one of " + compatibleOutputTypes.serialize2Message;
					error(msg, RdfMappingPackage.Literals.MAPPING__NAME,
						RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE);
				}
			}

			if (source?.xmlNamespaceExtensionResolved !== null) {
				if (!OutputType.CARML.equals(domainModel.outputType.type)) {
					val String msg = "Source with xml-namespace-extension requires OutputType " +
						OutputType.CARML.serialize2Message;
					error(msg, RdfMappingPackage.Literals.MAPPING__SOURCE,
						RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE);
				}
			}
		}

		// no literal on subjectIriMapping
		if (subjectIriMapping !== null && subjectIriMapping.termTypeRef !== null &&
			TermType.LITERAL.equals(subjectIriMapping.termTypeRef.type)) {
			error("Literal is invalid on the subject", subjectIriMapping.termTypeRef,
				RdfMappingPackage.Literals.TERM_TYPE_REF__TYPE);
		}
	}

	@Check
	def void check(ParentTriplesMapTerm it) {
		onlyOnRmlishType(outputType);
	}

	def private onlyOnRmlishType(OutputType outputType) {
		if (outputType === null) {
			return;
		}
		if (!RdfMappingConstants.RMLISH_OUTPUTTYPES.contains(outputType)) {
			warning("Not on output of type '" + outputType.literal + "' - only valid on " +
				RdfMappingConstants.RMLISH_OUTPUTTYPES.serialize2Message, null,
				RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS);
		}
	}

	@Check
	def void check(TermTypeRef it) {
		onlyOnRmlishType(outputType);
	}

	@Check
	def void check(PredicateObjectMapping it) {
		if (term === null) {
			error("ValuedTerm missing", null, "missing");
		}
	}

	@Check
	def void check(MultiReferenceValuedTerm it) {
		carmlOnly(outputType);
	}

	def private carmlOnly(OutputType outputType) {
		if (outputType === null) {
			return;
		}
		if (!OutputType.CARML.equals(outputType)) {
			error(
				"Not on output of type '" + outputType.literal + "' - only valid on " +
					OutputType.CARML.serialize2Message,
				null,
				// no quickfix - it would be complicated and the case should not happen at all (omitted completion)
				RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS_NOFIX
			);
		}
	}

	@Check
	def void prefixLabelWithoutSeparator(Prefix it) {
		if (label !== null && label.contains(RdfMappingConstants.PREFIX_LABEL_SEPARATOR_CHARACTER)) {
			error("No separator characters allowed", it, RdfMappingPackage.eINSTANCE.prefix_Label,
				RdfMappingValidationCodes.PREFIX_LABEL_SEPARATOR);
		}
	}

	@Check
	def void xmlNamespaceExtension(XmlNamespaceExtension it) {
		val LazyMap<String, List<Prefix>> label2Prefix = new LazyMap(new TreeMap, [new LinkedList]);
		for (Prefix current : prefixes) {
			if (current.label !== null) {
				label2Prefix.getOrInit(current.label).add(current);
			}
		}

		label2Prefix.values.stream().filter[list|list.size > 1].flatMap[list|list.stream].forEach [ duplicatedPrefix |
			error("Duplicated Label", duplicatedPrefix, RdfMappingPackage.eINSTANCE.prefix_Label);
		];
	}

	@Check
	def void templateFormat(TemplateValueDeclaration it) {
		if (templateValue !== null) {
			var TemplateFormatAnalysis data;
			try {
				data = templateAnalyzer.analyzeFormats(templateValue);
			} catch (TemplateFormatAnalyzerException e) {
				error("Pattern invalid: " + e.message, it,
					RdfMappingPackage.eINSTANCE.templateValueDeclaration_TemplateValue);
				return;
			}
			if (!data.getSkippedKeys.empty) {
				error("Pattern invalid, skipped keys " + data.getSkippedKeys.toList.toString, it,
					RdfMappingPackage.eINSTANCE.templateValueDeclaration_TemplateValue);
			}
		}
	}

	@Check
	def void templateValuedTerm_parameterSatisfied(TemplateValuedTerm it) {
		if (template === null) {
			return;
		}
		val String templateValue = template.templateValueResolved;
		var TemplateFormatAnalysis data;
		try {
			data = templateAnalyzer.analyzeFormats(templateValue);
		} catch (TemplateFormatAnalyzerException e) {
			// this is not an issue to be marked here - just go away
			return;
		}

		if (data.getUsedKeys.size != references.size) {
			warning(
				"Pattern '" + templateValue + "' requires " + data.getUsedKeys.size + " argument(s), but there are " +
					references.size,
				it,
				null
			);
		}
	}

}
