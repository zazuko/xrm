package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IDefaultResourceDescriptionStrategy;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.generic.AbstractGenericResourceRuntimeModule;

public class NqFaninRuntimeModule extends AbstractGenericResourceRuntimeModule {

	public static final String NQ_FANIN_FILE_EXTENSION = "nq";

	@Override
	protected String getLanguageName() {
//		return "com.zazuko.rdfmapping.fanin.nq.NqFaninEditorID";
		return "http://www.zazuko.com/rdfmapping/fanin/nq";
	}

	@Override
	protected String getFileExtensions() {
		return NQ_FANIN_FILE_EXTENSION;
	}
	
	
	
//	@Inject
//	public void register(Injector injector) {
//		if (!EPackage.Registry.INSTANCE.containsKey(NqFaninPackage.eNS_URI)) {
//			EPackage.Registry.INSTANCE.put(NqFaninPackage.eNS_URI, NqFaninPackage.eINSTANCE);
//		}
//		IResourceFactory resourceFactory = injector.getInstance(IResourceFactory.class);
//		IResourceServiceProvider serviceProvider = injector.getInstance(IResourceServiceProvider.class);
//
//		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(NqFaninRuntimeModule.NQ_FANIN_FILE_EXTENSION, resourceFactory);
//		IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put(NqFaninRuntimeModule.NQ_FANIN_FILE_EXTENSION, serviceProvider);
//	}

	@Override
	public Class<? extends IResourceServiceProvider> bindIResourceServiceProvider() {
		return NqFaninResourceServiceProvider.class;
	}

	@Override
	public Class<? extends Manager> bindIResourceDescription$Manager() {
		// TODO Auto-generated method stub
		return super.bindIResourceDescription$Manager();
	}

	public Class<? extends IDefaultResourceDescriptionStrategy> bindIDefaultResourceDescriptionStrategy() {
		return NqFaninResourceDescriptionStrategy.class;
	}
	
	@Override
	public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return NqFaninQualifiedNameProvider.class;
	}
	
}
