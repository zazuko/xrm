package com.zazuko.rdfmapping.fanin.nq.nqfanin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninFactory;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqThing;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.GrammarToDomainConverter;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.Statement;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.StatementParser;

public class RealNqFaninResourceImpl extends NqFaninResourceImpl {

	private StatementParser statementParser = new StatementParser();
	private GrammarToDomainConverter converter = new GrammarToDomainConverter();
	
	public RealNqFaninResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		List<Statement> parsedStatements = new ArrayList<>();
		int lineNumber = -1;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			if (line.isEmpty()) {
				continue;
			}
			if (line.startsWith("#") && line.length() > 1) {
				// dummy implementation
				// TODO remove dummy
				NqThing thing = NqFaninFactory.eINSTANCE.createNqThing();
				this.getContents().add(thing);
				thing.setName(line.substring(1));
			} else {
				// nq grammar: https://www.w3.org/TR/n-quads/#sec-grammar
				Statement statement = this.statementParser.parse(lineNumber, line);
				parsedStatements.add(statement);
			}
		}
		this.converter.convert(extractName(), parsedStatements).ifPresent(vocabulary -> getContents().add(vocabulary));
	}

	private String extractName() {
		return this.getURI().trimFileExtension().lastSegment();
	}

}
