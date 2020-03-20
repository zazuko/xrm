package com.zazuko.rdfmapping.fanin.nq.nqfanin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

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
		int position = 0;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			if (!line.isEmpty()) {
				Statement statement = this.statementParser.parse(lineNumber, position, line);
				parsedStatements.add(statement);
				position += line.length();
			}
			position++; // newLine
		}
		this.converter.convert(extractName(), parsedStatements).ifPresent(vocabulary -> getContents().add(vocabulary));
	}

	private String extractName() {
		return this.getURI().trimFileExtension().lastSegment();
	}

}
