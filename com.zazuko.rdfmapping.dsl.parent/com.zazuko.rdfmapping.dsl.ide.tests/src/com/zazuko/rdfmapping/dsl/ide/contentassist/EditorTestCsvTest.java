package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.PositionContext;
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable;

public class EditorTestCsvTest extends EditorTestProjectDrivenTest {

	public EditorTestCsvTest() {
		super("editortest-csv.xrm");
	}
	
	@Test
	public void noParentTriplesMapOnCsvTest() {
		MarkerContext ctx = this.marker("noParentTriplesMapOnCsv");
		PositionContext position = ctx.nextLineWithTextAfter("employeeEditorTestCsv.no");
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		this.assertionMode(AssertEqualsMode.EXPECTED_ABSENT_IN_ACTUAL);
		
		b.keyword("parent-map", position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}
	
	@Test
	public void noAsAfterEMPNOTest() {
		MarkerContext ctx = this.marker("noAsAfterEMPNO");
		PositionContext position = ctx.nextLineWithTextAtEnd("EMPNO");
		
		CompletionExpectationBuilder b = new CompletionExpectationBuilder();
		
		b.ref("EMPNO", Referenceable.class, position.spanLeftForLengthOf("EMPNO"));
		// TODO why is 'id' offered in eclipse but not in LSP?
		//b.ref("id", Referenceable.class, position);
		
		// NOT 'as'
		// NOT b.keyword("as", position);
		
		testCompletion((TestCompletionConfiguration it) -> {
			position.configure(it);
			it.setExpectedCompletionItems(b.toExpectedCompletionItems());
		});
	}
}
