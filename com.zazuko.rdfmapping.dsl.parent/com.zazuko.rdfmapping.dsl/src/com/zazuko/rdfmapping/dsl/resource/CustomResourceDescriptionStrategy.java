package com.zazuko.rdfmapping.dsl.resource;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

public class CustomResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	private final Map<EClass, UserDataProvider> userDataProviders = new HashMap<>();

	public void addUserDataProvider(EClass targetClass, UserDataProvider udp) {
		if (targetClass == null) {
			throw new NullPointerException("targetClass");
		}
		if (udp == null) {
			throw new NullPointerException("udp");
		}

		// reject override of UserDataProvider binding
		if (this.userDataProviders.containsKey(targetClass)) {
			throw new IllegalStateException(String.format("UserDataProvider for targetClass %s is already bound to %s",
					targetClass, this.userDataProviders.get(targetClass)));
		}

		this.userDataProviders.put(targetClass, udp);
	}

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> origAcceptor) {
		EClass eClass = eObject.eClass();
		if (!this.userDataProviders.containsKey(eClass)) {
			return super.createEObjectDescriptions(eObject, origAcceptor);
		}

		UserDataProvider udp = this.userDataProviders.get(eClass);
		final Map<String, String> userData = udp.provideUserData(eObject);

		// replace userData
		return super.createEObjectDescriptions(eObject, new IAcceptor<IEObjectDescription>() {
			@Override
			public void accept(IEObjectDescription t) {
				origAcceptor.accept(EObjectDescription.create(t.getQualifiedName(), t.getEObjectOrProxy(), userData));
			}
		});
	}

}
