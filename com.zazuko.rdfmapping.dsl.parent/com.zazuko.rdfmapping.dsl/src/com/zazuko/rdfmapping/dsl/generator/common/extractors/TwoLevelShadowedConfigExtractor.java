package com.zazuko.rdfmapping.dsl.generator.common.extractors;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class TwoLevelShadowedConfigExtractor<C, P, V> {

	private final Function<C, V> childValueExtraction;
	private final Function<P, V> parentValueExtraction;
	private final Function<C, P> child2ParentNavigation;
	private Supplier<V> nullValueSupplier = () -> null;

	public TwoLevelShadowedConfigExtractor(Function<C, V> childValueExtraction, Function<P, V> parentValueExtraction,
			Function<C, P> toParentNavigation) {
		super();
		this.childValueExtraction = childValueExtraction;
		this.parentValueExtraction = parentValueExtraction;
		this.child2ParentNavigation = toParentNavigation;
	}
	

	public void setNullValueSupplier(Supplier<V> nullValueSupplier) {
		this.nullValueSupplier = nullValueSupplier;
	}

	public V extractC(C child) {
		if (child == null) {
			return this.nullValueSupplier.get();
		}
		V result = this.childValueExtraction.apply(child);
		if (result != null) {
			return result;
		}
		P parent = this.child2ParentNavigation.apply(child);
		return this.extractP(parent);
	}

	public V extractP(P parent) {
		return parent == null ? this.nullValueSupplier.get() : this.parentValueExtraction.apply(parent);
	}

}
