package com.zazuko.rdfmapping.fanin.nq.nqfanin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.zazuko.rdfmapping.dsl.common.util.LazyMap;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqClass;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninFactory;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqNameAware;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqProperty;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqVocabulary;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.LineContext;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.Statement;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.ValueType;

public class GrammarToDomainConverter {

	private static Logger logger = Logger.getLogger(GrammarToDomainConverter.class);

	public Optional<NqVocabulary> convert(String name, List<Statement> parsedStatements) {

		parsedStatements.removeIf(s -> {
			boolean dropMe = s.getSubject() == null;
			dropMe = dropMe || !ValueType.IRI_REF.equals(s.getSubject().getType()); // drop blank nodes
			dropMe = dropMe || s.getPredicate() == null;
			dropMe = dropMe || !ValueType.IRI_REF.equals(s.getPredicate().getType()); // drop blank nodes
			dropMe = dropMe || s.getObject() == null;
			if (logger.isDebugEnabled()) {
				logger.debug("dropping statement: " + s);
			}
			return dropMe;
		});

		if (parsedStatements.isEmpty()) {
			return Optional.empty();
		}

		// group by subject
		LazyMap<String, List<Statement>> subject2Statements = new LazyMap<>(new TreeMap<>(), ArrayList::new);
		String shortestIri = null;
		for (Statement statement : parsedStatements) {
			String iri = statement.getSubject().getValue();
			subject2Statements.getOrInit(iri).add(statement);

			// fetch shortest iri - we expect only one
			if (shortestIri == null || iri.length() < shortestIri.length()) {
				shortestIri = iri;
			}
		}

		final String iri;
		final String prefix;
		if (shortestIri.endsWith("#")) {
			prefix = shortestIri;
			iri = shortestIri.substring(0, shortestIri.length() - 1);
		} else {
			prefix = shortestIri + "#";
			iri = shortestIri;
		}

		NqVocabulary result = NqFaninFactory.eINSTANCE.createNqVocabulary();
		result.setLabel(name);
		result.setIri(prefix);

		for (Entry<String, List<Statement>> group : subject2Statements.entrySet()) {
			if (group.getKey().length() <= result.getIri().length()) {
				if (!group.getKey().equals(iri)) {
					logger.warn("dropping unexpected group with key '" + group.getKey() + "'");
				}
				continue;
			}
			String groupName = group.getKey().substring(result.getIri().length());
			NqNameAware newElement = transformGroup(result, groupName, iri, group.getValue());

			if (newElement != null) {
				LineContext firstLineContext = group.getValue().get(0).getCtx();
				LineContext lastConsecutiveLineContext = firstLineContext;
				int startPosition = firstLineContext.getStartPosition();
				int consecutivityChecker = firstLineContext.getLineNumber();
				for (int a = 1; a < group.getValue().size(); a++) {
					LineContext candidate = group.getValue().get(a).getCtx();
					if (++consecutivityChecker != candidate.getLineNumber()) {
						break;
					}
					lastConsecutiveLineContext = candidate;
				}
				int endPosition = lastConsecutiveLineContext.getEndPosition();

				newElement.eAdapters().add(new PositionAdapter(startPosition, endPosition, newElement.getEid()));
			}
		}

		return Optional.of(result);
	}

	private NqNameAware transformGroup(NqVocabulary result, String name, String iri, List<Statement> statements) {
		if (isClass(statements, iri)) {
			NqClass nqClass = NqFaninFactory.eINSTANCE.createNqClass();
			result.getClasses().add(nqClass);
			nqClass.setName(name);
			nqClass.setEid(NqClass.class.getSimpleName() + "_" + name);
			return nqClass;

		} else if (isProperty(statements, iri)) {
			NqProperty nqProperty = NqFaninFactory.eINSTANCE.createNqProperty();
			result.getProperties().add(nqProperty);
			nqProperty.setName(name);
			nqProperty.setEid(NqProperty.class.getSimpleName() + "_" + name);
			return nqProperty;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("unclassifiable group of statements with name '" + name + "'");
		}
		return null;

	}

	private boolean isClass(List<Statement> statements, String ownIri) {
		return hasPredicateWithObject(statements, ValueType.IRI_REF, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				ValueType.IRI_REF, "http://www.w3.org/2002/07/owl#Class")
				&& hasPredicateWithObject(statements, ValueType.IRI_REF,
						"http://www.w3.org/2000/01/rdf-schema#isDefinedBy", ValueType.IRI_REF, ownIri);
	}

	private boolean isProperty(List<Statement> statements, String ownIri) {
		return hasPredicateWithObject(statements, ValueType.IRI_REF, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				ValueType.IRI_REF, "http://www.w3.org/1999/02/22-rdf-syntax-ns#Property")
				&& hasPredicateWithObject(statements, ValueType.IRI_REF,
						"http://www.w3.org/2000/01/rdf-schema#isDefinedBy", ValueType.IRI_REF, ownIri);
	}

	private boolean hasPredicateWithObject(List<Statement> statements, ValueType predicateType, String predicateValue,
			ValueType objectType, String objectValue) {
		return statements.stream().filter(s -> predicateType.equals(s.getPredicate().getType()))
				.filter(s -> predicateValue.equals(s.getPredicate().getValue()))
				.filter(s -> objectType.equals(s.getObject().getType()))
				.filter(s -> objectValue.equals(s.getObject().getValue())).findFirst().isPresent();
	}

}
