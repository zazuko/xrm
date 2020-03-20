package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

public class NqFaninRuntimeModule extends AbstractGenericResourceRuntimeModule {

	public static final String NQ_FANIN_FILE_EXTENSION = "nq";

	@Override
	protected String getLanguageName() {
		return "com.zazuko.rdfmapping.fanin.nq.NqFaninEditorID"; // ?
//		return "http://www.zazuko.com/rdfmapping/fanin/nq"; //?
	}

	@Override
	protected String getFileExtensions() {
		return NQ_FANIN_FILE_EXTENSION;
	}

	public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return NqFaninResourceDescriptionStrategy.class;
	}

	@Override
	public Class<? extends Manager> bindIResourceDescription$Manager() {
		return NqFaninResourceDescriptionManager.class;
	}

	@Override
	public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return NqFaninQualifiedNameProvider.class;
	}

}
