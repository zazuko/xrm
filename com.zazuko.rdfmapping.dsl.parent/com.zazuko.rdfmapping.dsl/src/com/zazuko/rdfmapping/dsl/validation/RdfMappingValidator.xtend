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
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType
import com.zazuko.rdfmapping.dsl.rdfMapping.TermTypeRef
import com.zazuko.rdfmapping.dsl.services.InputOutputCompatibility
import java.util.ArrayList
import java.util.List
import java.util.Set
import javax.inject.Inject
import org.eclipse.xtext.validation.Check

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
	def void checkReferenceableDeclarationNullValueMarker(NullValueDeclaration it) {
		val Referenceable ref = eContainer as Referenceable;
		if (ref === null) {
			return;
		}
		if (ref.nullValueMarker === null) {
			return;
		}
		val LogicalSource ls = ref.eContainer as LogicalSource;
		if (ls === null) {
			return;
		}
		val SourceType type = ls.typeResolved;
		if (type !== null && !SourceType.CSV.equals(type)) {
			error("Type 'csv' required for null value declaration, but was '" + type + "'", ref,
				RdfMappingPackage.Literals.REFERENCEABLE__NULL_VALUE_MARKER);
		}
	}

	@Check
	def void check(Domainmodel it) {
		if (outputType !== null && elements.filter(typeof(Mapping)).empty) {
			warning("No outputType needed when no mapping is declared.",
				RdfMappingPackage.Literals.DOMAINMODEL__OUTPUT_TYPE,
				RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS);
		}
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
			if (ownSourceType !== null) {
				val Set<OutputType> compatibleOutputTypes = ownSourceType.compatibleOutputTypes;
				if (!compatibleOutputTypes.contains(domainModel.outputType.type)) {
					val String msg = "Output of type " + domainModel.outputType.type.literal +
						" is incompatible. Expected one of " + compatibleOutputTypes.serialize2Message;
					error(msg, RdfMappingPackage.Literals.MAPPING__NAME,
						RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE)
				}
			}
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
	def void check(MultiReferenceValuedTerm it) {
		carmlOnly(outputType);
	}
	
	def private carmlOnly(OutputType outputType) {
		if (outputType === null) {
			return;
		}
		if (!OutputType.CARML.equals(outputType)) {
			error("Not on output of type '" + outputType.literal + "' - only valid on " +
				OutputType.CARML.serialize2Message, null,
				// no quickfix - it would be complicated and the case should not happen at all (omitted completion)
				RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS + "nofix"
				);
		}
	}

}
