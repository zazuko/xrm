package com.zazuko.rdfmapping.dsl

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.util.SimpleAttributeResolver
import org.eclipse.xtext.util.Strings

class RdfQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {  
	
	IQualifiedNameConverter converter = new RdfQualifiedNameConverter();
	
	QualifiedName qualifiedNameFromConverter
	QualifiedName parentsQualifiedName
		
	override def QualifiedName computeFullyQualifiedNameFromNameAttribute(EObject obj) {
		val name = SimpleAttributeResolver.newResolver(String, "rdfname").apply(obj);
		if (Strings.isEmpty(name))
			return null;

		qualifiedNameFromConverter = converter.toQualifiedName(name);
		while (obj.eContainer() != null) {
			parentsQualifiedName = getFullyQualifiedName(obj.eContainer());
			if (parentsQualifiedName != null)
				return parentsQualifiedName.append(qualifiedNameFromConverter);
		}
		return qualifiedNameFromConverter;
	}
}