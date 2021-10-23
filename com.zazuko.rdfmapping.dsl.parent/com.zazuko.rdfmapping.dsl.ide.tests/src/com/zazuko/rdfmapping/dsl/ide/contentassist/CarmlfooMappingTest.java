package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;

public class CarmlfooMappingTest extends EditorTestProjectDrivenTest {

	public CarmlfooMappingTest() {
		super("carmlfoo-mapping.xrm");
	}

	@Test
	public void xmlNamespaceExtensionButNotDialectTest() {
		MarkerContext ctx = this.marker("xmlNamespaceExtensionButNotDialect");
		PositionContext position = ctx.first();

		CompletionExpectationBuilder b = new CompletionExpectationBuilder();

		b.keyword("iterator", position);
		b.keyword("null", position);
		b.keyword("referenceables", position);

		// yield 'xml-namespace-extension', but not 'dialect'
		b.keyword("xml-namespace-extension", position);

		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});

	}

	@Test
	public void multiRefDueAndParentMapToCarmlTest() {
		MarkerContext ctx = this.marker("multiRefDueAndParentMapToCarml");
		PositionContext position = ctx.nextLineWithTextAfter("thing.weight");

		CompletionExpectationBuilder b = new CompletionExpectationBuilder();

		b.keyword("constant", position);
		b.keyword("from", position);

		// get 'multi-reference' in code completion (due to type carml)
		b.keyword("multi-reference", position);
		// get 'parent-map' in code completion (due to type carml)
		b.keyword("parent-map", position);

		b.keyword("template", position);

		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});

	}

}
