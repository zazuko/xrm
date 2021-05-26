package com.zazuko.rdfmapping.dsl.ui.labeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultHoverDocumentationProvider;

import com.google.inject.Inject;
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
import com.zazuko.rdfmapping.dsl.ui.contentassist.OmniMapKeyDefinition;

public class RdfHoverDocumentationProvider extends DefaultHoverDocumentationProvider {
	
	private static final Map<String, String> HTML_REPLACE;
	private static final String HTML_LINEBREAK = "<br />";
	
	static {
		Map<String, String> tmp = new LinkedHashMap<>();
		tmp.put(System.lineSeparator(), HTML_LINEBREAK);
		HTML_REPLACE = Collections.unmodifiableMap(tmp);
	}
	
	@Inject
	private OmniMapKeyDefinition omniMapKeyDefinition;

	@Override
	public String getDocumentation(EObject object) {
		if (object instanceof RdfClass) {
			return toDocumentation((RdfClass) object);
		} else if (object instanceof RdfProperty) {
			return toDocumentation((RdfProperty) object);
		} else if (object instanceof Datatype) {
			return toDocumentation((Datatype) object);
		}

		return super.getDocumentation(object);
	}

	private String toDocumentation(RdfClass object) {
		return toHtmlString(harvestOmniMap(object.getOmniMap()));
	}

	private String toDocumentation(RdfProperty object) {
		return toHtmlString(harvestOmniMap(object.getOmniMap()));
	}
	
	private String toDocumentation(Datatype object) {
		return toHtmlString(harvestOmniMap(object.getOmniMap()));
	}
	
	private String harvestOmniMap(OmniMap in) {
		if (in == null) {
			return null;
		}
		Map<String, OmniMapEntry> validEntries = new LinkedHashMap<>();
		for (OmniMapEntry candidate: in.getEntries()) {
			String key = trimToNull(candidate.getKey());
			String value = trimToNull(candidate.getValue());
			if (key != null && value != null) {
				validEntries.put(key, candidate);
			}
		}
		
		Set<String> knownKeysOrdered = this.omniMapKeyDefinition.knownKeys(in.eContainer());
		
		List<OmniMapEntry> normalizedEntries = new ArrayList<>(validEntries.size());

		// put known keys first in order the definition
		for (String knownKey : knownKeysOrdered) {
			OmniMapEntry e = validEntries.remove(knownKey);
			if (e != null) {
				normalizedEntries.add(e);
			}
		}
		
		// now the remaining entries
		normalizedEntries.addAll(validEntries.values());
		
		return normalizedEntries.stream().map(OmniMapEntry::getValue).collect(Collectors.joining(System.lineSeparator()+System.lineSeparator(), "", ""));
	}

	private String trimToNull(String in) {
		if (in == null) {
			return null;
		}
		String result = in.trim();
		return result.isEmpty() ? null : result;
	}

	// newlines might also be entered by the user, not only by harvestOmniMap
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
