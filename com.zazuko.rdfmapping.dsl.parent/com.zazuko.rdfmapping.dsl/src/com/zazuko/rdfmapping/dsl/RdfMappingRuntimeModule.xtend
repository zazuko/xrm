/*
 * generated by Xtext 2.18.0
 */
package com.zazuko.rdfmapping.dsl

import com.google.inject.Injector
import com.google.inject.Provides
import com.zazuko.rdfmapping.dsl.generator.common.GeneratorConstants
import com.zazuko.rdfmapping.dsl.generator.rml.R2rmlDialect
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialect
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialectGenerator
import com.zazuko.rdfmapping.dsl.resource.RdfMappingResourceDescriptionStrategy
import com.zazuko.rdfmapping.dsl.services.RdfDslConverters
import javax.inject.Named
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class RdfMappingRuntimeModule extends AbstractRdfMappingRuntimeModule {

	@Provides
	@Named(GeneratorConstants.DIALECT_RML)
	def RmlDialectGenerator rmlDialectGenerator(Injector injector, RmlDialect dialect) {
		val RmlDialectGenerator result = new RmlDialectGenerator(dialect);
		injector.injectMembers(result);
		return result;
	}

	@Provides
	@Named(GeneratorConstants.DIALECT_R2RML)
	def RmlDialectGenerator r2rmlDialectGenerator(Injector injector, R2rmlDialect dialect) {
		val RmlDialectGenerator result = new RmlDialectGenerator(dialect);
		injector.injectMembers(result);
		return result;
	}

	override bindIValueConverterService() {
		return RdfDslConverters;
	}

	def Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return RdfMappingResourceDescriptionStrategy;
	}

}
