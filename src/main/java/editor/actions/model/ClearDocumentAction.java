package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;

import java.util.ArrayList;
import java.util.List;

public class ClearDocumentAction extends AbstractEditAction {

    public ClearDocumentAction(TextEditorModel model) {
        super(model);
    }

    @Override
    public void executeDo() {
        model.setCursorLocation(Location.start());

        model.setSelectionRange(null);
        List<String> blankDocument = new ArrayList<>();
        blankDocument.add(" ");
        model.setLines(blankDocument);
    }

    @Override
    public void executeUndo() {
        model.setLines(linesBeforeAction);
        model.setSelectionRange(selectionRangeBeforeAction);
        model.setCursorLocation(cursorLocationBeforeAction);
    }
}
