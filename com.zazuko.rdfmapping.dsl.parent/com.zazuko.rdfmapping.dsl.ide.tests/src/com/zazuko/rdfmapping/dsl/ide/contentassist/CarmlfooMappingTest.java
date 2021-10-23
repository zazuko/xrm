package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.EditorTestProjectDrivenTest.MarkerContext.PositionContext;

public class CarmlfooMappingTest extends EditorTestProjectDrivenTest {

	public CarmlfooMappingTest() {
		super("carmlfoo-mapping.xrm");
		}
	
	@Test
	public void xmlNamespaceExtensionButNotDialectTest() {
		MarkerContext ctx = this.getContext("xmlNamespaceExtensionButNotDialect");
		PositionContext position = ctx.first();
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		b.keyword("iterator", position);
		b.keyword("null", position);
		b.keyword("referenceables", position);
		
		// yield 'xml-namespace-extension', but not 'dialect'
		b.keyword("xml-namespace-extension", position);
		
		
		testCompletion((TestCompletionConfiguration it) -> {
			it.setModel(this.getFileContent());
			it.setLine(ctx.getLineNo());
			it.setColumn(0);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});

	}
	
 
}
