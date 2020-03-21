package com.zazuko.rdfmapping.fanin.nq.nqfanin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;

import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.GrammarToDomainConverter;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.Statement;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.parsing.StatementParser;

public class RealNqFaninResourceImpl extends NqFaninResourceImpl {

	private static final Logger logger = Logger.getLogger(RealNqFaninResourceImpl.class);

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

	@Override
	public void load(Map<?, ?> options) throws IOException {
		try {
			super.load(options);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

}
