package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;

public class CompletionExpectationBuilder {

	private final List<String> expectations = new LinkedList<>();

	public void keyword(String keyword, PositionContext position) {
		this.expectations.add(String.format("%1$s -> %1$s [%2$s .. %3$s]", keyword,
				position.toExpectationDescriptionLeft(), position.toExpectationDescriptionRight()));
	}

	public String toExpectedCompletionItems() {
		StringBuilder b = new StringBuilder();
		for (String current : expectations) {
			b.append(current);
			b.append('\n');
		}
		return b.toString();
	}

	public void ref(String name, Class<? extends EObject> clazz, PositionContext position) {
		// airportIri (TemplateDeclaration) -> airportIri [[19, 18] .. [19, 18]]
		this.expectations.add(String.format("%1$s (%2$s) -> %1$s [%3$s .. %4$s]", name, clazz.getSimpleName(),
				position.toExpectationDescriptionLeft(), position.toExpectationDescriptionRight()));

	}

	public void string(String string, PositionContext position) {
		// "templateValue" (STRING) -> "templateValue" [[19, 18] .. [19, 18]]
		this.expectations.add(String.format("\"%1$s\" (STRING) -> \"%1$s\" [%2$s .. %3$s]", string,
				position.toExpectationDescriptionLeft(), position.toExpectationDescriptionRight()));

	}
}
