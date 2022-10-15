package editor.actions.toolbar.edit;

import editor.actions.helpers.extractors.impl.SelectionExtractor;
import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.observers.SelectionObserver;

import java.awt.event.ActionEvent;

public class CopyAction extends AbstractToolBarAction implements SelectionObserver {

	public CopyAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		editor.getClipboard().push(new SelectionExtractor(editor.getModel()).extractText());
	}

	@Override
	public void updateTextSelection(boolean hasSelection) {
		setEnabled(hasSelection);
	}
}
