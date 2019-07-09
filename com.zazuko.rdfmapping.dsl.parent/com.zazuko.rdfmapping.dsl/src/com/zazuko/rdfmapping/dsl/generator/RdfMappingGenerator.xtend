package com.zazuko.rdfmapping.dsl.generator

import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
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

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		val Iterable<Mapping> mappings = resource.allContents.filter(Mapping).toList
		
		if ( ! mappings.empty) {
			val String dslFileName = resource.getURI().lastSegment.toString();
			val String outFileBase = dslFileName.substring(0, dslFileName.lastIndexOf("."));
			
			val r2rmlGenerator = new RmlDialectGenerator(new R2rmlDialect);
			fsa.generateFile(outFileBase + '.r2rml.ttl', r2rmlGenerator.generateTurtle(mappings));
			
			val rmlGenerator = new RmlDialectGenerator(new RmlDialect);		
			fsa.generateFile(outFileBase + '.rml.ttl', rmlGenerator.generateTurtle(mappings));
		}
	}		
	
}
