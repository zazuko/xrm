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
}