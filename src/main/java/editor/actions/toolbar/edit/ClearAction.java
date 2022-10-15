package editor.actions.toolbar.edit;

import editor.observers.TextObserver;
import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;

import java.awt.event.ActionEvent;

public class ClearAction extends AbstractToolBarAction implements TextObserver {

	public ClearAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		editor.getModel().clear();
	}

	@Override
	public void updateText(boolean isEmpty) {
		setEnabled(!isEmpty);
	}
}
