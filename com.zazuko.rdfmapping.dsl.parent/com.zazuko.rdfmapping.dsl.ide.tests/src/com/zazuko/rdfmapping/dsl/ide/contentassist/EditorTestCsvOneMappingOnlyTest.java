package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;

public class EditorTestCsvOneMappingOnlyTest extends EditorTestProjectDrivenTest {

	public EditorTestCsvOneMappingOnlyTest() {
		super("editortest-csv-onemappingonly.xrm");
	}

	@Test
	public void noParentTriplesMapOnCsvTest() {
		MarkerContext ctx = this.marker("onCsvYiealdDialectButNotXmlNameSpaceExtension");
		PositionContext position = ctx.first();

		CompletionExpectationBuilder b = new CompletionExpectationBuilder();

		b.keyword("dialect", position);
		b.keyword("iterator", position);
		b.keyword("null", position);
		b.keyword("referenceables", position);
		b.keyword("source", position);

		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

}
