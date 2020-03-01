package com.zazuko.rdfmapping.dsl.tests.snippets

class DomainmodelDslSnippets {
	
	def duplicatedTemplateName() '''
		template foo "http://example.com/{0}"
		template foo "http://example.com/{0}"
	'''

	def skippedKey() '''
		template foo "http://example.com/{0}/{2}/{4}"
	'''

	def notParseable() '''
		template foo "http://example.com/{0}/{b}/{1}"
	'''
}