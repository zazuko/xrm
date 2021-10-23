package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty;
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
		b.ref("UniqTemplate", TemplateDeclaration.class, position); // also from other file #88
		b.string("templateValue", position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

	@Test
	public void refToRdfPropertThingSizeOfferedTest() {
		MarkerContext ctx = this.marker("refToRdfPropertThingSizeOffered");
		PositionContext position = ctx.first();
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		this.assertionMode(AssertEqualsMode.EXPECTED_FOUND_IN_ACTUAL);
		
		b.ref("thing.size", RdfProperty.class, position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

	@Test
	public void refToRdfPropertThingSizeOffered2Test() {
		MarkerContext ctx = this.marker("refToRdfPropertThingSizeOffered2");
		PositionContext position = ctx.first();
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		this.assertionMode(AssertEqualsMode.EXPECTED_FOUND_IN_ACTUAL);
		
		b.ref("thing.size", RdfProperty.class, position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

	@Test
	public void noProposalForMultiRefTest() {
		MarkerContext ctx = this.marker("noProposalForMultiRef");
		PositionContext position = ctx.nextLineWithTextAtStart("constant");
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		this.assertionMode(AssertEqualsMode.EXPECTED_ABSENT_IN_ACTUAL);
		
		b.keyword("multi-reference", position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}

}
