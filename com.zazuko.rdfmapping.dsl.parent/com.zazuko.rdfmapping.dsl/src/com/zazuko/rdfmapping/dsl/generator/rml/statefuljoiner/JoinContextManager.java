package com.zazuko.rdfmapping.dsl.generator.rml.statefuljoiner;

import java.util.ArrayList;
import java.util.List;

public class JoinContextManager implements IJoinContext {

	private List<JoinContext> children;
	private int counter;

	public JoinContextManager() {
		this.children = new ArrayList<>();
	}

	@Override
	public String postProcess(String orig) {
		String result = orig;
		for (JoinContext child : this.children) {
			result = child.postProcess(result);
		}
		return result;
	}

	@Override
	public IJoinContext newContext(String separator, String postfix) {
		JoinContext result = new JoinContext(this, separator, postfix);
		this.children.add(result);
		return result;
	}

	@Override
	public String acquireMarker() {
		return "###replaceme___" + this.counter++ + "___replaceme###";
	}
}
