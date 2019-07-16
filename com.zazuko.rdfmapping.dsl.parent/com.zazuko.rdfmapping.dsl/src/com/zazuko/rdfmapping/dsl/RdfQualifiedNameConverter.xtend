package com.zazuko.rdfmapping.dsl

import org.eclipse.xtext.naming.IQualifiedNameConverter

class RdfQualifiedNameConverter extends IQualifiedNameConverter.DefaultImpl {
		
	override String getDelimiter() { 
			return ":";
	}
}