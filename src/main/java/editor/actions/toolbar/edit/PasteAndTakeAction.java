package editor.actions.toolbar.edit;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.observers.ClipboardObserver;

import java.awt.event.ActionEvent;

public class PasteAndTakeAction extends AbstractToolBarAction implements ClipboardObserver {

	public PasteAndTakeAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		editor.getModel().insertText(editor.getClipboard().pop());
	}

	@Override
	public void updateClipboard(boolean isEmpty) {
		setEnabled(!isEmpty);
	}
}
