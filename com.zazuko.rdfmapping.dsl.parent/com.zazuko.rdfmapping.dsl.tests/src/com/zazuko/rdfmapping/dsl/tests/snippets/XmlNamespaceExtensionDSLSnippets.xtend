package com.zazuko.rdfmapping.dsl.tests.snippets

class XmlNamespaceExtensionDSLSnippets {
	
	def separatorInLabel() '''
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa:" "http://www.example.com/audios/1.0/"
		}
	'''

	def duplicatedPrefix() '''
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa" "http://www.example.com/audios/1.0/"
			prefix "exa" "http://www.example.com/audios/2.0/"
		}
	'''

	def ok() '''
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa" "http://www.example.com/audios/1.0/"
			prefix "exb" "http://www.example.com/books/1.0/"
		}
	'''
}