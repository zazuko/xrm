package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateDeclaration;

public class EditorTest extends EditorTestProjectDrivenTest {

	public EditorTest() {
		super("editortest.xrm");
	}
	
	@Test
	public void getProposalOfTemplatesDefinedInThisFileTest() {
		MarkerContext ctx = this.marker("getProposalOfTemplatesDefinedInThisFile");
		PositionContext position = ctx.nextLineWithTextAtStart("airportIri");
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		b.ref("airportIri", TemplateDeclaration.class, position);
		b.ref("anyTemplate", TemplateDeclaration.class, position);
		b.ref("exampleIri", TemplateDeclaration.class, position);
		b.ref("UniqTemplate", TemplateDeclaration.class, position);
		b.string("templateValue", position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

}
