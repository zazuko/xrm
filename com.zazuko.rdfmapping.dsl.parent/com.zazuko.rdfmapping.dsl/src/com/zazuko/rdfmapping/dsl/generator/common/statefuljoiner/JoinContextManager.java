package com.zazuko.rdfmapping.dsl.generator.common.statefuljoiner;

import java.util.ArrayList;
import java.util.List;

public class JoinContextManager implements IJoinContext {

	private final String defaultSeparator;
	private final String defaultPostfix;
	
	private List<JoinContext> children;
	private int counter;

	public JoinContextManager(String defaultSeparator, String defaultPostfix) {
		this.defaultSeparator = defaultSeparator;
		this.defaultPostfix = defaultPostfix;
		
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
	public IJoinContext newContext() {
		return this.newContext(this.defaultSeparator, this.defaultPostfix);
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
