package com.zazuko.rdfmapping.dsl.common.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapper<E> {

	private final Map<String, E> string2model;
	private final Map<E, String> model2string;

	protected EnumMapper(Map<String, E> tmp) {
		this.string2model = Collections.unmodifiableMap(tmp);

		Map<E, String> rev = new LinkedHashMap<>();
		for (Entry<String, E> current : tmp.entrySet()) {
			if (rev.get(current.getValue()) != null) {
				throw new RuntimeException("duplicated: " + current.getValue());
			}
			rev.put(current.getValue(), current.getKey());
		}
		this.model2string = Collections.unmodifiableMap(rev);
	}

	public E toEnum(String in) {
		return this.toEnum(in, this.defaultExceptionBuilder());
	}

	public E toEnum(String in, Function<String, ? extends RuntimeException> exceptionBuilder) {
		if (in == null) {
			return null;
		}
		E result = this.string2model.get(in);
		if (result == null) {
			String msg = String.format("Unknown value '%s'. Known values are %s", in,
					this.string2model.keySet().stream().collect(Collectors.joining(", ", "[ ", " ]")));
			throw exceptionBuilder.apply(msg);
		}
		return result;
	}

	public String toStringValue(E in) {
		return this.toStringValue(in, this.defaultExceptionBuilder());
	}

	public String toStringValue(E in, Function<String, ? extends RuntimeException> exceptionBuilder) {
		if (in == null) {
			return null;
		}
		String result = this.model2string.get(in);
		if (result == null) {
			String msg = String.format("Unknown enum '%s'. Known enums are %s", in, this.model2string.keySet().stream()
					.map(String::valueOf).collect(Collectors.joining(", ", "[ ", " ]")));
			throw exceptionBuilder.apply(msg);
		}
		return result;
	}

	private Function<String, ? extends RuntimeException> defaultExceptionBuilder() {
		return IllegalArgumentException::new;
	}

}
