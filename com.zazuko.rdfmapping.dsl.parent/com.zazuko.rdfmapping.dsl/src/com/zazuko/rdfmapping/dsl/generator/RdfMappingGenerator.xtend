package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.generator.common.GeneratorConstants
import com.zazuko.rdfmapping.dsl.generator.common.ModelAccess
import com.zazuko.rdfmapping.dsl.generator.csvw.CsvwDialectContext
import com.zazuko.rdfmapping.dsl.generator.csvw.CsvwDialectGenerator
import com.zazuko.rdfmapping.dsl.generator.rml.RmlDialectGenerator
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceType
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
	extension ModelAccess
	
	@Inject
	ModelAccess modelAccess
	
	@Inject
	@Named(GeneratorConstants.DIALECT_RML)
	RmlDialectGenerator rmlGenerator

	@Inject
	@Named(GeneratorConstants.DIALECT_R2RML)
	RmlDialectGenerator r2rmlGenerator
	
	@Inject
	CsvwDialectGenerator csvwGenerator

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		val List<Mapping> mappings = resource.allContents.filter(Mapping).toList
		val List<Mapping> csvwMappings = mappings.filter[SourceType.CSV.equals(source.typeResolved)].toList
		
		val String dslFileName = resource.getURI().lastSegment.toString();
		val String outFileBase = dslFileName.substring(0, dslFileName.lastIndexOf("."));
			
		if ( ! mappings.empty) {
			fsa.generateFile(outFileBase + '.r2rml.ttl', r2rmlGenerator.generateTurtle(mappings));
			fsa.generateFile(outFileBase + '.rml.ttl', rmlGenerator.generateTurtle(mappings));
		}
		
		if ( ! csvwMappings.empty) {
			val CsvwDialectContext ctx = new CsvwDialectContext(modelAccess, csvwMappings);
			fsa.generateFile(outFileBase + '.csv.meta.json', csvwGenerator.generateJson(csvwMappings, ctx));		
		}
	}		
}
