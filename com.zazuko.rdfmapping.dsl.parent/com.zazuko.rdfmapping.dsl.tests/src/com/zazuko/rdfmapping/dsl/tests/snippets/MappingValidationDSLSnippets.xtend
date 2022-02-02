package com.zazuko.rdfmapping.dsl.tests.snippets

class MappingValidationDSLSnippets {
	
	def duplicatedMappingName() '''
		output rml
		
		map Duplicated from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee.Employee
		
			properties
				employee.two from EMPNO;
		}

		map Duplicated from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id;
		
			types
				employee.Employee
		
			properties
				employee.two from EMPNO;
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
			prefix "employee." "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
				two
		}
	'''
	
	def nrOfArgumentsDiffer() '''
		output rml
		
		map Employee from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id EMPNO;
		
			types
				employee.Employee
		
			properties
				employee.two from EMPNO;
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
			prefix "employee." "http://example.com/employee"
		
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
				employee.Employee
		
			properties
				employee.one from EMPNO;
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
			prefix "employee." "http://example.com/employee"
		
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
				employee.Employee
		
			properties
				employee.one from EMPNO;
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
			prefix "employee." "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
		}
	'''
	
	def subjectAsLiteral() '''
		output rml
		
		map EmployeeMapping from EMPLOYEE {
			subject template "http://airport.example.com/{0}" with id as Literal;
		
			types
				employee.Employee
		
			properties
				employee.one from EMPNO;
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
			prefix "employee." "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
		}
	'''
	
	def graphMap_templateWithTermType() '''
		output r2rml
		
		map ThingIntoGraphmapsMapping2 from ThingIntoGraphmapsSource2 {
			subject template "http://example.org/thing/{0}" with id;
		
			graphs
				constant "http://example.org/graph/omnigraph";
				template "http://example.org/graph/thing-{0}" with id as Literal;
		}
		
		logical-source ThingIntoGraphmapsSource2 {
			type rdb
			source "THINGS"
		
			referenceables
				id
		}
	'''
}