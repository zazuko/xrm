package com.zazuko.rdfmapping.dsl.ui.labeling;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultHoverDocumentationProvider;

import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;

public class RdfHoverDocumentationProvider extends DefaultHoverDocumentationProvider {

	@Override
	public String getDocumentation(EObject object) {
		if (object instanceof RdfClass) {
			return toDocumentation((RdfClass) object);
		} else if (object instanceof RdfProperty) {
			return toDocumentation((RdfProperty) object);
		}

		return super.getDocumentation(object);
	}

	private String toDocumentation(RdfClass object) {
		return object.getDescription();
	}

	private String toDocumentation(RdfProperty object) {
		return object.getDescription();
	}

}
