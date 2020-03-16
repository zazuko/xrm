package com.zazuko.rdfmapping.fanin.nq.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.xtext.ui.editor.LanguageSpecificURIEditorOpener;

public class NqFaninEditorOpener extends LanguageSpecificURIEditorOpener {
	
	@Override
	protected void selectAndReveal(IEditorPart openEditor, URI uri,
			EReference crossReference, int indexInList, boolean select) {
		if (uri.fragment() != null) {
			TextEditor editor = openEditor.getAdapter(TextEditor.class);
			if (editor != null) {
				editor.selectAndReveal(0, 2); // TODO calculate correct location based on EObject hidden info (to be written)
			}
		}
	}

	@Override
	protected String getEditorId() {
		return "org.eclipse.ui.DefaultTextEditor"; // delegate to default eclipse text editor
	}
	
	

}
