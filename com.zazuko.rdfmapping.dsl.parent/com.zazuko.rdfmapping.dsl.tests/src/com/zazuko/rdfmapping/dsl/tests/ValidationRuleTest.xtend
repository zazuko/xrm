package com.zazuko.rdfmapping.dsl.tests

import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import javax.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.eclipse.xtext.diagnostics.Severity

@ExtendWith(InjectionExtension)
@InjectWith(RdfMappingInjectorProvider)
class ValidationRuleTest {
	@Inject
	ParseHelper<Domainmodel> parseHelper
	@Inject ValidationTestHelper validationTester

	// Validation tests for the validation rules about the use of type
	@Test
	def void NoTypeDefined() {
		val result = parseHelper.parse('''
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			        source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			        referenceables {
			            P2 "RIC_P2"
			        }
			    }
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No type declared for the logical-source or source-group")
	}

	@Test
	def void TypeDefinedOnLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			    	type xml
			    	   source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			    	   referenceables {
			    	       P2 "RIC_P2"
			    	   }
			    }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void TypeDefinedOnSourceGroup() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			source-group WSD_STABS_SCP_ARCHIV_DBA {
				type xml
				   logical-source RECS_rico {
				       source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				       referenceables {
				           P2 "RIC_P2"
				       }
				   }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void TypeNotDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			logical-source RECS_rico {
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables {
					P2 "RIC_P2"
				}
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No type declared for the logical-source")
	}

	@Test
	def void TypeDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			logical-source RECS_rico {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables {
					P2 "RIC_P2"
				}
			}
		''')

		validationTester.assertNoErrors(result)
	}

	@Test
	def void TypeShadowed() {
		val result = parseHelper.parse('''
			source-types {
							xml referenceFormulation "ql:XPath"
							   rdb referenceFormulation "rr:SQL2008"
							   csv referenceFormulation "ql:CSV"
						}
				source-group WSD_STABS_SCP_ARCHIV_DBA {
				type rdb
			
			    logical-source RECS_rico {
				type xml
				       source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			        referenceables {
			            P2 "RIC_P2"
			        }
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
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			        type xml
			        referenceables {
			            P2 "RIC_P2"
			        }
			    }
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No source declared for the logical-source or source-group")
	}

	@Test
	def void SourceDefinedOnLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			source-group WSD_STABS_SCP_ARCHIV_DBA {
			    logical-source RECS_rico {
			    	type xml
			    	   source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			    	   referenceables {
			    	       P2 "RIC_P2"
			    	   }
			    }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void SourceDefinedOnSourceGroup() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			source-group WSD_STABS_SCP_ARCHIV_DBA {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				   logical-source RECS_rico {
				       referenceables {
				           P2 "RIC_P2"
				       }
				   }
			}
		''')

		validationTester.assertNoErrors(result);
	}

	@Test
	def void SourceNotDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			logical-source RECS_rico {
				type xml
				referenceables {
					P2 "RIC_P2"
				}
			}
		''')

		validationTester.assertError(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null,
			"No source declared for the logical-source")
	}

	@Test
	def void SourceDefinedOnNonEnclosedLogicalSource() {
		val result = parseHelper.parse('''
			source-types {
				xml referenceFormulation "ql:XPath"
				   rdb referenceFormulation "rr:SQL2008"
				   csv referenceFormulation "ql:CSV"
			}
			logical-source RECS_rico {
				type xml
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
				referenceables {
					P2 "RIC_P2"
				}
			}
		''')

		validationTester.assertNoErrors(result)
	}

	@Test
	def void SourceShadowed() {
		val result = parseHelper.parse('''
			source-types {
							xml referenceFormulation "ql:XPath"
							   rdb referenceFormulation "rr:SQL2008"
							   csv referenceFormulation "ql:CSV"
						}
				source-group WSD_STABS_SCP_ARCHIV_DBA {
				type rdb
				source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			    logical-source RECS_rico {
			        source "WSD_STABS_SCP_ARCHIV_DBA.RECS_rico"
			
			        referenceables {
			            P2 "RIC_P2"
			        }
			    }
			}
		''')

		validationTester.assertIssue(result, RdfMappingPackage.Literals.LOGICAL_SOURCE, null, Severity.WARNING,
			"Source declared on source-group level is shadowed by source declared on logical-source.")
	}
}
