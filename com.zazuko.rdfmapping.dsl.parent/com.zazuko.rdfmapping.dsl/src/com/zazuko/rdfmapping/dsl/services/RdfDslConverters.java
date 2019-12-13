package com.zazuko.rdfmapping.dsl.services;

import javax.inject.Inject;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;

public class RdfDslConverters extends DefaultTerminalConverters {

	@Inject
	private RdfPrefixedNameConverter rdfPrefixedNameConverter;

	@ValueConverter(rule = "RdfPrefixedName")
	public IValueConverter<String> RdfPrefixedName() {
		return rdfPrefixedNameConverter;
	}

}
