package editor.actions.toolbar.edit;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.observers.ClipboardObserver;

import java.awt.event.ActionEvent;

public class PasteAction extends AbstractToolBarAction implements ClipboardObserver {

	public PasteAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		editor.getModel().insertText(editor.getClipboard().peek());
	}

	@Override
	public void updateClipboard(boolean isEmpty) {
		setEnabled(!isEmpty);
	}
}
