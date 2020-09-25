package com.zazuko.rdfmapping.dsl.ui.labeling;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultHoverDocumentationProvider;

import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;

public class RdfHoverDocumentationProvider extends DefaultHoverDocumentationProvider {
	
	private static final Map<String, String> HTML_REPLACE;
	private static final String HTML_LINEBREAK = "<br />";
	
	static {
		Map<String, String> tmp = new LinkedHashMap<>();
		tmp.put(System.lineSeparator(), HTML_LINEBREAK);
		HTML_REPLACE = Collections.unmodifiableMap(tmp);
	}

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
		return toHtmlString(harvestOmniMap(object.getOmniMap()));
	}

	private String toDocumentation(RdfProperty object) {
		return toHtmlString(harvestOmniMap(object.getOmniMap()));
	}
	
	private String harvestOmniMap(OmniMap in) {
		if (in == null) {
			return null;
		}
		return in.getEntries().stream()
				.map(OmniMapEntry::getValue)
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.joining(HTML_LINEBREAK + HTML_LINEBREAK, "", ""));
	}

	private String toHtmlString(String in) {
		if (in == null) {
			return null;
		}
		for (Entry<String, String> entry : HTML_REPLACE.entrySet()) {
			in = in.replaceAll(entry.getKey(), entry.getValue());
		}
		return in;
	}

}
