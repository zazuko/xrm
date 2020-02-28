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
	
	def outputAndXmlNamespaceExtensionRef(String output, boolean referXmlExtension) '''
		output «output»
		
		map FooMapping from KEYWORDSOURCE {
			subject template "http://foo.example.com/{0}" with id;
		
			types
				foobar.SomeClass
		
			properties
				foobar.one from color;
		}
		
		logical-source KEYWORDSOURCE {
			type xml
			source "KWS"
			
			«IF referXmlExtension»
			xml-namespace-extension SomeXmlNsExtension
			
			«ENDIF»
			referenceables
				id
				color
		}
		
		vocabulary foobar {
			prefix "employee" "http://example.com/employee"
		
			classes
				SomeClass
		
			properties
				one
		}
		
		xml-namespace-extension SomeXmlNsExtension {
			prefix "exa" "http://www.example.com/audios/1.0/"
			prefix "exb" "http://www.example.com/books/1.0/"
		}
	'''
}