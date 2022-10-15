package editor.actions.toolbar.navigation;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;

import java.awt.event.ActionEvent;

public class MoveToStartAction extends AbstractToolBarAction {

	public MoveToStartAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		editor.getModel().moveToStart();
	}
}
