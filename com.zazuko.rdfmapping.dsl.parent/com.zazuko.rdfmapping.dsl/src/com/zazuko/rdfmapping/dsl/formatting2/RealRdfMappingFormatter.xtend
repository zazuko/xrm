/*
 * generated by Xtext 2.19.0
 */
package com.zazuko.rdfmapping.dsl.formatting2

import com.google.inject.Inject
import com.zazuko.rdfmapping.dsl.rdfMapping.ConstantValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Datatype
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.DialectGroupDescription
import com.zazuko.rdfmapping.dsl.rdfMapping.Domainmodel
import com.zazuko.rdfmapping.dsl.rdfMapping.LanguageTag
import com.zazuko.rdfmapping.dsl.rdfMapping.LanguageTagDefinition
import com.zazuko.rdfmapping.dsl.rdfMapping.LogicalSource
import com.zazuko.rdfmapping.dsl.rdfMapping.Mapping
import com.zazuko.rdfmapping.dsl.rdfMapping.MultiReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.NullValueDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMap
import com.zazuko.rdfmapping.dsl.rdfMapping.OmniMapEntry
import com.zazuko.rdfmapping.dsl.rdfMapping.OutputTypeRef
import com.zazuko.rdfmapping.dsl.rdfMapping.ParentTriplesMapTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.PredicateObjectMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.Prefix
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfClass
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfMappingPackage
import com.zazuko.rdfmapping.dsl.rdfMapping.RdfProperty
import com.zazuko.rdfmapping.dsl.rdfMapping.ReferenceValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Referenceable
import com.zazuko.rdfmapping.dsl.rdfMapping.SourceGroup
import com.zazuko.rdfmapping.dsl.rdfMapping.SubjectTypeMapping
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateDeclaration
import com.zazuko.rdfmapping.dsl.rdfMapping.TemplateValuedTerm
import com.zazuko.rdfmapping.dsl.rdfMapping.Vocabulary
import com.zazuko.rdfmapping.dsl.rdfMapping.XmlNamespaceExtension
import com.zazuko.rdfmapping.dsl.services.RdfMappingGrammarAccess
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument

// TODO implement in RdfMappingFormatter.java
class RealRdfMappingFormatter extends AbstractFormatter2 {

	@Inject RdfMappingGrammarAccess ga
	RdfMappingPackage pkg = RdfMappingPackage.eINSTANCE;

	def dispatch void format(Domainmodel it, extension IFormattableDocument document) {
		regionFor.keyword(ga.domainmodelAccess.outputKeyword_0_0).append[oneSpace];
		outputType?.format;
		elements.forEach[format];
	}

	def dispatch void format(OutputTypeRef it, extension IFormattableDocument document) {
		append[setNewLines(2)];
	}

	def dispatch void format(SourceGroup it, extension IFormattableDocument document) {
		regionFor.assignment(ga.sourceGroupAccess.nameAssignment_1).surround[oneSpace];
		interior(
			regionFor.ruleCall(ga.sourceGroupAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.sourceGroupAccess.BLOCK_ENDTerminalRuleCall_9)
		)[indent];

		// 'type'
		regionFor.keyword(ga.sourceGroupAccess.typeKeyword_3_0).prepend[setNewLines(1)].append[oneSpace];

		// 'source'
		regionFor.keyword(ga.sourceGroupAccess.sourceKeyword_4_0).prepend[setNewLines(1)];
		regionFor.assignment(ga.sourceGroupAccess.sourceIsQueryAssignment_4_1).prepend[oneSpace];
		regionFor.assignment(ga.sourceGroupAccess.sourceAssignment_4_2).prepend[oneSpace];
		
		if (nullValueMarker !== null) {
			if (source !== null) {
				regionFor.assignment(ga.sourceGroupAccess.sourceAssignment_4_2).append[setNewLines(1)];
			} else if (typeRef !== null) {
				// ! delegation to typeRef
				typeRef.regionFor.assignment(ga.sourceTypeRefAccess.typeAssignment).append[setNewLines(1)];
			}	
			regionFor.keyword(ga.nullValueDeclarationAccess.nullKeyword_0).prepend[setNewLines(1)];
			nullValueMarker.format;
		}

		regionFor.keyword(ga.sourceGroupAccess.dialectKeyword_6_0).prepend[setNewLines(1)].append[oneSpace];
		regionFor.keyword(ga.sourceGroupAccess.xmlNamespaceExtensionKeyword_7_0).prepend[setNewLines(1)].append [
			oneSpace
		];

		logicalSources.forEach[format];
	}

	def dispatch void format(Prefix it, extension IFormattableDocument document) {
		prepend[setNewLines(1)];
		regionFor.keyword(ga.prefixAccess.prefixKeyword_0).append[oneSpace];
		regionFor.feature(pkg.prefix_Iri).prepend[oneSpace]
	}

	def dispatch void format(Vocabulary it, extension IFormattableDocument document) {
		interior(
			regionFor.ruleCall(ga.vocabularyAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.vocabularyAccess.BLOCK_ENDTerminalRuleCall_7)
		)[indent];

		regionFor.keyword(ga.vocabularyAccess.vocabularyKeyword_0).append[oneSpace];
		regionFor.ruleCall(ga.vocabularyAccess.BLOCK_BEGINTerminalRuleCall_2).prepend[oneSpace];

		prefix?.format

		// 'classes'
		if (!properties.empty) {
			interior(
				regionFor.keyword(ga.vocabularyAccess.classesKeyword_4_0),
				regionFor.keyword(ga.vocabularyAccess.propertiesKeyword_5_0)
			)[indent];
		} else if (!datatypes.empty) {
			interior(
				regionFor.keyword(ga.vocabularyAccess.classesKeyword_4_0),
				regionFor.keyword(ga.vocabularyAccess.datatypesKeyword_6_0)
			)[indent];
		} else {
			interior(
				regionFor.keyword(ga.vocabularyAccess.classesKeyword_4_0),
				regionFor.ruleCall(ga.vocabularyAccess.BLOCK_ENDTerminalRuleCall_7)
			)[indent];
		}
		regionFor.keyword(ga.vocabularyAccess.classesKeyword_4_0).prepend[setNewLines(2)];
		classes.forEach[format];

		// 'properties'
		if (!datatypes.empty) {
			interior(
				regionFor.keyword(ga.vocabularyAccess.propertiesKeyword_5_0),
				regionFor.keyword(ga.vocabularyAccess.datatypesKeyword_6_0)
			)[indent];
		} else {
			interior(
				regionFor.keyword(ga.vocabularyAccess.propertiesKeyword_5_0),
				regionFor.ruleCall(ga.vocabularyAccess.BLOCK_ENDTerminalRuleCall_7)
			)[indent];

		}
		regionFor.keyword(ga.vocabularyAccess.propertiesKeyword_5_0).prepend[setNewLines(2)];
		properties.forEach[format];

		// 'datatypes'
		interior(
			regionFor.keyword(ga.vocabularyAccess.datatypesKeyword_6_0),
			regionFor.ruleCall(ga.vocabularyAccess.BLOCK_ENDTerminalRuleCall_7)
		)[indent];
		regionFor.keyword(ga.vocabularyAccess.datatypesKeyword_6_0).prepend[setNewLines(2)];
		datatypes.forEach[format];

		regionFor.ruleCall(ga.vocabularyAccess.BLOCK_ENDTerminalRuleCall_7).prepend[setNewLines(1)].append [
			setNewLines(2)
		];
	}

	def dispatch void format(RdfClass it, extension IFormattableDocument document) {
		prepend[setNewLines(1)];
		regionFor.feature(pkg.rdfClass_Value).prepend[oneSpace];
		omniMap?.format;
	}

	def dispatch void format(RdfProperty it, extension IFormattableDocument document) {
		prepend[setNewLines(1)];
		regionFor.feature(pkg.rdfProperty_Value).prepend[oneSpace];
		omniMap?.format;
	}
	
	def dispatch void format(Datatype it, extension IFormattableDocument document) {
		prepend[setNewLines(1)];
		regionFor.feature(pkg.datatype_Value).prepend[oneSpace];
		omniMap?.format;
	}
	
	def dispatch void format(OmniMap it, extension IFormattableDocument document) {
		interior(
			regionFor.ruleCall(ga.omniMapAccess.BLOCK_BEGINTerminalRuleCall_0),
			regionFor.ruleCall(ga.omniMapAccess.BLOCK_ENDTerminalRuleCall_3)
		)[indent];
		regionFor.ruleCall(ga.omniMapAccess.BLOCK_BEGINTerminalRuleCall_0).prepend[oneSpace];
		regionFor.keyword(ga.omniMapAccess.commaKeyword_2_0).prepend[noSpace];
		
		entries.forEach[format];
	}

	def dispatch void format(OmniMapEntry it, extension IFormattableDocument document) {
		regionFor.assignment(ga.omniMapEntryAccess.keyAssignment_0).prepend[setNewLines(1, 1, 2)];
		regionFor.keyword(ga.omniMapEntryAccess.colonKeyword_1).prepend[noSpace].append[oneSpace];
	}

	def dispatch void format(LogicalSource it, extension IFormattableDocument document) {
		interior(
			regionFor.ruleCall(ga.logicalSourceAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.logicalSourceAccess.BLOCK_ENDTerminalRuleCall_11)
		)[indent];

		regionFor.keyword(ga.logicalSourceAccess.logicalSourceKeyword_0).prepend[setNewLines(0, 1, 2)].append[oneSpace];
		regionFor.ruleCall(ga.logicalSourceAccess.BLOCK_BEGINTerminalRuleCall_2).prepend[oneSpace];
		regionFor.keyword(ga.logicalSourceAccess.typeKeyword_3_0).prepend[setNewLines(1)].append[oneSpace];
		regionFor.keyword(ga.logicalSourceAccess.sourceKeyword_4_0).prepend[setNewLines(1)].append[oneSpace];
		if (sourceIsQuery) {
			regionFor.keyword(ga.logicalSourceAccess.sourceIsQueryQueryKeyword_4_1_0).append[oneSpace];				
		}
		
		if (nullValueMarker !== null) {
			regionFor.assignment(ga.logicalSourceAccess.sourceAssignment_4_2).append[setNewLines(1)];				
			nullValueMarker.format;
		}
		
		regionFor.keyword(ga.logicalSourceAccess.dialectKeyword_6_0).prepend[setNewLines(1)].append[oneSpace];
		regionFor.keyword(ga.logicalSourceAccess.xmlNamespaceExtensionKeyword_7_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.logicalSourceAccess.iteratorKeyword_8_0).prepend[setNewLines(1)].append[oneSpace];

		interior(
			regionFor.keyword(ga.logicalSourceAccess.referenceablesKeyword_9),
			regionFor.ruleCall(ga.logicalSourceAccess.BLOCK_ENDTerminalRuleCall_11)
		)[indent];
		regionFor.keyword(ga.logicalSourceAccess.referenceablesKeyword_9).prepend[setNewLines(2)];

		referenceables.forEach[format];

		regionFor.ruleCall(ga.logicalSourceAccess.BLOCK_ENDTerminalRuleCall_11).prepend[setNewLines(1)];
	}

	def dispatch void format(Referenceable it, extension IFormattableDocument document) {
		prepend[setNewLines(1, 1, 2)]
		regionFor.feature(pkg.referenceable_Name).append[oneSpace]
	}

	def dispatch void format(NullValueDeclaration it, extension IFormattableDocument document) {
		regionFor.keyword(ga.nullValueDeclarationAccess.nullKeyword_0).append[oneSpace];
	}

	def dispatch void format(TemplateDeclaration it, extension IFormattableDocument document) {
		regionFor.assignment(ga.templateDeclarationAccess.nameAssignment_1).surround[oneSpace];
		// !!! accessing templateVALUEdeclaration, and that's the right thing to do here
		regionFor.assignment(ga.templateValueDeclarationAccess.templateValueAssignment).append[setNewLines(1, 1, 2)];
	}

	def dispatch void format(Mapping it, extension IFormattableDocument document) {
		regionFor.keyword(ga.mappingAccess.mapKeyword_0).append[oneSpace]; // .prepend[setNewLines(1, 1, 2)];
		regionFor.keyword(ga.mappingAccess.fromKeyword_2).surround[oneSpace];

		interior(
			regionFor.ruleCall(ga.mappingAccess.BLOCK_BEGINTerminalRuleCall_4),
			regionFor.ruleCall(ga.mappingAccess.BLOCK_ENDTerminalRuleCall_10)
		)[indent];
		regionFor.ruleCall(ga.mappingAccess.BLOCK_BEGINTerminalRuleCall_4).append[setNewLines(1)];

		regionFor.keyword(ga.mappingAccess.subjectKeyword_5).append[oneSpace];
		subjectIriMapping?.format;
		regionFor.ruleCall(ga.mappingAccess.LINE_ENDTerminalRuleCall_7).prepend[noSpace];

		// 'types'
		if (!poMappings.empty) {
			interior(
				regionFor.keyword(ga.mappingAccess.typesKeyword_8_0),
				regionFor.keyword(ga.mappingAccess.propertiesKeyword_9_0)
			)[indent];
		} else {
			interior(
				regionFor.keyword(ga.mappingAccess.typesKeyword_8_0),
				regionFor.ruleCall(ga.mappingAccess.BLOCK_ENDTerminalRuleCall_10)
			)[indent];
		}
		regionFor.keyword(ga.mappingAccess.typesKeyword_8_0).prepend[setNewLines(2)];
		subjectTypeMappings.forEach[format];

		// 'properties'
		regionFor.keyword(ga.mappingAccess.propertiesKeyword_9_0).prepend[setNewLines(2)];
		interior(
			regionFor.keyword(ga.mappingAccess.propertiesKeyword_9_0),
			regionFor.ruleCall(ga.mappingAccess.BLOCK_ENDTerminalRuleCall_10)
		)[indent];
		poMappings.forEach[format];
	}

	def dispatch void format(SubjectTypeMapping it, extension IFormattableDocument document) {
		regionFor.assignment(ga.subjectTypeMappingAccess.typeAssignment).prepend[setNewLines(1, 1, 2)];
	}

	def dispatch void format(PredicateObjectMapping it, extension IFormattableDocument document) {
		regionFor.assignment(ga.predicateObjectMappingAccess.propertyAssignment_0).prepend[setNewLines(1, 1, 2)].append [
			oneSpace
		];
		term?.format;
		regionFor.assignment(ga.predicateObjectMappingAccess.lineEndAssignment_2).prepend[noSpace];
	}

	def dispatch void format(ReferenceValuedTerm it, extension IFormattableDocument document) {
		regionFor.assignment(ga.referenceValuedTermAccess.referenceAssignment_1).prepend[oneSpace];

		// case 'datatype'
		regionFor.keyword(ga.referenceValuedTermAccess.withKeyword_2_0_0).prepend[oneSpace];
		regionFor.keyword(ga.referenceValuedTermAccess.datatypeKeyword_2_0_1).surround[oneSpace];
		// case 'language-tag'
		regionFor.keyword(ga.referenceValuedTermAccess.withKeyword_2_1_0).prepend[oneSpace];
		regionFor.keyword(ga.referenceValuedTermAccess.languageTagKeyword_2_1_1).surround[oneSpace];

		regionFor.keyword(ga.referenceValuedTermAccess.asKeyword_3_0).surround[oneSpace];
	}

	def dispatch void format(MultiReferenceValuedTerm it, extension IFormattableDocument document) {
		regionFor.keyword(ga.multiReferenceValuedTermAccess.multiReferenceKeyword_0).surround[oneSpace];
		regionFor.keyword(ga.multiReferenceValuedTermAccess.fromKeyword_1).append[oneSpace];

		// case 'datatype'
		regionFor.keyword(ga.multiReferenceValuedTermAccess.withKeyword_3_0_0).prepend[oneSpace];
		regionFor.keyword(ga.multiReferenceValuedTermAccess.datatypeKeyword_3_0_1).surround[oneSpace];
		// case 'language-tag'
		regionFor.keyword(ga.multiReferenceValuedTermAccess.withKeyword_3_1_0).prepend[oneSpace];
		regionFor.keyword(ga.multiReferenceValuedTermAccess.languageTagKeyword_3_1_1).surround[oneSpace];

		regionFor.keyword(ga.multiReferenceValuedTermAccess.asKeyword_4_0).surround[oneSpace];
	}

	def dispatch void format(ConstantValuedTerm it, extension IFormattableDocument document) {
		regionFor.keyword(ga.constantValuedTermAccess.constantKeyword_0).append[oneSpace];
	}

	def dispatch void format(TemplateValuedTerm it, extension IFormattableDocument document) {
		regionFor.keyword(ga.templateValuedTermAccess.templateKeyword_0).surround[oneSpace];
		regionFor.keyword(ga.templateValuedTermAccess.withKeyword_2_0).prepend[oneSpace];
		regionFor.assignment(ga.templateValuedTermAccess.referencesAssignment_2_1).prepend[oneSpace];
		regionFor.keyword(ga.templateValuedTermAccess.asKeyword_3_0).surround[oneSpace];
	}

	def dispatch void format(ParentTriplesMapTerm it, extension IFormattableDocument document) {
		regionFor.keyword(ga.parentTriplesMapTermAccess.parentMapKeyword_0).surround[oneSpace];
	}

	def dispatch void format(LanguageTagDefinition it, extension IFormattableDocument document) {
		interior(
			regionFor.ruleCall(ga.languageTagDefinitionAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.languageTagDefinitionAccess.BLOCK_ENDTerminalRuleCall_4)
		)[indent];
		languageTags.forEach[format];
		regionFor.ruleCall(ga.languageTagDefinitionAccess.BLOCK_ENDTerminalRuleCall_4).prepend[setNewLines(1)];
	}

	def dispatch void format(LanguageTag it, extension IFormattableDocument document) {
		regionFor.assignment(ga.languageTagAccess.nameAssignment).prepend[setNewLines(1)];
	}

	def dispatch void format(DialectGroup it, extension IFormattableDocument document) {
		regionFor.assignment(ga.dialectGroupAccess.nameAssignment_1).surround[oneSpace];
		interior(
			regionFor.ruleCall(ga.dialectGroupAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.dialectGroupAccess.BLOCK_ENDTerminalRuleCall_4)
		)[indent];
		description?.format;
	}

	def dispatch void format(DialectGroupDescription it, extension IFormattableDocument document) {
		regionFor.keyword(ga.dialectGroupDescriptionAccess.delimiterKeyword_0_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.commentPrefixKeyword_1_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.doubleQuoteKeyword_2_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.encodingKeyword_3_0).prepend[setNewLines(1)].
			append[oneSpace];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.headerKeyword_4_0).prepend[setNewLines(1)].append[oneSpace];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.headerRowCountKeyword_5_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.lineTerminatorsKeyword_6_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.quoteCharKeyword_7_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.skipBlankRowsKeyword_8_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.skipColumnsKeyword_9_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.skipInitialSpaceKeyword_10_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.skipRowsKeyword_11_0).prepend[setNewLines(1)].append [
			oneSpace
		];
		regionFor.keyword(ga.dialectGroupDescriptionAccess.trimKeyword_12_0).prepend[setNewLines(1)].append[oneSpace];
	}

	def dispatch void format(XmlNamespaceExtension it, extension IFormattableDocument document) {
		regionFor.assignment(ga.xmlNamespaceExtensionAccess.nameAssignment_1).surround[oneSpace];
		interior(
			regionFor.ruleCall(ga.xmlNamespaceExtensionAccess.BLOCK_BEGINTerminalRuleCall_2),
			regionFor.ruleCall(ga.xmlNamespaceExtensionAccess.BLOCK_ENDTerminalRuleCall_4)
		)[indent];
		prefixes.forEach[format];
	}
}