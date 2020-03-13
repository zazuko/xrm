package com.zazuko.rdfmapping.dsl.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumMapperBuilder<E> {

	private final Map<String, E> data = new LinkedHashMap<>();

	public EnumMapperBuilder<E> add(String key, E entry) {
		this.data.put(key, entry);
		return this;
	}

	public EnumMapper<E> build() {
		return new EnumMapper<E>(this.data);
	}
}
