package com.zazuko.rdfmapping.dsl.tests.snippets

class SourceGroupDSLSnippets {

	def withDialect(String type) '''
		source-group MyGroup {
			type «type»
			dialect MyDialect
		}
		
		dialect MyDialect {
			delimiter ","	
		}
	'''

	def withXmlNsExt(String type) '''
		source-group MyGroup {
			type «type»
			xml-namespace-extension SomeXmlNsExtension
		}
		
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa" "http://www.example.com/audios/1.0/"
			prefix "exb" "http://www.example.com/books/1.0/"
		}
	'''
}