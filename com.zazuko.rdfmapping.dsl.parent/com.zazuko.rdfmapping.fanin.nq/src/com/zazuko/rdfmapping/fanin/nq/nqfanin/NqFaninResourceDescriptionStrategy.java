package com.zazuko.rdfmapping.fanin.nq.nqfanin;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.impl.DefaultResourceDescriptionStrategy;
import org.eclipse.xtext.util.IAcceptor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.PositionAdapter;

@Singleton
public class NqFaninResourceDescriptionStrategy extends DefaultResourceDescriptionStrategy {

	private static final Logger LOG = Logger.getLogger(NqFaninResourceDescriptionStrategy.class);

	public static final String NS_URI_INDEX_ENTRY = "nsURI";

	@Inject
	private NsURIQualifiedNameProvider nsURIQualifiedNameProvider;

	@Override
	public boolean createEObjectDescriptions(EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		boolean isProcessContents = createEObjectDescriptions(getQualifiedNameProvider(), false, eObject, acceptor);
		isProcessContents |= createEObjectDescriptions(nsURIQualifiedNameProvider, true, eObject, acceptor);
		return isProcessContents;
	}

	protected boolean createEObjectDescriptions(IQualifiedNameProvider qualifiedNameProvider, boolean isNsURI,
			EObject eObject, IAcceptor<IEObjectDescription> acceptor) {
		try {
			QualifiedName qualifiedName = qualifiedNameProvider.getFullyQualifiedName(eObject);
			if (qualifiedName != null) {
				Map<String, String> userData = new HashMap<>();
				userData.put(NS_URI_INDEX_ENTRY, Boolean.toString(isNsURI));
				for (Adapter candidate : eObject.eAdapters()) {
					if (candidate instanceof PositionAdapter) {
						PositionAdapter p = (PositionAdapter) candidate;
						p.toUserData(userData);
						break;
					}
				}

				IEObjectDescription description = EObjectDescription.create(qualifiedName, eObject, userData);
				acceptor.accept(description);
				return true;
			}
		} catch (Exception exc) {
			LOG.error(exc.getMessage(), exc);
		}
		return false;
	}

}
