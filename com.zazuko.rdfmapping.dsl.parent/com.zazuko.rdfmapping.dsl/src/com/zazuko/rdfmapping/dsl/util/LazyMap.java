package com.zazuko.rdfmapping.dsl.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class LazyMap<K, V> implements Map<K, V> {

	private Map<K, V> backingMap;
	private Supplier<V> factory;

	public LazyMap(Map<K, V> backingMap, Supplier<V> factory) {
		this.backingMap = backingMap;
		this.factory = factory;
	}

	public V getOrInit(K key) {
		V result = this.backingMap.get(key);
		if (result == null) {
			result = this.factory.get();
			this.put(key, result);
		}
		return result;
	}

	@Override
	public V get(Object key) {
		return this.backingMap.get(key);
	}

	@Override
	public int size() {
		return this.backingMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.backingMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.backingMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.backingMap.containsKey(value);
	}

	@Override
	public V put(K key, V value) {
		return this.backingMap.put(key, value);
	}

	@Override
	public V remove(Object key) {
		return this.backingMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		this.backingMap.putAll(m);
	}

	@Override
	public void clear() {
		this.backingMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.backingMap.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.backingMap.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.backingMap.entrySet();
	}

	@Override
	public String toString() {
		return "LazyMap [backingMap=" + backingMap + ", factory=" + factory + "]";
	}
}