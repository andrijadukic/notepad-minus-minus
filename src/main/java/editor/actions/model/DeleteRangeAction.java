package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.components.model.location.LocationRange;

import java.util.ArrayList;
import java.util.List;

public class DeleteRangeAction extends AbstractEditAction {

	private final LocationRange range;

	public DeleteRangeAction(TextEditorModel model, LocationRange range) {
		super(model);
		this.range = range;
	}

	@Override
	public void executeDo() {
		List<String> lines = new ArrayList<>(linesBeforeAction);

		Location start = range.getStart();
		Location end = range.getEnd();

		if (start.compareTo(end) > 0) {
			Location temp = start;
			start = end;
			end = temp;
		}

		int startLineNumber = start.line();
		int endLineNumber = end.line();
		int startColumn = start.column();
		int endColumn = end.column();

		String startLine = lines.get(startLineNumber);
		String endLine = lines.get(endLineNumber);

		if (startLineNumber == endLineNumber) {
			String updatedLine = new StringBuilder(startLine).delete(startColumn, endColumn).toString();
			lines.set(startLineNumber, updatedLine);
		} else {
			String updatedStartLine = startLine.substring(0, startColumn);
			lines.set(startLineNumber, updatedStartLine);

			int lineToRemove = start.line() + 1;
			for (int i = lineToRemove, n = end.line(); i < n; i++) {
				lines.remove(lineToRemove);
			}

			String updatedEndLine = endLine.substring(endColumn);
			lines.set(lineToRemove, updatedEndLine);
		}

		model.setCursorLocation(start);
		model.setSelectionRange(null);
		model.setLines(lines);
	}

	@Override
	public void executeUndo() {
		model.setLines(linesBeforeAction);
		model.setSelectionRange(selectionRangeBeforeAction);
		model.setCursorLocation(cursorLocationBeforeAction);
	}
}
