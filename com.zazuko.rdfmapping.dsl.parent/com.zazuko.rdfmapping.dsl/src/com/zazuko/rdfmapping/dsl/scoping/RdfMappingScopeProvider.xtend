package com.zazuko.rdfmapping.dsl.scoping

import com.zazuko.rdfmapping.dsl.rdfMapping.LinkedResourceTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.scoping.Scopes

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
class RdfMappingScopeProvider extends AbstractRdfMappingScopeProvider {

	override getScope(EObject context, EReference reference) {
		if (context instanceof ReferenceValuedTerm &&
			reference == RdfMappingPackage.Literals.REFERENCE_VALUED_TERM__REFERENCE) {
			return scopeForReferenceables(context.eContainer.eContainer as Mapping)

		} else if (context instanceof TemplateValuedTerm &&
			reference == RdfMappingPackage.Literals.TEMPLATE_VALUED_TERM__REFERENCES) {
			val mapping = switch context.eContainer {
				Mapping: context.eContainer as Mapping
				PredicateObjectMapping: context.eContainer.eContainer as Mapping
			}
			return scopeForReferenceables(mapping)

		} else if (context instanceof LinkedResourceTerm &&
			reference == RdfMappingPackage.Literals.LINKED_RESOURCE_TERM__REFERENCES) {
			return scopeForReferenceables(context.eContainer.eContainer as Mapping)

		}
		return super.getScope(context, reference);
	}

	def scopeForReferenceables(Mapping mapping) {
		return Scopes.scopeFor(mapping.source.referencables)
	}
}
