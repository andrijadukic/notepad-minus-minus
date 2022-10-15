package editor.actions.helpers.extractors.impl;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.components.model.location.LocationRange;

public class SelectionExtractor extends AbstractTextExtractor {

    public SelectionExtractor(TextEditorModel model) {
        super(model);
    }

    @Override
    public String extractText() {
        LocationRange selectionRange = model.getSelectionRange();

        if (selectionRange == null) return null;

        Location start = selectionRange.getStart();
        Location end = selectionRange.getEnd();

        if (start.compareTo(end) > 0) {
            Location temp = start;
            start = end;
            end = temp;
        }

        int startLineNumber = start.line();
        int endLineNumber = end.line();
        int startColumnNumber = start.column();
        int endColumnNumber = end.column();

        if (startLineNumber == endLineNumber) {
            return model.lineAt(startLineNumber).substring(startColumnNumber, endColumnNumber);
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(model.lineAt(startLineNumber).substring(startColumnNumber)).append(System.lineSeparator());

        model.linesRange(startLineNumber + 1, endLineNumber).forEachRemaining(line -> builder.append(line).append(System.lineSeparator()));

        builder.append(model.lineAt(endLineNumber), 0, endColumnNumber);

        return builder.toString();
    }
}
