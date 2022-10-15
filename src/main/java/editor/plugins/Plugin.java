package editor.plugins;

import editor.clipboard.ClipboardStack;
import editor.components.model.TextEditorModel;
import editor.undo.UndoManager;

public interface Plugin {

	String getName();

	String getDescription();

	void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
