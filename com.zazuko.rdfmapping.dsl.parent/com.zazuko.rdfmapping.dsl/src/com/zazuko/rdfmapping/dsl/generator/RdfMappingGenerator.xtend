package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.generator.common.GeneratorConstants
import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.csvw.CsvwDialectContext
import com.zazuko.rdfmapping.dsl.generator.csvw.CsvwDialectGenerator
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialectGenerator
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputType
import java.util.List
import javax.inject.Inject
import javax.inject.Named
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class RdfMappingGenerator extends AbstractGenerator {

	@Inject
	ModelAccess modelAccess

	@Inject
	@Named(GeneratorConstants.DIALECT_RML)
	RmlDialectGenerator rmlGenerator

	@Inject
	@Named(GeneratorConstants.DIALECT_R2RML)
	RmlDialectGenerator r2rmlGenerator

	@Inject
	@Named(GeneratorConstants.DIALECT_CARML)
	RmlDialectGenerator carmlGenerator

	@Inject
	CsvwDialectGenerator csvwGenerator

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {

		val Domainmodel model = resource.contents.filter(Domainmodel).findFirst[true];
		val OutputType outputType = model?.outputType?.type;
		if (outputType === null) {
			return;
		}

		val List<Mapping> mappings = resource.allContents.filter(Mapping).toList;

		val String dslFileName = resource.getURI().lastSegment.toString();
		val String outFileBase = dslFileName.substring(0, dslFileName.lastIndexOf("."));

		switch (outputType) {
			case RML: {
				fsa.generateFile(outFileBase + '.rml.ttl', rmlGenerator.generateTurtle(mappings));
			}
			case R2RML: {
				fsa.generateFile(outFileBase + '.r2rml.ttl', r2rmlGenerator.generateTurtle(mappings));
			}
			case CARML: {
				fsa.generateFile(outFileBase + '.carml.ttl', carmlGenerator.generateTurtle(mappings));
			}
			case CSVW: {
				val CsvwDialectContext ctx = new CsvwDialectContext(modelAccess, mappings);
				fsa.generateFile(outFileBase + '.csv.meta.json', csvwGenerator.generateJson(mappings, ctx));
			}
		}

	}
}
