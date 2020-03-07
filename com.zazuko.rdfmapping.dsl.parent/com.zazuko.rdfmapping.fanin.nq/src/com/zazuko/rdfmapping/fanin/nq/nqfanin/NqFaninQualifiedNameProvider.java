package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.util.IResourceScopeCache;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.util.Tuples;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class NqFaninQualifiedNameProvider extends IQualifiedNameProvider.AbstractImpl {

	private static final Logger logger = Logger.getLogger(NqFaninQualifiedNameProvider.class);

	@Inject
	private IResourceScopeCache cache = IResourceScopeCache.NullImpl.INSTANCE;

	@Override
	public QualifiedName getFullyQualifiedName(final EObject obj) {
		return cache.get(Tuples.pair(obj, getCacheKey()), obj.eResource(), new Provider<QualifiedName>() {

			@Override
			public QualifiedName get() {
				if (obj instanceof NqThing) {
					return QualifiedName.create(((NqThing)obj).getName());
				} 
				logger.error("no qname for " + obj);
				return null;
			}

		});
	}

	protected boolean isRecurseParent(final EObject obj) {
		return obj.eContainer() != null;
	}

	protected String getCacheKey() {
		return "fqn";
	}

	protected String name(EPackage ePackage) {
		return ePackage.getName();
	}

	protected String name(EClassifier eClassifier) {
		return eClassifier.getName();
	}

	protected String name(EStructuralFeature eStructuralFeature) {
		return eStructuralFeature.getName();
	}

	protected String name(EOperation eOperation) {
		return eOperation.getName();
	}
}
