package com.zazuko.rdfmapping.dsl.scoping;

import jakarta.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;

import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping;
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapJoinCondition;
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage;
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm;
import com.zazuko.rdfmapping.dsl.util.RootFinder;

/**
 * This class contains custom scoping description.
 * 
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class RdfMappingScopeProvider extends AbstractRdfMappingScopeProvider {

	@Inject
	private RootFinder rootFinder;

	@Override
	public IScope getScope(EObject context, EReference reference) {
		if (context instanceof ReferenceValuedTerm
				&& reference == RdfMappingPackage.Literals.REFERENCE_VALUED_TERM__REFERENCE) {
			return scopeForReferenceables(this.rootFinder.findRoot(context, Mapping.class));

		} else if (context instanceof MultiReferenceValuedTerm
				&& reference == RdfMappingPackage.Literals.MULTI_REFERENCE_VALUED_TERM__REFERENCE) {
			return scopeForReferenceables(this.rootFinder.findRoot(context, Mapping.class));

		} else if (context instanceof TemplateValuedTerm
				&& reference == RdfMappingPackage.Literals.TEMPLATE_VALUED_TERM__REFERENCES) {
			return scopeForReferenceables(this.rootFinder.findRoot(context, Mapping.class));

		} else if (context instanceof ParentTriplesMapJoinCondition
				&& reference == RdfMappingPackage.Literals.PARENT_TRIPLES_MAP_JOIN_CONDITION__CHILD) {
			return scopeForReferenceables(this.rootFinder.findRoot(context, Mapping.class));

		} else if (context instanceof ParentTriplesMapJoinCondition
				&& reference == RdfMappingPackage.Literals.PARENT_TRIPLES_MAP_JOIN_CONDITION__PARENT) {
			final ParentTriplesMapTerm term = this.rootFinder.findRoot(context, ParentTriplesMapTerm.class);
			if (term !=null && term.getMapping() != null && term.getMapping().getSource() != null) {
				return scopeForReferenceables(term.getMapping());				
			}
		}
		return super.getScope(context, reference);
	}

	private IScope scopeForReferenceables(Mapping mapping) {
		return Scopes.scopeFor(mapping.getSource().getReferenceables());
	}

}
