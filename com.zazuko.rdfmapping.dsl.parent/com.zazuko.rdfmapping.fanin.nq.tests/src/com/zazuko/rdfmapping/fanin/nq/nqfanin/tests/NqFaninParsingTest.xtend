package com.zazuko.rdfmapping.fanin.nq.nqfanin.tests

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqClass
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqNameAware
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqProperty
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqVocabulary
import java.util.Collection
import java.util.StringTokenizer
import org.eclipse.emf.ecore.resource.ResourceSet
import org.junit.Assert
import org.junit.jupiter.api.Test

class NqFaninParsingTest {
	
	extension NqFaninParseHelper = new NqFaninParseHelper();
	
	def skosCommon() '''
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/contributor> "Dave Beckett" <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/contributor> "Nikki Rogers" <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/contributor> "Participants in W3C's Semantic Web Deployment Working Group." <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/creator> "Alistair Miles" <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/creator> "Sean Bechhofer" <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/description> "An RDF vocabulary for describing the basic structure and content of concept schemes such as thesauri, classification schemes, subject heading lists, taxonomies, 'folksonomies', other types of controlled vocabulary, and also concept schemes embedded in glossaries and terminologies."@en <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://purl.org/dc/terms/title> "SKOS Vocabulary"@en <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Ontology> <http://www.w3.org/2004/02/skos/core#> .
		<http://www.w3.org/2004/02/skos/core> <http://www.w3.org/2000/01/rdf-schema#seeAlso> <http://www.w3.org/TR/skos-reference/> <http://www.w3.org/2004/02/skos/core#> .
	'''
	
	@Test
	def void parseClass() {
		val NqVocabulary vocabulary = parse('''
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Class> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/2000/01/rdf-schema#isDefinedBy> <http://www.w3.org/2004/02/skos/core> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/2000/01/rdf-schema#label> "Ordered Collection"@en <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://www.w3.org/2004/02/skos/core#Collection> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/2004/02/skos/core#definition> "An ordered collection of concepts, where both the grouping and the ordering are meaningful."@en <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#OrderedCollection> <http://www.w3.org/2004/02/skos/core#scopeNote> "Ordered collections can be used where you would like a set of concepts to be displayed in a specific order, and optionally under a 'node label'."@en <http://www.w3.org/2004/02/skos/core#> .
			
			«skosCommon»
		''').extractOne(NqVocabulary);
		
		Assert.assertNotNull(vocabulary);
		Assert.assertEquals("testskos", vocabulary.label);
		Assert.assertNotNull(vocabulary.getNqClass("OrderedCollection"));
	}

	def NqClass getNqClass(NqVocabulary it, String name) {
		return classes.findFirst[c | c.name.equals(name)];
	}
	@Test
	def void parseProperty() {
		val NqVocabulary vocabulary = parse('''
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#DatatypeProperty> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/2000/01/rdf-schema#isDefinedBy> <http://www.w3.org/2004/02/skos/core> <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/2000/01/rdf-schema#label> "notation"@en <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/2004/02/skos/core#definition> "A notation, also known as classification code, is a string of characters such as \"T58.5\" or \"303.4833\" used to uniquely identify a concept within the scope of a given concept scheme."@en <http://www.w3.org/2004/02/skos/core#> .
			<http://www.w3.org/2004/02/skos/core#notation> <http://www.w3.org/2004/02/skos/core#scopeNote> "By convention, skos:notation is used with a typed literal in the object position of the triple."@en <http://www.w3.org/2004/02/skos/core#> .
			
			«skosCommon»
		''').extractOne(NqVocabulary);
		
		Assert.assertNotNull(vocabulary);
		Assert.assertEquals("testskos", vocabulary.label);
		Assert.assertNotNull(vocabulary.getNqProperty("notation"));
	}
	
	def NqProperty getNqProperty(NqVocabulary it, String name) {
		return properties.findFirst[c | c.name.equals(name)];
	}
	
	@Test
	def void readExampleSkos() {
		val ResourceSet set = parseFolder("./data/skos");
		Assert.assertEquals(1, set.resources.size);
		
		val NqVocabulary voca = set.resources.get(0).extractOne(NqVocabulary);
		
		voca.consumeClass("Collection");
		voca.consumeClass("Concept");
		voca.consumeClass("ConceptScheme");
		voca.consumeClass("OrderedCollection");
		
		Assert.assertEquals(28, voca.properties.size);
		val StringTokenizer tokenizer = new StringTokenizer(allSkosProperties.toString);
		while (tokenizer.hasMoreTokens) {
			voca.consumeProperty(tokenizer.nextToken);
		} 
		
		Assert.assertEquals(voca.properties.debugString, 0, voca.properties.size);
		Assert.assertEquals(voca.classes.debugString, 0, voca.classes.size);
	}
	
	def allSkosProperties() '''altLabel broadMatch broader broaderTransitive changeNote closeMatch definition editorialNote exactMatch example hasTopConcept hiddenLabel historyNote inScheme mappingRelation member memberList narrowMatch narrower narrowerTransitive notation note prefLabel related relatedMatch scopeNote semanticRelation topConceptOf'''
	
	def private String debugString(Collection<? extends NqNameAware> it) {
		val StringBuilder b = new StringBuilder();
		for (NqNameAware current : it) {
			b.append(current.name);
			b.append(" ");
		}
		return b.toString();
	}
	
	def private void consumeClass(NqVocabulary it, String name) {
		val NqClass found = getNqClass(name);
		Assert.assertNotNull(name + " not found", found);
		classes.remove(found);
	}
	def private void consumeProperty(NqVocabulary it, String name) {
		val NqProperty  found = getNqProperty(name);
		Assert.assertNotNull(name + " not found", found);
		properties.remove(found);
	}
	
}