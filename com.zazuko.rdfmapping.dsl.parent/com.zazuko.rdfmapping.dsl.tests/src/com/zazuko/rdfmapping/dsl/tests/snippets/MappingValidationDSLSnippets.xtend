package com.zazuko.rdfmapping.dsl.tests.snippets

class MappingValidationDSLSnippets {
	
	def duplicatedMappingName() '''
		output rml
		
		map Duplicated from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee:Employee
		
			properties
				employee:two from EMPNO;
		}

		map Duplicated from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee:Employee
		
			properties
				employee:two from EMPNO;
		}
		
		logical-source EMPLOYEE {
			type xml
			source "EMP"
			iterator "/Employees/Employee"
		
			referenceables
				id
				EMPNO
		}
		
		vocabulary employee {
			prefix "employee:" "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
				two
		}
	'''

	def noOutputType() '''
		map EmployeeMapping from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee:Employee
		
			properties
				employee:one from EMPNO;
		}

		
		logical-source EMPLOYEE {
			type xml
			source "EMP"
			iterator "/Employees/Employee"
		
			referenceables
				id
				EMPNO
		}
		
		vocabulary employee {
			prefix "employee:" "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
		}
	'''

	def incompatibleOutputType() '''
		output csvw
		
		map EmployeeMapping from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee:Employee
		
			properties
				employee:one from EMPNO;
		}

		
		logical-source EMPLOYEE {
			type xml
			source "EMP"
			iterator "/Employees/Employee"
		
			referenceables
				id
				EMPNO
		}
		
		vocabulary employee {
			prefix "employee:" "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
		}
	'''
}