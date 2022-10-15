package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.constants.SystemConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class InsertTextAction extends AbstractEditAction {

    private final String text;

    private final static String LINE_BREAK_DELIMITER = "((?<=\n)|(?=\n))";

    public InsertTextAction(TextEditorModel model, String text) {
        super(model);
        this.text = text;
    }

    @Override
    public void executeDo() {
        if (text == null || text.isEmpty()) return;

        if (selectionRangeBeforeAction != null) {
            model.deleteRange(selectionRangeBeforeAction);
        }

        List<String> lines = new ArrayList<>(linesBeforeAction);
        Location cursorLocation = cursorLocationBeforeAction;

        int lineNumber = cursorLocation.line();
        int columnNumber = cursorLocation.column();

        String currentLine = lines.get(lineNumber);
        String prefix = currentLine.substring(0, columnNumber);
        String suffix = currentLine.substring(columnNumber);

        List<String> newLines = tranformTextIntoLines(text);

        int newLinesCount = newLines.size();
        if (newLinesCount == 1) {
            String updatedLine = String.format("%s%s%s", prefix, text, suffix);
            lines.set(lineNumber, updatedLine);
            cursorLocation = new Location(lineNumber, updatedLine.length() - suffix.length());
        } else {
            lines.set(lineNumber, String.format("%s%s", prefix, newLines.get(0)));

            int newLinesInsertIndex = lineNumber + 1;
            newLines.subList(1, newLinesCount - 1).forEach(line -> lines.add(newLinesInsertIndex, line));

            String lastNewLine = newLines.get(newLinesCount - 1);
            lines.add(newLinesInsertIndex, String.format("%s%s", lastNewLine, suffix));
            cursorLocation = new Location(lineNumber + newLinesCount - 1, lastNewLine.length());
        }

        model.setLines(lines);
        model.setCursorLocation(cursorLocation);
    }

    private static List<String> tranformTextIntoLines(String text) {
        List<String> newLines = new ArrayList<>(List.of(text.split(LINE_BREAK_DELIMITER)));

        String previousLine = null;
        for (Iterator<String> it = newLines.iterator(); it.hasNext(); ) {
            String line = it.next();
            if (previousLine != null && line.matches(SystemConstants.LINE_SEPARATORS_REGEX) && !previousLine.matches(SystemConstants.LINE_SEPARATORS_REGEX)) {
                it.remove();
            }
            previousLine = line;
        }

        return newLines.stream().map(line -> line.equals("\n") ? "" : line).collect(Collectors.toList());
    }

    @Override
    public void executeUndo() {
        model.setLines(linesBeforeAction);
        model.setSelectionRange(selectionRangeBeforeAction);
        model.setCursorLocation(cursorLocationBeforeAction);
    }
}
