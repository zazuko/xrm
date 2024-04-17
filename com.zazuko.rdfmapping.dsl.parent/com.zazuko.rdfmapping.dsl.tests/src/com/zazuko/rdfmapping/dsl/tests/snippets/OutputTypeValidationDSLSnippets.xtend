package com.zazuko.rdfmapping.dsl.tests.snippets

class OutputTypeValidationDSLSnippets {
	def outputType_propertiesmapping(String outputType, String properties) '''
			output «outputType»
			
			map EmployeeMapping from EMPLOYEE {
				subject template "http://airport.example.com/{0}" with id;
				
					types
						employee.Employee
				
					properties
					   «properties»
			}
			
			map EmployeeMapping2 from EMPLOYEE {
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
		
	def outputType_subjectMapping(String outputType, String subjectMapping) '''
		output «outputType»
		
		map EmployeeMapping1 from EMPLOYEE1 {
			subject «subjectMapping»;
		}
		
		logical-source EMPLOYEE1 {
			type csv
			source "EMP"
		
			referenceables
				id
		}
	'''
	
	def outputTypeWithoutMapping() '''
		output csvw
		
		vocabulary employee {
			prefix "employee." "http://example.com/employee"
		
			classes
				Employee
		
			properties
				one
		}
	'''	
}