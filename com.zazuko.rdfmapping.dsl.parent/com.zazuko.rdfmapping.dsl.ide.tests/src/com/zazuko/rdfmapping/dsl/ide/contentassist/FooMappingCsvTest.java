package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;

public class FooMappingCsvTest extends EditorTestProjectDrivenTest {

	public FooMappingCsvTest() {
		super("foo-mapping-csv.xrm");
	}

	@Test
	public void noAsOnTemplateValueInGraphMappingsTest() {
		MarkerContext ctx = this.marker("noGraphsOnCsv");
		PositionContext position = ctx.first();
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		// NOT b.keyword("graphs", position);
		b.keyword("}", position);
		b.keyword("properties", position);
		b.keyword("types", position);

		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}
}
