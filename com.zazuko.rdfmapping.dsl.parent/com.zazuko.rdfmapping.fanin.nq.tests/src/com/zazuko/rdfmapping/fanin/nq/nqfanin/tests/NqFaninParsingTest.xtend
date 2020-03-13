package com.zazuko.rdfmapping.fanin.nq.nqfanin.tests

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqThing
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.junit.Assert
import org.junit.jupiter.api.Test

class NqFaninParsingTest {
	
	extension NqFaninParseHelper = new NqFaninParseHelper();
	
	// for now, just enable capability for testing (will be used when reading real nq-files)
	@Test
	def void parseOneElement() {
		val NqThing thing = parse('''
			#FooBar
		''').extractOne(NqThing);
		
		Assert.assertNotNull(thing);
		Assert.assertEquals("FooBar", thing.name);
	}

	@Test
	def void parseManyElements() {
		val Resource res = parse('''
			#Foo
			#Bar
		''');
		
		val List<NqThing> things = res.contents.filter(NqThing).toList();
		
		var int i = 0;
		Assert.assertEquals("Foo", things.get(i++).name);
		Assert.assertEquals("Bar", things.get(i++).name);
	}
	
	@Test
	def void readExampleSkos() {
		val ResourceSet set = parseFolder("./data/skos");
		Assert.assertEquals(2, set.resources.size);
		
	}
}