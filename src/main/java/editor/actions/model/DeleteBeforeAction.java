package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;

import java.util.ArrayList;
import java.util.List;

public class DeleteBeforeAction extends AbstractEditAction {

    public DeleteBeforeAction(TextEditorModel model) {
        super(model);
    }

    @Override
    public void executeDo() {
        if (model.isEmpty()) return;

        Location cursorLocation = cursorLocationBeforeAction;

        int lineNumber = cursorLocation.line();
        int columnNumber = cursorLocation.column();

        if (columnNumber == 0 && lineNumber == 0) return;

        List<String> lines = new ArrayList<>(linesBeforeAction);

        String line = lines.get(lineNumber);
        if (columnNumber == 0) {
            lines.remove(lineNumber);
            String previousLine = lines.get(lineNumber - 1);
            String updatedLine = previousLine + line;
            lines.set(lineNumber - 1, updatedLine);
            cursorLocation = new Location(lineNumber - 1, previousLine.length());
        } else {
            String updatedLine = new StringBuilder(lines.get(lineNumber)).deleteCharAt(columnNumber - 1).toString();
            lines.set(lineNumber, updatedLine);
            cursorLocation = new Location(lineNumber, columnNumber - 1);
        }

        model.setLines(lines);
        model.setCursorLocation(cursorLocation);
    }

    @Override
    public void executeUndo() {
        model.setLines(linesBeforeAction);
        model.setCursorLocation(cursorLocationBeforeAction);
    }
}
