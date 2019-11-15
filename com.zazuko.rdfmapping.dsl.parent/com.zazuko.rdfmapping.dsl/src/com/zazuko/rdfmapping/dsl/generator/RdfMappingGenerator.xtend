package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

import static extension com.zazuko.rdfmapping.dsl.generator.ModelAccess.typeResolved

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class RdfMappingGenerator extends AbstractGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		val List<Mapping> mappings = resource.allContents.filter(Mapping).toList
		val List<Mapping> csvwMappings = mappings.filter[source.typeResolved?.name == 'csv'].toList
		
		val String dslFileName = resource.getURI().lastSegment.toString();
		val String outFileBase = dslFileName.substring(0, dslFileName.lastIndexOf("."));
			
		if ( ! mappings.empty) {
			val RmlDialectGenerator r2rmlGenerator = new RmlDialectGenerator(new R2rmlDialect);
			fsa.generateFile(outFileBase + '.r2rml.ttl', r2rmlGenerator.generateTurtle(mappings));
			
			val RmlDialectGenerator rmlGenerator = new RmlDialectGenerator(new RmlDialect);		
			fsa.generateFile(outFileBase + '.rml.ttl', rmlGenerator.generateTurtle(mappings));
		}
		
		if ( ! csvwMappings.empty) {
			val CsvwDialectContext ctx = new CsvwDialectContext(csvwMappings);
			val CsvwDialectGenerator generator = new CsvwDialectGenerator(new CsvwDialect);		
			fsa.generateFile(outFileBase + '.csv.meta.json', generator.generateJson(csvwMappings, ctx));		
		}
	}		
}
