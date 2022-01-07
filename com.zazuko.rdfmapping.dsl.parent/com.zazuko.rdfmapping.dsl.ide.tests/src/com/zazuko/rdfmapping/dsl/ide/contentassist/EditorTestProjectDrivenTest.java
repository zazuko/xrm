package com.zazuko.rdfmapping.dsl.ide.contentassist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.zazuko.rdfmapping.dsl.ide.contentassist.util.FileContext;
import com.zazuko.rdfmapping.dsl.ide.contentassist.util.MarkerContext;

public abstract class EditorTestProjectDrivenTest extends AbstractServerTest {

	private static final String PATH = "../../runtime-EclipseXtext/editor-test";

	private final FileContext fileContext;

	private AssertEqualsMode assertEqualsMode = AssertEqualsMode.IDENTITCAL;

	public EditorTestProjectDrivenTest(String fileName) {
		this.fileContext = new FileContext(PATH, fileName);
	}

	public FileContext getFileContext() {
		return this.fileContext;
	}

	public MarkerContext marker(String marker) {
		return this.fileContext.getMarker(marker);
	}

	public void assertionMode(AssertEqualsMode mode) {
		this.assertEqualsMode = mode;
	}

	@Override
	public void assertEquals(String expected, String actual) {
		switch (this.assertEqualsMode) {
		case IDENTITCAL:
			super.assertEquals(expected, actual);
			break;
		case EXPECTED_FOUND_IN_ACTUAL:
			assertExpectedInActual(expected, actual);
			break;
		case EXPECTED_ABSENT_IN_ACTUAL:
			assertMissingInActual(expected, actual);
			break;
		default:
			throw new RuntimeException(this.assertEqualsMode + "?");
		}
	}

	private void assertExpectedInActual(String expected, String actual) {
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.contains(expected),
				() -> String.format("expected to find '%s' in '%s'", expected, actual));
	}

	private void assertMissingInActual(String expected, String actual) {
		Assertions.assertNotNull(actual);
		Assertions.assertFalse(actual.contains(expected),
				() -> String.format("expected NOT to find '%s' in '%s'", expected, actual));
	}

	static enum AssertEqualsMode {
		IDENTITCAL, //
		/** if you don't want to list everything (e.g. too many references) */
		EXPECTED_FOUND_IN_ACTUAL, //
		/** only use if needed */
		EXPECTED_ABSENT_IN_ACTUAL, //
		;
	}


	// annotation in super-class is not seen by mvn build
	// --> re-bind JUnit5 annotation here
	// also don't forget to setup the IDE service in META-INF/services/org.eclipse.xtext.ISetup --> com.zazuko.rdfmapping.dsl.ide.RdfMappingIdeSetup
	@BeforeEach  
	public void setup() {
		super.setup();
	}

}
