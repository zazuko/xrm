package com.zazuko.rdfmapping.fanin.nq.ui.search;

import org.apache.log4j.Logger;
import org.eclipse.xtext.ecore.EcoreResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.search.IXtextSearchFilter;

public class NqFaninSearchFilter implements IXtextSearchFilter {

	private static final Logger logger = Logger.getLogger(NqFaninSearchFilter.class);

	@Override
	public boolean reject(IEObjectDescription element) {
		logger.debug(".reject(): " + element);
		return Boolean.parseBoolean(element.getUserData(EcoreResourceDescriptionStrategy.NS_URI_INDEX_ENTRY));
	}

}
