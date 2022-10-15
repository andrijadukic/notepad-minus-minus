package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.constants.SystemConstants;

import java.util.ArrayList;
import java.util.List;

public class InsertCharacterAction extends AbstractEditAction {

    private final char c;

    public InsertCharacterAction(TextEditorModel model, char c) {
        super(model);
        this.c = c;
    }

    @Override
    public void executeDo() {
        if (selectionRangeBeforeAction != null) {
            model.deleteRange(selectionRangeBeforeAction);
        }

        List<String> lines = new ArrayList<>(linesBeforeAction);
        Location cursorLocation = cursorLocationBeforeAction;

        int lineNumber = cursorLocation.line();
        int columnNumber = cursorLocation.column();

        String line = lines.get(lineNumber).replace("\t", " ".repeat(SystemConstants.TAB_LENGTH));
        String lineBefore = line.substring(0, columnNumber);
        String lineAfter = line.substring(columnNumber);

        if (String.valueOf(c).matches(SystemConstants.LINE_SEPARATORS_REGEX)) {
            lines.set(lineNumber, lineBefore);
            if (lineNumber != model.lineCount()) {
                lines.add(lineNumber + 1, lineAfter);
            } else {
                lines.add(lineAfter);
            }

            cursorLocation = new Location(lineNumber + 1, 0);
        } else if (c == '\t') {
            lines.set(lineNumber, String.format("%s%s%s", lineBefore, " ".repeat(SystemConstants.TAB_LENGTH), lineAfter));
            cursorLocation = new Location(lineNumber, columnNumber + SystemConstants.TAB_LENGTH);
        } else {
            lines.set(lineNumber, String.format("%s%s%s", lineBefore, c, lineAfter));
            cursorLocation = new Location(lineNumber, columnNumber + 1);
        }

        model.setLines(lines);
        model.setCursorLocation(cursorLocation);
    }

    @Override
    public void executeUndo() {
        model.setLines(linesBeforeAction);
        model.setSelectionRange(selectionRangeBeforeAction);
        model.setCursorLocation(cursorLocationBeforeAction);
    }
}
