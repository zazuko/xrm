package com.zazuko.rdfmapping.dsl.tests

import com.zazuko.rdfmapping.dsl.common.RdfMappingValidationCodes
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.tests.snippets.LogicalSourceDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.MappingValidationDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.OutputTypeValidationDSLSnippets
import javax.inject.Inject
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

@ExtendWith(InjectionExtension)
@InjectWith(RdfMappingInjectorProvider)
class ValidationRuleTest {
	@Inject
	ParseHelper<Domainmodel> parseHelper
	@Inject ValidationTestHelper validationTester
	
	@Inject OutputTypeValidationDSLSnippets outputTypeSnippets
	@Inject MappingValidationDSLSnippets mappingSnippets
	@Inject LogicalSourceDSLSnippets logicalSourceSnippets

	// Validation tests for the validation rules about the use of type
	@Test
	def void NoTypeDefined() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			        source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			        referenceables
			            P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No type declared for the logical-source or source-group")
	}

	@Test
	def void TypeDefinedOnLogicalSource() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			    	type xml
			    	   source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			    	   referenceables
			    	       P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void TypeDefinedOnSourceGroup() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
				type xml
				   logical-source RECS_rico {
				       source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				       referenceables
				           P2 "RIC_P2"
				   }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void TypeNotDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			logical-source RECS_rico {
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables
					P2 "RIC_P2"
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No type declared for the logical-source")
	}

	@Test
	def void TypeDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			logical-source RECS_rico {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables
					P2 "RIC_P2"
			}
		''')

		validationTester.assertNoErrors(result)
	}

	@Test
	def void TypeShadowed() {
		val result = parseHelper.parse('''
				source-group WSD_STABS_SCP_ARCHIV_DBA {
				type rdb
			
			    logical-source RECS_rico {
				type xml
				       source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			        referenceables
			            P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertIssue(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null, Severity.WARNING,
			"Type declared on source-group level is shadowed by type declared on logical-source.")
	}

	// Validation tests for the validation rules about the use of source
	@Test
	def void NoSourceDefined() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			        type xml
			        referenceables
			            P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No source declared for the logical-source or source-group")
	}

	@Test
	def void SourceDefinedOnLogicalSource() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			    	type xml
			    	   source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			    	   referenceables
			    	       P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void SourceDefinedOnSourceGroup() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				   logical-source RECS_rico {
				       referenceables
				           P2 "RIC_P2"
				   }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void SourceNotDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			logical-source RECS_rico {
				type xml
				referenceables
					P2 "RIC_P2"
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No source declared for the logical-source")
	}

	@Test
	def void SourceDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			logical-source RECS_rico {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables
					P2 "RIC_P2"
			}
		''')

		validationTester.assertNoErrors(result)
	}

	@Test
	def void SourceShadowed() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
				type rdb
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			    logical-source RECS_rico {
			        source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			        referenceables
			            P2 "RIC_P2"
			    }
			}
		''')

		validationTester.assertIssue(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null, Severity.WARNING,
			"Source declared on source-group level is shadowed by source declared on logical-source.")
	}
	
	@Test
	def void rml_ok() {
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("rml", '''employee.one constant "42";'''));
		validationTester.assertNoErrors(result);
	}

	@Test
	def void rml_multiReference_onCarmlOnly() {
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("rml", '''employee.one multi-reference from EMPNO;'''));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.multiReferenceValuedTerm, 
			RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS_NOFIX, 
			"Not on output of type 'rml' - only valid on [carml]"
		);
	}
	
	@Test
	def void carml_multiReference_ok() {
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("carml", '''employee.one multi-reference from EMPNO;'''));
		validationTester.assertNoErrors(result);
	}
	
	@Test
	def void predicateObjectMapping_without_valuedTerm() {
		// valuedTerm is optional in grammar in order to enable quickfixes 
		//// (serialization new properties while editing PredicateObjectMapping )
 
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("rml", '''employee.one'''));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.predicateObjectMapping, 
			"missing", 
			"ValuedTerm missing"
		);
	}
	
	@Test
	def void rmp_parentMap_ok() {
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("rml", '''employee.one parent-map EmployeeMapping2;'''));
		
		validationTester.assertNoErrors(result);
	}
	
	@Test
	def void csv_parentMap_onRmlishOnly() {
		val result = parseHelper.parse(outputTypeSnippets.outputType_propertiesmapping("csvw", '''employee.one parent-map EmployeeMapping2;'''));
		
		validationTester.assertWarning(result, 
			RdfMappingPackage.eINSTANCE.parentTriplesMapTerm, 
			RdfMappingValidationCodes.EOBJECT_SUPERFLUOUS, 
			"Not on output of type 'csvw' - only valid on [rml, r2rml, carml]"
		);
	}
	
	@Test
	def void mapping_duplicatedMappingName() {
		val result = parseHelper.parse(mappingSnippets.duplicatedMappingName());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.mapping, 
			null, 
			"Mapping name already in use."
		);
	}

	@Test
	def void mapping_noOutputType() {
		val result = parseHelper.parse(mappingSnippets.noOutputType());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.mapping, 
			RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_MISSING, 
			"A mapping requires an outputType. Choose from [rml, carml]"
		);
	}

	@Test
	def void mapping_outputType_incompatible() {
		val result = parseHelper.parse(mappingSnippets.incompatibleOutputType());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.mapping, 
			RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE, 
			"Output of type csvw is incompatible. Expected one of [rml, carml]"
		);
	}
	
	@Test
	def void logicalSource_xml_withNullValueDeclaration() {
		val result = parseHelper.parse(logicalSourceSnippets.nullValueDeclarationOnXml());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.referenceable, 
			null, 
			"Type 'csv' required for null value declaration, but was 'xml"
		);
		
	}

	@Test
	def void outputtype_without_mapping() {
		val result = parseHelper.parse(outputTypeSnippets.outputTypeWithoutMapping());
		
		validationTester.assertWarning(result, 
			RdfMappingPackage.eINSTANCE.domainmodel, 
			RdfMappingValidationCodes.DOMAINMODEL_OUTPUTTYPE_SUPERFLUOUS, 
			"No outputType needed when no mapping is declared."
		);
		
	}
	
}
