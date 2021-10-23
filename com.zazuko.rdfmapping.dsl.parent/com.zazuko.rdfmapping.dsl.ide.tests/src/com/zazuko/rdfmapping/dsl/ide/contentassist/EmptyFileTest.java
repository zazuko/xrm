package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.junit.jupiter.api.Test;

public class EmptyFileTest extends AbstractServerTest {

	@Test
	public void testCompletion_01() {
		testCompletion((TestCompletionConfiguration it) -> {
			it.setModel("");
			String expectedCompletionItems =
					"package -> package [[0, 0] .. [0, 0]]\n" +
					"type -> type [[0, 0] .. [0, 0]]\n" +
					"Sample Snippet -> type ${1|A,B,C|} {\n" +
					"    \n" +
					"} [[0, 0] .. [0, 0]]\n";
			it.setExpectedCompletionItems(expectedCompletionItems);
		});
	}

}
