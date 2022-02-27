package com.zazuko.rdfmapping.dsl.generator.rml.statefuljoiner;

import java.util.LinkedList;
import java.util.List;

public class JoinContext implements IJoinContext {
	
	private final JoinContextManager parent;
	private final String separator;
	private final String postfix;
	private final List<String> markers;

	protected JoinContext(JoinContextManager parent, String separator, String postfix) {
		this.parent = parent;
		this.separator = separator;
		this.postfix = postfix;
		
		this.markers = new LinkedList<>();
	}
	

	@Override
	public IJoinContext newContext() {
		return this.parent.newContext();
	}

	@Override
	public IJoinContext newContext(String separator, String postfix) {
		return this.parent.newContext(separator, postfix);
	}

	@Override
	public String postProcess(String orig) {
		String text = orig;
		for (int a = 0; a < this.markers.size()-1;a++) {
			text = text.replaceFirst(this.markers.get(a), this.separator);
		}
		if (!this.markers.isEmpty()) {
			text = text.replaceFirst(this.markers.get(this.markers.size()-1), this.postfix);
			
		}
		return text;
	}

	@Override
	public String acquireMarker() {
		String marker = this.parent.acquireMarker();
		this.markers.add(marker);
		return marker;
	}

}
