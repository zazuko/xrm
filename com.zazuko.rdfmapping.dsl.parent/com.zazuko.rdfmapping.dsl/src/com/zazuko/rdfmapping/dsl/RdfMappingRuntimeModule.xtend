/*
 * generated by Xtext 2.18.0
 */
package com.zazuko.rdfmapping.dsl

import org.eclipse.xtext.naming.IQualifiedNameConverter

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class RdfMappingRuntimeModule extends AbstractRdfMappingRuntimeModule {
	def Class<? extends IQualifiedNameConverter> bindIQualifiedNameConverter() {
		return RdfQualifiedNameConverter;
	}
}
