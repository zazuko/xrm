package com.zazuko.scratch.scratch;

import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

public class Parse {
	public static void main(String[] args) throws Exception {
		System.out.println("Hello World!");
		Model model;
		try (InputStream input = new FileInputStream(
				"../com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.fanin.nq.tests/data/skos/skos.nq")) {

			// Rio also accepts a java.io.Reader as input for the parser.
			model = Rio.parse(input, "", RDFFormat.NQUADS);
		}
		System.out.println(model);
		System.out.println("bye!");
	}
}
