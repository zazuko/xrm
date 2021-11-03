package com.zazuko.rdfmapping.dsl.ide.debug;

public class Debugger {

	private static final String KEY = "xrmdebugging";
	private static final String VALUE_SYSERR = "syserr";
	
	// if launched integrated VS-code, this is visible in the tab "OUTPUT" for Xtext Server
	public static void debug(String message) {
		if (VALUE_SYSERR.equals(System.getProperty(KEY))) {
			System.err.println("############### " + message);
		}
	}
}
