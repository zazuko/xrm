package com.zazuko.rdfmapping.dsl.ide.contentassist;

import java.util.LinkedList;
import java.util.List;

import com.zazuko.rdfmapping.dsl.ide.contentassist.EditorTestProjectDrivenTest.MarkerContext.PositionContext;

public class CompletionExpectationBuilder {

	private final List<String> expectations = new LinkedList<>();

	public CompletionExpectationBuilder keyword(String keyword, PositionContext position) {
		this.expectations
				.add(String.format("%1$s -> %1$s [%2$s .. %2$s]", keyword, position.toExpectationDescription()));
		return this;
	}

	public String toExpectedCompletionItems() {
		StringBuilder b = new StringBuilder();
		for (String current : expectations) {
			b.append(current);
			b.append('\n');
		}
		return b.toString();
	}
}
