package com.zazuko.rdfmapping.fanin.nq.nqfanin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqFaninFactory;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.NqThing;

public class RealNqFaninResourceImpl extends NqFaninResourceImpl {

	public RealNqFaninResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		reader.lines().forEach(line -> {
			if (line == null || line.isEmpty()) {
				return;
			}
			NqThing thing = NqFaninFactory.eINSTANCE.createNqThing();
			this.getContents().add(thing);
			thing.setName(line);
		});
	}
	
	

}
