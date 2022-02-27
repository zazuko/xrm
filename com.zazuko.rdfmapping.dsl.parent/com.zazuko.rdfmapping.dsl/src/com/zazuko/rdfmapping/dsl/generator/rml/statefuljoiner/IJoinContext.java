package com.zazuko.rdfmapping.dsl.generator.rml.statefuljoiner;

public interface IJoinContext {

	
	default IJoinContext newContext() {
		return this.newContext(";", "");
	}
	
	IJoinContext newContext(String separator, String postfix);

	String postProcess(String orig);
	
	String acquireMarker();
}
