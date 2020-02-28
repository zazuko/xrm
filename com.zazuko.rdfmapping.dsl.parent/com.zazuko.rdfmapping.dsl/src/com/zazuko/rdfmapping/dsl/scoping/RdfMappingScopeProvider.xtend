package com.zazuko.rdfmapping.dsl.scoping

import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.util.RootFinder
import javax.inject.Inject
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
	
	@Inject extension RootFinder

	override getScope(EObject context, EReference reference) {
		if (context instanceof ReferenceValuedTerm &&
			reference == RdfMappingPackage.Literals.REFERENCE_VALUED_TERM__REFERENCE) {
			return scopeForReferenceables(context.findRoot(Mapping))
		
		} else if (context instanceof MultiReferenceValuedTerm &&
			reference == RdfMappingPackage.Literals.MULTI_REFERENCE_VALUED_TERM__REFERENCE) {
			return scopeForReferenceables(context.findRoot(Mapping))

		} else if (context instanceof TemplateValuedTerm &&
			reference == RdfMappingPackage.Literals.TEMPLATE_VALUE_REF__TEMPLATE_DECLARATION) {
			return scopeForReferenceablesTemplates(context.findRoot(Domainmodel))

		} else if (context instanceof TemplateValuedTerm &&
			reference == RdfMappingPackage.Literals.TEMPLATE_VALUED_TERM__REFERENCES) {
			return scopeForReferenceables(context.findRoot(Mapping))

		}
		return super.getScope(context, reference);
	}

	def private scopeForReferenceables(Mapping mapping) {
		return Scopes.scopeFor(mapping.source.referenceables);
	}
	def private scopeForReferenceablesTemplates(Domainmodel it) {
		return Scopes.scopeFor(elements.filter(TemplateDeclaration));
	}
}
