package editor.actions.toolbar.edit;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.observers.StackObserver;

import java.awt.event.ActionEvent;

public class RedoAction extends AbstractToolBarAction implements StackObserver {

	public RedoAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		editor.getUndoManager().redo();
	}

	@Override
	public void updateStack(boolean isEmpty) {
		setEnabled(!isEmpty);
	}
}
