package com.zazuko.rdfmapping.dsl.tests

import com.zazuko.rdfmapping.dsl.common.RdfMappingValidationCodes
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.tests.snippets.DomainmodelDslSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.LogicalSourceDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.MappingValidationDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.OutputTypeValidationDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.PrefixSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.SourceGroupDSLSnippets
import com.zazuko.rdfmapping.dsl.tests.snippets.XmlNamespaceExtensionDSLSnippets
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
	@Inject SourceGroupDSLSnippets sourceGroupSnippets
	@Inject XmlNamespaceExtensionDSLSnippets xmlNamespaceExtensionSnippets
	@Inject DomainmodelDslSnippets domainmodelDslSnippets
	@Inject PrefixSnippets prefixSnippets


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
	def void mapping_templateNrOfArguments_differ() {
		val result = parseHelper.parse(mappingSnippets.nrOfArgumentsDiffer());
		
		validationTester.assertWarning(result, 
			RdfMappingPackage.eINSTANCE.templateValuedTerm, 
			null, 
			"Pattern 'http://airport.example.com/{0}' requires 1 argument(s), but there are 2"
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
	def void mapping_subjectAsLiteral() {
		val result = parseHelper.parse(mappingSnippets.subjectAsLiteral());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.termTypeRef, 
			null, 
			"Literal is invalid on the subject"
		);
	}
	
	@Test
	def void mapping_graphMap_templateWithTermType() {
		val result = parseHelper.parse(mappingSnippets.graphMap_templateWithTermType());

		validationTester.assertError(result,
			RdfMappingPackage.eINSTANCE.termTypeRef,
			null,
			"TermType specification not valid for Graphmap"
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

	@Test
	def void xmlNsExtension_ok() {
		val result = parseHelper.parse(xmlNamespaceExtensionSnippets.ok());
		
		validationTester.assertNoErrors(result);
	}
	
	@Test
	def void xmlNsExtension_label_withSeparator() {
		val result = parseHelper.parse(xmlNamespaceExtensionSnippets.separatorInLabel());
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.prefix, 
			RdfMappingValidationCodes.PREFIX_LABEL_SEPARATOR, 
			"No separator characters allowed"
		);
	}

	@Test
	def void xmlNsExtension_label_duplicated() {
		val Domainmodel result = parseHelper.parse(xmlNamespaceExtensionSnippets.duplicatedPrefix());
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.prefix, 
			null, 
			"DuplicatedLabel"
		);
	}
	
	@Test
	def void xmlNsExtensionRef_ok() {
		val result = parseHelper.parse(xmlNamespaceExtensionSnippets.outputAndXmlNamespaceExtensionRef("carml", true));
		
		validationTester.assertNoErrors(result);
	}

	@Test
	def void xmlNsExtensionRef_outputRml_fail() {
		val result = parseHelper.parse(xmlNamespaceExtensionSnippets.outputAndXmlNamespaceExtensionRef("rml", true));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.mapping, 
			RdfMappingValidationCodes.MAPPING_OUTPUTTYPE_INCOMPATIBLE, 
			"Source with xml-namespace-extension requires OutputType [carml]"
		);
	}
	
	@Test
	def void logicalSource_dialect_onCSV_ok() {
		val result = parseHelper.parse(logicalSourceSnippets.withDialect("csv"));
		
		validationTester.assertNoErrors(result);
	}

	@Test
	def void logicalSource_dialect_onXML() {
		val result = parseHelper.parse(logicalSourceSnippets.withDialect("xml"));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.logicalSource, 
			null,
			"Dialect is for sourceType CSV only"
		);
	}
	
	@Test
	def void logicalSource_xmlNsExt_onXML_ok() {
		val result = parseHelper.parse(logicalSourceSnippets.withXmlNsExt("xml"));
		
		validationTester.assertNoErrors(result);
	}

	@Test
	def void logicalSource_xmlNsExt_onCSV() {
		val result = parseHelper.parse(logicalSourceSnippets.withXmlNsExt("csv"));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.logicalSource, 
			null,
			"xml-namespace-extension is for sourceType XML only"
		);
	}

	@Test
	def void sourceGroup_dialect_onCSV_ok() {
		val result = parseHelper.parse(sourceGroupSnippets.withDialect("csv"));
		
		validationTester.assertNoErrors(result);
	}

	@Test
	def void sourceGroup_dialect_onXML() {
		val result = parseHelper.parse(sourceGroupSnippets.withDialect("xml"));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.sourceGroup, 
			null,
			"Dialect is for sourceType CSV only"
		);
	}

	@Test
	def void sourceGroup_xmlNsExt_onXML_ok() {
		val result = parseHelper.parse(sourceGroupSnippets.withXmlNsExt("xml"));
		
		validationTester.assertNoErrors(result);
	}

	@Test
	def void sourceGroup_xmlNsExt_onCSV() {
		val result = parseHelper.parse(sourceGroupSnippets.withXmlNsExt("csv"));
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.sourceGroup, 
			null,
			"xml-namespace-extension is for sourceType XML only"
		);
	}
	
	@Test
	def void domainmodel_templatedeclaration_duplicated() {
		val result = parseHelper.parse(domainmodelDslSnippets.duplicatedTemplateName);
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.templateDeclaration, 
			null, 
			"Declaration name already in use: foo"
		);
	}

	@Test
	def void domainmodel_templatedeclarationValue_skippedKey() {
		val result = parseHelper.parse(domainmodelDslSnippets.skippedKey);
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.templateValue, 
			null, 
			"Pattern invalid, skipped keys [1, 3]"
		);
	}

	@Test
	def void domainmodel_templatedeclarationValue_notParsable() {
		val result = parseHelper.parse(domainmodelDslSnippets.notParseable);
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.templateValue, 
			null,
			"Pattern invalid: can't parse argument number: b"
		);
	}

	@Test
	def void prefix_labelWithSeparator() {
		val result = parseHelper.parse(prefixSnippets.labelWithSeparator);
		
		validationTester.assertError(result, 
			RdfMappingPackage.eINSTANCE.prefix, 
			RdfMappingValidationCodes.PREFIX_LABEL_SEPARATOR,
			"No separator characters allowed"
		);
		
	}
}
