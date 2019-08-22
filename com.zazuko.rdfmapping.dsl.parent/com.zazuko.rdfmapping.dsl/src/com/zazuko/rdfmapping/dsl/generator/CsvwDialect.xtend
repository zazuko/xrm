package com.zazuko.rdfmapping.dsl.generator

class CsvwDialect {
	
	def dialect() '''
		"dialect": {
			"delimiter": ";"
		},
	'''
	
	def context() '''
		"@context": "http://www.w3.org/ns/csvw",
	'''
}
