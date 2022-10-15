package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.components.model.location.LocationRange;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEditAction implements EditAction {

    protected final TextEditorModel model;
    protected final Location cursorLocationBeforeAction;
    protected final LocationRange selectionRangeBeforeAction;
    protected final List<String> linesBeforeAction;

    protected AbstractEditAction(TextEditorModel model) {
        this.model = model;
        this.cursorLocationBeforeAction = model.getCursorLocation();
        this.selectionRangeBeforeAction = model.getSelectionRange();
        this.linesBeforeAction = new ArrayList<>(model.getLines());
    }
}
