package com.zazuko.rdfmapping.dsl;

import javax.inject.Named;

import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.formatting2.IFormatter2;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.zazuko.rdfmapping.dsl.formatting2.RealRdfMappingFormatter;
import com.zazuko.rdfmapping.dsl.generator.common.GeneratorConstants;
import com.zazuko.rdfmapping.dsl.generator.rml.CarmlDialect;
import com.zazuko.rdfmapping.dsl.generator.rml.R2rmlDialect;
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialect;
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialectGenerator;
import com.zazuko.rdfmapping.dsl.resource.RdfMappingResourceDescriptionStrategy;
import com.zazuko.rdfmapping.dsl.services.RdfDslConverters;

/**
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */
public class RdfMappingRuntimeModule extends AbstractRdfMappingRuntimeModule {

	@Provides
	@Named(GeneratorConstants.DIALECT_RML)
	public RmlDialectGenerator rmlDialectGenerator(Injector injector, RmlDialect dialect) {
		RmlDialectGenerator result = new RmlDialectGenerator(dialect);
		injector.injectMembers(result);
		return result;
	}

	@Provides
	@Named(GeneratorConstants.DIALECT_R2RML)
	public RmlDialectGenerator r2rmlDialectGenerator(Injector injector, R2rmlDialect dialect) {
		RmlDialectGenerator result = new RmlDialectGenerator(dialect);
		injector.injectMembers(result);
		return result;
	}

	@Provides
	@Named(GeneratorConstants.DIALECT_CARML)
	public RmlDialectGenerator carmlDialectGenerator(Injector injector, CarmlDialect dialect) {
		RmlDialectGenerator result = new RmlDialectGenerator(dialect);
		injector.injectMembers(result);
		return result;
	}

	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return RdfDslConverters.class;
	}

	public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return RdfMappingResourceDescriptionStrategy.class;
	}

	@Override
	public Class<? extends IFormatter2> bindIFormatter2() {
		// TODO return RdfMappingFormatter.class; (means delete this method completely)
		return RealRdfMappingFormatter.class;
	}

}
