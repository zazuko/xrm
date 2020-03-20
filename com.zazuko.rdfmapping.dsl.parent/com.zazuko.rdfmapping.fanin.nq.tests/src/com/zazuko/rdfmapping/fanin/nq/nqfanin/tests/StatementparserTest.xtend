package com.zazuko.rdfmapping.fanin.nq.nqfanin.tests

import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.Statement
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.StatementParser
import java.nio.file.Files
import java.nio.file.Paths
import java.util.List
import org.junit.Assert
import org.junit.jupiter.api.Test

class StatementparserTest {

	extension StatementParser = new StatementParser();

	def private parse(CharSequence it) {
		return parse(0, toString);
	}
	
	@Test
	def void simpleStatement() {
		val Statement s = parse('''
			<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/creator> "Sean Bechhofer" <http://www.w3.org/2004/02/skos/core#> .
		''');
		
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core", s.subject?.value);
		Assert.assertEquals("http://purl.org/dc/terms/creator", s.predicate?.value);
		Assert.assertEquals("Sean Bechhofer", s.object?.value);
		Assert.assertNull(s.languageTag);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#", s.graphLabel?.value);
	}

	@Test
	def void escapedLiteral() {
		val Statement s = parse('''
			<http://www.w3.org/2004/02/skos/core#closeMatch> <http://www.w3.org/2004/02/skos/core#definition> "skos:closeMatch [...] of \"compound errors\" when [...]" <http://www.w3.org/2004/02/skos/core#> .
		''');
		
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#closeMatch", s.subject?.value);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#definition", s.predicate?.value);
		// no doing any un-escaping, but it has to be readable
		Assert.assertEquals('''skos:closeMatch [...] of \"compound errors\" when [...]'''.toString, s.object?.value);
		Assert.assertNull(s.languageTag);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#", s.graphLabel?.value);
	}

	@Test
	def void languageTag() {
		val Statement s = parse('''
			<http://www.w3.org/2004/02/skos/core#closeMatch> <http://www.w3.org/2004/02/skos/core#definition> "skos:closeMatch [...]"@en <http://www.w3.org/2004/02/skos/core#> .
		''');
		
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#closeMatch", s.subject?.value);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#definition", s.predicate?.value);
		// no doing any un-escaping, but it has to be readable
		Assert.assertEquals('''skos:closeMatch [...]'''.toString, s.object?.value);
		Assert.assertEquals("en", s.languageTag?.value);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#", s.graphLabel?.value);
	}
	
	@Test
	def void blanknodes() {
		val Statement s = parse('''
			_:c14n0 <http://www.w3.org/2002/07/owl#unionOf> _:c14n2 <http://www.w3.org/2004/02/skos/core#> .
		''');
		
		Assert.assertEquals("_:c14n0", s.subject?.value);
		Assert.assertEquals("http://www.w3.org/2002/07/owl#unionOf", s.predicate?.value);
		Assert.assertEquals("_:c14n2", s.object?.value);
		Assert.assertNull(s.languageTag);
		Assert.assertEquals("http://www.w3.org/2004/02/skos/core#", s.graphLabel?.value);
	}
	
	@Test
	def void parseAllLines_sqos() {
		val List<String> lines = Files.readAllLines(Paths.get("data", "skos", "skos.nq"));
		for (String line : lines) {
			parse(line);
		}
	}
	
	
}
