package com.zazuko.rdfmapping.dsl.services;

import jakarta.inject.Inject;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.QualifiedNameValueConverter;
import org.eclipse.xtext.nodemodel.INode;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;

//see https://www.eclipse.org/forums/index.php/t/1099755/
public class RdfPrefixedNameConverter implements IValueConverter<String> {

	// delegate to defaultQNameValueConverter in order to keep default
	// behavior like escaping
	@Inject
	private QualifiedNameValueConverter delegate;

	@Override
	public String toValue(String string, INode node) throws ValueConverterException {
		String defaultQName = string.replaceAll(RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX,
				RdfMappingConstants.TOKEN_QNAME_SEPARATOR_DEFAULT);
		return this.delegate.toValue(defaultQName, node);
	}

	@Override
	public String toString(String value) throws ValueConverterException {
		String defaultQName = this.delegate.toString(value);
		return defaultQName.replace(RdfMappingConstants.TOKEN_QNAME_SEPARATOR_DEFAULT_CHAR,
				RdfMappingConstants.TOKEN_QNAME_SEPARATOR_RDFPREFIX_CHAR);
	}

}