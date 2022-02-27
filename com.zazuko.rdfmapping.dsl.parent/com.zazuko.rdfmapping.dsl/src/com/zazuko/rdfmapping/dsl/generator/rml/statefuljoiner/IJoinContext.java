package com.zazuko.rdfmapping.dsl.generator.rml.statefuljoiner;

public interface IJoinContext {

	
	IJoinContext newContext();
	
	IJoinContext newContext(String separator, String postfix);

	String postProcess(String orig);
	
	String acquireMarker();
}
