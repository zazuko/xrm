package com.zazuko.rdfmapping.dsl.tests.snippets

class DomainmodelDslSnippets {
	
	def duplicatedTemplateName() '''
		template foo "http://example.com/{0}"
		template foo "http://example.com/{0}"
	'''
}