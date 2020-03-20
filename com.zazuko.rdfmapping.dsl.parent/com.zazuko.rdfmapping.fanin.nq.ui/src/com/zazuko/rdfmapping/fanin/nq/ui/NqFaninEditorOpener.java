package com.zazuko.rdfmapping.fanin.nq.ui;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.editor.LanguageSpecificURIEditorOpener;

import com.google.inject.Inject;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.PositionAdapter;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.PositionInformation;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.util.RealNqFaninResourceImpl;

public class NqFaninEditorOpener extends LanguageSpecificURIEditorOpener {

	private static final Logger logger = Logger.getLogger(NqFaninEditorOpener.class);

	@Inject
	private IResourceServiceProvider.Registry resourceServiceProviderRegistry;

	@Override
	protected void selectAndReveal(IEditorPart openEditor, URI uri, EReference crossReference, int indexInList,
			boolean select) {
		if (uri.fragment() != null) {
			TextEditor editor = openEditor.getAdapter(TextEditor.class);
			if (editor != null) {
				try {
					IResourceServiceProvider resourceServiceProvider = resourceServiceProviderRegistry
							.getResourceServiceProvider(uri.trimFragment());
					Manager rdm = resourceServiceProvider.getResourceDescriptionManager();
					IResourceDescription resourceDescription = rdm
							.getResourceDescription(new RealNqFaninResourceImpl(uri));

					// example uri: platform:/resource/editor-test/skos.nq#NqClass_Collection
					// example fragment/expectedAddress: NqClass_Collection
					String expectedAddress = uri.fragment();
					for (IEObjectDescription desc : resourceDescription.getExportedObjects()) {
						Optional<PositionInformation> posO = PositionAdapter.fromIEObjectDescription(expectedAddress,
								desc);
						if (posO.isPresent()) {
							PositionInformation position = posO.get();
							editor.selectAndReveal(position.getStart(), position.getEnd() - position.getStart());
							return;
						}

					}

				} catch (Exception e) {
					logger.warn("failed to find location", e);
				}
				editor.selectAndReveal(0, 0);
			}
		}
	}

	@Override
	protected String getEditorId() {
		return "org.eclipse.ui.DefaultTextEditor"; // delegate to default eclipse text editor
	}

}
