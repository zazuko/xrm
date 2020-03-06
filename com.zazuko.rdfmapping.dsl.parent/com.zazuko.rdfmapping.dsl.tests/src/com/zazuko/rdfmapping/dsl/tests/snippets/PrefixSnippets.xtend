package com.zazuko.rdfmapping.dsl.tests.snippets

class PrefixSnippets {
	def labelWithSeparator() '''
		vocabulary foobar {
			prefix "employee:" "http://example.com/employee"
		
			classes
				SomeClass
		
			properties
				one
		}
	'''
}