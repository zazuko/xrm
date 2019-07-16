/*
 * generated by Xtext 2.18.0
 */
package com.zazuko.rdfmapping.dsl.validation

import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import org.eclipse.xtext.validation.Check

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class RdfMappingValidator extends AbstractRdfMappingValidator {

	@Check
	def void checkTypeDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer.eClass.name.equals("SourceGroup")) {
			val sourceGroup = logicalSource.eContainer as SourceGroup;
			if (sourceGroup.type !== null) {
				if (logicalSource.type !== null) {
					warning("Type declared on source-group level is shadowed by type declared on logical-source.",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__TYPE);
				}
			}
			if (sourceGroup.type === null) {
				if (logicalSource.type === null) {
					error("No type declared for the logical-source or source-group",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
				}
			}
		} else {
			if (logicalSource.type === null) {
				error("No type declared for the logical-source", RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
			}
		}
	}

	@Check
	def void checkSourceDeclarations(LogicalSource logicalSource) {
		if (logicalSource.eContainer.eClass.name.equals("SourceGroup")) {
			val sourceGroup = logicalSource.eContainer as SourceGroup;
			if (sourceGroup.source !== null) {
				if (logicalSource.source !== null) {
					warning("Source declared on source-group level is shadowed by source declared on logical-source.",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__SOURCE);
				}
			}
			if (sourceGroup.source === null) {
				if (logicalSource.source === null) {
					error("No source declared for the logical-source or source-group",
						RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
				}
			}
		} else {
			if (logicalSource.source === null) {
				error("No source declared for the logical-source",
					RdfMappingPackage.Literals.LOGICAL_SOURCE__NAME)
			}
		}
	}
}
