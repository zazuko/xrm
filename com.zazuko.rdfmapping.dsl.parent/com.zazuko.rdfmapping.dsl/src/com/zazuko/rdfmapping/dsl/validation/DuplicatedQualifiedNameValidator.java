package com.zazuko.rdfmapping.dsl.validation;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;

import com.google.inject.Inject;

public class DuplicatedQualifiedNameValidator {

	@Inject
	private ResourceDescriptionsProvider resourceDescriptionsProvider;

	@Inject
	private IContainer.Manager containerManager;

	@Inject
	private IQualifiedNameProvider qNameProvider;

	public void validate(EObject target, EClass eClass, Consumer<String> errorAcceptor) {
		// copied from (and improved): https://www.eclipse.org/forums/index.php/mv/msg/261440/754503/#msg_754503
		Set<QualifiedName> names = new HashSet<>();
		IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider
				.getResourceDescriptions(target.eResource());
		IResourceDescription resourceDescription = resourceDescriptions
				.getResourceDescription(target.eResource().getURI());
		QualifiedName myQualifiedName = this.qNameProvider.apply(target);
		for (IContainer c : this.containerManager.getVisibleContainers(resourceDescription, resourceDescriptions)) {
			for (IEObjectDescription od : c.getExportedObjects(eClass, myQualifiedName, false)) {
				if (!names.add(od.getQualifiedName())) {
					errorAcceptor.accept(String.format("duplicate %s '%s'", eClass.getName(), od.getQualifiedName()));
					return;
				}
			}
		}
	}

}
