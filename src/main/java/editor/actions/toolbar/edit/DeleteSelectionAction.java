package editor.actions.toolbar.edit;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.components.model.TextEditorModel;
import editor.observers.SelectionObserver;

import java.awt.event.ActionEvent;

public class DeleteSelectionAction extends AbstractToolBarAction implements SelectionObserver {

	public DeleteSelectionAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		super.actionPerformed(actionEvent);

		TextEditorModel model = editor.getModel();
		model.deleteRange(model.getSelectionRange());
	}

	@Override
	public void updateTextSelection(boolean hasSelection) {
		setEnabled(hasSelection);
	}
}
