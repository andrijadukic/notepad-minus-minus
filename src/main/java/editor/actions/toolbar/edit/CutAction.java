package editor.actions.toolbar.edit;

import editor.actions.helpers.extractors.impl.SelectionExtractor;
import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;
import editor.components.model.TextEditorModel;
import editor.observers.SelectionObserver;

import java.awt.event.ActionEvent;

public class CutAction extends AbstractToolBarAction implements SelectionObserver {

    public CutAction(String name, TextEditorApplication editor) {
        super(name, editor);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);

        TextEditorModel model = editor.getModel();
        editor.getClipboard().push(new SelectionExtractor(model).extractText());
        model.deleteRange(model.getSelectionRange());
    }

    @Override
    public void updateTextSelection(boolean hasSelection) {
        setEnabled(hasSelection);
    }
}
