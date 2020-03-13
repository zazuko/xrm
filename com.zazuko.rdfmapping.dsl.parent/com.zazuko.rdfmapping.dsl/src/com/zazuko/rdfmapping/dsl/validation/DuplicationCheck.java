package com.zazuko.rdfmapping.dsl.validation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.zazuko.rdfmapping.dsl.common.util.LazyMap;

public class DuplicationCheck<E> {

	public void check(Collection<E> all, Function<E, String> extractor, BiConsumer<String, E> acceptor) {
		LazyMap<String, List<E>> tmp = new LazyMap<>(new LinkedHashMap<>(), LinkedList::new);
		
		for (E e : all) {
			String key = extractor.apply(e);
			tmp.getOrInit(key).add(e);
		}
		
		for (Entry<String, List<E>> e : tmp.entrySet()) {
			if (e.getValue().size() > 1) {
				for (E duplicated : e.getValue()) {
					acceptor.accept(e.getKey(), duplicated);
				}
			}
		}
	}
}
