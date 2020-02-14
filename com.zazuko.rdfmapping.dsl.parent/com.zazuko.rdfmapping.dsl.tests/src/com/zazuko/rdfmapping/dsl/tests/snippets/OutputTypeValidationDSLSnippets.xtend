package com.zazuko.rdfmapping.dsl.tests.snippets

class OutputTypeValidationDSLSnippets {
	def outputType_propertiesmapping(String outputType, String properties) '''
			output «outputType»
			
			map EmployeeMapping from EMPLOYEE {
				subject template "http://airport.example.com/{0}" with id;
				
					types
						employee:Employee
				
					properties
					   «properties»
			}
			
			map EmployeeMapping2 from EMPLOYEE {
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
}