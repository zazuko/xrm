package com.zazuko.rdfmapping.dsl.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnumMapperTest {

	static enum Number {
		ONE, TWO, THREE, FOUR
	}

	private EnumMapper<Number> mapper;

	@BeforeEach
	public void before() {
		this.mapper = new EnumMapperBuilder<Number>() //
				.add("one", Number.ONE) //
				.add("two", Number.TWO) //
				.add("three", Number.THREE) //
				.build();
	}

	@Test
	public void toStringValue_ok() {
		Assertions.assertEquals("one", this.mapper.toStringValue(Number.ONE));
	}

	@Test
	public void toStringValueNull_ok() {
		Assertions.assertNull(this.mapper.toStringValue(null));
	}

	@Test
	public void toStringValue_fail() {
		try {
			this.mapper.toStringValue(Number.FOUR);
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals("Unknown enum 'FOUR'. Known enums are [ ONE, TWO, THREE ]", e.getMessage());
		}
	}
	
	@Test
	public void toStringValue_fail_customException() {
		try {
			this.mapper.toStringValue(Number.FOUR, MapperTestException::new);
			Assertions.fail();
		} catch (MapperTestException e) {
			Assertions.assertEquals("Unknown enum 'FOUR'. Known enums are [ ONE, TWO, THREE ]", e.getMessage());
		}
	}

	@Test
	public void toEnum_ok() {
		Assertions.assertEquals(Number.TWO, this.mapper.toEnum("two"));
	}

	@Test
	public void toEnumNull_ok() {
		Assertions.assertNull(this.mapper.toEnum(null));
	}
	
	@Test
	public void toString_fail() {
		try {
			this.mapper.toEnum("four");
			Assertions.fail();
		} catch (IllegalArgumentException e) {
			Assertions.assertEquals("Unknown value 'four'. Known values are [ one, two, three ]", e.getMessage());
		}
	}
	
	@Test
	public void toString_fail_customException() {
		try {
			this.mapper.toEnum("four", MapperTestException::new);
			Assertions.fail();
		} catch (MapperTestException e) {
			Assertions.assertEquals("Unknown value 'four'. Known values are [ one, two, three ]", e.getMessage());
		}
	}
	
	static class MapperTestException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MapperTestException(String msg) {
			super(msg);
		}
	}

}
