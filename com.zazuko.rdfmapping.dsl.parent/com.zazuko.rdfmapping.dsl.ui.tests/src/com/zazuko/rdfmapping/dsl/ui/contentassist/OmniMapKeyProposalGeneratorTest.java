package com.zazuko.rdfmapping.dsl.ui.contentassist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap;
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass;
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingFactory;

public class OmniMapKeyProposalGeneratorTest {

	@Inject
	private OmniMapKeyProposalGenerator gen;

	@Before
	public void before() {
		Guice.createInjector().injectMembers(this);
	}

	@Test
	public void danglingMap() {
		Set<String> actual = this.gen.createKeyProposals(RdfMappingFactory.eINSTANCE.createOmniMap());
		Assert.assertEquals(0, actual.size());
	}

	@Test
	public void rdfClass_empty() {
		Set<String> actual = this.gen.createKeyProposals(rdfClass());

		Assert.assertEquals(2, actual.size());

		List<String> actualList = new ArrayList<>(actual);
		int i = 0;
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_LABEL, actualList.get(i++));
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION, actualList.get(i++));
	}

	@Test
	public void rdfClass_labelInUse() {
		Set<String> actual = this.gen.createKeyProposals(rdfClass(RdfMappingConstants.OMNIMAP_KEY_LABEL));

		Assert.assertEquals(1, actual.size());

		List<String> actualList = new ArrayList<>(actual);
		int i = 0;
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION, actualList.get(i++));
	}

	@Test
	public void rdfClass_allInUse() {
		Set<String> actual = this.gen.createKeyProposals(
				rdfClass(RdfMappingConstants.OMNIMAP_KEY_LABEL, RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION));

		Assert.assertEquals(0, actual.size());
	}

	@Test
	public void rdfClass_labelInUseWithAdditional() {
		Set<String> actual = this.gen.createKeyProposals(rdfClass(RdfMappingConstants.OMNIMAP_KEY_LABEL, "foobar"));

		Assert.assertEquals(1, actual.size());

		List<String> actualList = new ArrayList<>(actual);
		int i = 0;
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION, actualList.get(i++));
	}

	@Test
	public void rdfClass_nullEntryAndStuff() {
		Set<String> actual = this.gen.createKeyProposals(rdfClass("foo", null, "foo ", "foo"));

		Assert.assertEquals(2, actual.size());

		List<String> actualList = new ArrayList<>(actual);
		int i = 0;
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_LABEL, actualList.get(i++));
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION, actualList.get(i++));
	}

	@Test
	public void rdfClass_labelInUseWithWhitespace() {
		Set<String> actual = this.gen.createKeyProposals(rdfClass(RdfMappingConstants.OMNIMAP_KEY_LABEL + " "));

		Assert.assertEquals(1, actual.size());

		List<String> actualList = new ArrayList<>(actual);
		int i = 0;
		Assert.assertEquals(RdfMappingConstants.OMNIMAP_KEY_DESCRIPTION, actualList.get(i++));
	}

	private OmniMap rdfClass(String... keys) {
		RdfClass clazz = RdfMappingFactory.eINSTANCE.createRdfClass();
		OmniMap result = RdfMappingFactory.eINSTANCE.createOmniMap();
		clazz.setOmniMap(result);
		if (keys != null) {
			for (String key : keys) {
				OmniMapEntry entry = RdfMappingFactory.eINSTANCE.createOmniMapEntry();
				result.getEntries().add(entry);
				entry.setKey(key);
				entry.setValue("value for " + key);
			}
		}
		return result;
	}

}
