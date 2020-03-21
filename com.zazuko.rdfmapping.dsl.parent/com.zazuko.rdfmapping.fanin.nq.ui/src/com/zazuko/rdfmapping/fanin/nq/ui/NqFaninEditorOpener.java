package com.zazuko.rdfmapping.fanin.nq.ui;

import java.util.Optional;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.editor.LanguageSpecificURIEditorOpener;

import com.google.inject.Inject;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.PositionAdapter;
import com.zazuko.rdfmapping.fanin.nq.nqfanin.domain.PositionInformation;

public class NqFaninEditorOpener extends LanguageSpecificURIEditorOpener {

	private static final Logger logger = Logger.getLogger(NqFaninEditorOpener.class);
	
	@Inject
	private IResourceDescriptions rd;

	@Override
	protected void selectAndReveal(IEditorPart openEditor, URI uri, EReference crossReference, int indexInList,
			boolean select) {
		if (uri.fragment() != null) {
			TextEditor editor = openEditor.getAdapter(TextEditor.class);
			if (editor != null) {
				try {
					IResourceDescription resourceDescription = this.rd.getResourceDescription(uri.trimFragment());
					
					String expectedLocalObjectAddress = uri.fragment();
					for (IEObjectDescription desc : resourceDescription.getExportedObjects()) {
						Optional<PositionInformation> posO = PositionAdapter.fromIEObjectDescription(expectedLocalObjectAddress,
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
