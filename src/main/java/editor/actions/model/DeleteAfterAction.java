package editor.actions.model;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;

import java.util.ArrayList;
import java.util.List;

public class DeleteAfterAction extends AbstractEditAction {

	public DeleteAfterAction(TextEditorModel model) {
		super(model);
	}

	@Override
	public void executeDo() {
		if (model.isEmpty()) return;

		List<String> lines = new ArrayList<>(linesBeforeAction);
		Location cursorLocation = cursorLocationBeforeAction;

		int lineNumber = cursorLocation.line();
		int columnNumber = cursorLocation.column();

		String line = lines.get(lineNumber);

		if (columnNumber == line.length() && lineNumber == model.lineCount() - 1) return;

		if (columnNumber == line.length()) {
			lines.set(lineNumber, lines.get(lineNumber + 1));
			lines.remove(lineNumber + 1);
		} else {
			String updatedLine = new StringBuilder(line).deleteCharAt(columnNumber).toString();
			lines.set(lineNumber, updatedLine);
		}

		model.setLines(lines);
	}

	@Override
	public void executeUndo() {
		model.setLines(linesBeforeAction);
	}
}