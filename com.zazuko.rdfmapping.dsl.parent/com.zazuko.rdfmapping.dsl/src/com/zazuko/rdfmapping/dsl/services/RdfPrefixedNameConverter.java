package com.zazuko.rdfmapping.dsl.services;

import javax.inject.Inject;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.QualifiedNameValueConverter;
import org.eclipse.xtext.nodemodel.INode;

//see https://www.eclipse.org/forums/index.php/t/1099755/
public class RdfPrefixedNameConverter implements IValueConverter<String> {

	// delegate to defaultQNameValueConverter in order to keep default
	// behavior like escaping
	@Inject
	private QualifiedNameValueConverter delegate; 

	@Override
	public String toValue(String string, INode node) throws ValueConverterException {
		String defaultQName = string.replaceAll(":", ".");
		return this.delegate.toValue(defaultQName, node);
	}

	@Override
	public String toString(String value) throws ValueConverterException {
		String defaultQName = this.delegate.toString(value);
		return defaultQName.replace('.', ':');
	}

}