package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable;

public class GraphMapsMappingTest extends EditorTestProjectDrivenTest {

	public GraphMapsMappingTest() {
		super("graphmaps-mapping.xrm");
	}

	@Test
	public void noAsOnTemplateValueInGraphMappingsTest() {
		MarkerContext ctx = this.marker("noAsOnTemplateValueInGraphMappings");
		PositionContext position = ctx.nextLineWithTextAfter("id");
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		b.ref("id", Referenceable.class, position);
		b.ref("someIri", Referenceable.class, position);
		b.keyword(";", position);
		// NOT b.keyword("as", position);

		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}
}
