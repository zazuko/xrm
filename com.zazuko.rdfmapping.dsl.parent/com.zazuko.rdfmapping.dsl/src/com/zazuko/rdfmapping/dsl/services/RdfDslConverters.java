package com.zazuko.rdfmapping.dsl.services;

import javax.inject.Inject;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;

public class RdfDslConverters extends DefaultTerminalConverters {

	@Inject
	private RdfPrefixedNameConverter rdfPrefixedNameConverter;

	@ValueConverter(rule = "RdfPrefixedName")
	public IValueConverter<String> RdfPrefixedName() {
		return rdfPrefixedNameConverter;
	}

	// see https://www.eclipse.org/forums/index.php/t/1099755/
	public static class RdfPrefixedNameConverter implements IValueConverter<String> {

		@Override
		public String toValue(String string, INode node) throws ValueConverterException {
			return string.replaceAll(":", ".");
		}

		@Override
		public String toString(String value) throws ValueConverterException {
			return value.replace('.', ':');
		}

	}

}
