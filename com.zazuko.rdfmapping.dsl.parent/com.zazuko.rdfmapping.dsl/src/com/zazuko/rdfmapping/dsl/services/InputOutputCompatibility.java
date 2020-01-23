package com.zazuko.rdfmapping.dsl.services;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Enumerator;

import com.zazuko.rdfmapping.dsl.common.RdfMappingConstants;
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType;
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType;
import com.zazuko.rdfmapping.dsl.util.LazyMap;

public class InputOutputCompatibility {

	private final Set<SourceType> allSources;
	private final Set<OutputType> allOutputTypes;

	private final Map<OutputType, Set<SourceType>> out2Src;
	private final Map<SourceType, Set<OutputType>> src2Out;

	public InputOutputCompatibility() {
		this.allSources = all(SourceType.VALUES);
		this.allOutputTypes = all(OutputType.VALUES);

		{
			Map<SourceType, Set<OutputType>> tmp = new LinkedHashMap<>();
			// TODO SourceType.RDB?
			register(tmp, SourceType.XML, RdfMappingConstants.RMLISH_OUTPUTTYPES);
			register(tmp, SourceType.CSV, OutputType.CSVW);
			this.src2Out = Collections.unmodifiableMap(tmp);
		}
		{
			LazyMap<OutputType, Set<SourceType>> tmp = new LazyMap<>(new LinkedHashMap<>(), LinkedHashSet::new);
			for (Entry<SourceType, Set<OutputType>> entry : this.src2Out.entrySet()) {
				for (OutputType outputType : entry.getValue()) {
					tmp.getOrInit(outputType).add(entry.getKey());
				}
			}
			HashMap<OutputType, Set<SourceType>> tmp2 = new LinkedHashMap<>();
			for (Entry<OutputType, Set<SourceType>> entry : tmp.entrySet()) {
				tmp2.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
			}

			this.out2Src = Collections.unmodifiableMap(tmp2);
		}

	}

	private final <E> Set<E> all(List<E> values) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(values));
	}

	@SafeVarargs
	private final <S, T> void register(Map<S, Set<T>> tmp, S source, T... targets) {
		register(tmp, source, Arrays.asList(targets));
	}

	private final <S, T> void register(Map<S, Set<T>> tmp, S source, Collection<T> targets) {
		Set<T> values = new LinkedHashSet<>(targets.size());
		values.addAll(targets);
		tmp.put(source, Collections.unmodifiableSet(values));
	}

	public Set<SourceType> getCompatibleSourceTypes(OutputType type) {
		return this.out2Src.getOrDefault(type, this.allSources);
	}

	public Set<OutputType> getCompatibleOutputTypes(SourceType type) {
		return this.src2Out.getOrDefault(type, this.allOutputTypes);
	}
	
	public String serialize2Message(OutputType type) {
		return serialize2Message(Collections.singleton(type));
	}

	public String serialize2Message(Set<? extends Enumerator> in) {
		return in.stream().map(Enumerator::getLiteral).collect(Collectors.joining(", ", "[", "]"));
	}

	@SuppressWarnings("unchecked")
	public <R extends Enumerator> Set<R> extractFromMessage(String message, Class<R> clazz) {
		int start = message.indexOf("[");
		if (start < 0) {
			return null;
		}
		int end = message.indexOf("]");
		if (end < 0) {
			return null;
		}
		String fragment = message.substring(start + 1, end);
		String[] slices = fragment.split(",");
		Set<R> result = new LinkedHashSet<>();

		for (String slice : slices) {
			if (slice == null || "".equals(slice = slice.trim())) {
				continue;
			}
			try {
				result.add((R) clazz.getMethod("get", String.class).invoke(clazz, slice));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

		return result;

	}
}