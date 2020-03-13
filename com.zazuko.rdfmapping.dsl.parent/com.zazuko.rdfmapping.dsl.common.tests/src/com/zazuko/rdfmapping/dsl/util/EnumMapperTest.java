package com.zazuko.rdfmapping.dsl.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zazuko.rdfmapping.dsl.common.util.EnumMapper;
import com.zazuko.rdfmapping.dsl.common.util.EnumMapperBuilder;

public class EnumMapperTest {

	static enum Number {
		ONE, TWO, THREE, FOUR
	}

	private EnumMapper<Number> mapper;

	@Before
	public void before() {
		this.mapper = new EnumMapperBuilder<Number>() //
				.add("one", Number.ONE) //
				.add("two", Number.TWO) //
				.add("three", Number.THREE) //
				.build();
	}

	@Test
	public void toStringValue_ok() {
		Assert.assertEquals("one", this.mapper.toStringValue(Number.ONE));
	}

	@Test
	public void toStringValueNull_ok() {
		Assert.assertNull(this.mapper.toStringValue(null));
	}

	@Test
	public void toStringValue_fail() {
		try {
			this.mapper.toStringValue(Number.FOUR);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Unknown enum 'FOUR'. Known enums are [ ONE, TWO, THREE ]", e.getMessage());
		}
	}
	
	@Test
	public void toStringValue_fail_customException() {
		try {
			this.mapper.toStringValue(Number.FOUR, MapperTestException::new);
			Assert.fail();
		} catch (MapperTestException e) {
			Assert.assertEquals("Unknown enum 'FOUR'. Known enums are [ ONE, TWO, THREE ]", e.getMessage());
		}
	}

	@Test
	public void toEnum_ok() {
		Assert.assertEquals(Number.TWO, this.mapper.toEnum("two"));
	}

	@Test
	public void toEnumNull_ok() {
		Assert.assertNull(this.mapper.toEnum(null));
	}
	
	@Test
	public void toString_fail() {
		try {
			this.mapper.toEnum("four");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Unknown value 'four'. Known values are [ one, two, three ]", e.getMessage());
		}
	}
	
	@Test
	public void toString_fail_customException() {
		try {
			this.mapper.toEnum("four", MapperTestException::new);
			Assert.fail();
		} catch (MapperTestException e) {
			Assert.assertEquals("Unknown value 'four'. Known values are [ one, two, three ]", e.getMessage());
		}
	}
	
	static class MapperTestException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MapperTestException(String msg) {
			super(msg);
		}
	}

}
