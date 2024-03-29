package com.zazuko.rdfmapping.dsl.tests.snippets

class LogicalSourceDSLSnippets {
	def nullValueDeclarationOnXml() '''
		logical-source EMPLOYEE {
			type xml
			source "EMP"
			iterator "/Employees/Employee"
		
			referenceables
				id
				ENAME null "X"	
		}
	'''

	def withDialect(String type) '''
		logical-source EMPLOYEE {
			type «type»
			source "EMP"
			dialect MyDialect
			iterator "/Employees/Employee"
		
			referenceables
				id
		}
		
		dialect MyDialect {
			delimiter ","	
		}
	'''
	
	def withXmlNsExt(String type) '''
		logical-source EMPLOYEE {
			type «type»
			source "EMP"
			xml-namespace-extension SomeXmlNsExtension

			referenceables
				id
		}
		
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa" "http://www.example.com/audios/1.0/"
			prefix "exb" "http://www.example.com/books/1.0/"
		}
	'''
}