package editor.plugins.impl.uppercase;

import editor.actions.model.EditAction;
import editor.clipboard.ClipboardStack;
import editor.components.model.TextEditorModel;
import editor.plugins.Plugin;
import editor.undo.UndoManager;

import java.util.*;
import java.util.stream.Collectors;

public class UppercasePlugin implements Plugin, EditAction {

    private TextEditorModel model;
    private List<String> linesBeforeAction;

    @Override
    public String getName() {
        return "Uppercase";
    }

    @Override
    public String getDescription() {
        return "Converts all words in the editor so that they start with an uppercase letter";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        this.model = model;
        executeDo();
        undoManager.push(this);
    }

    @Override
    public void executeDo() {
        linesBeforeAction = new ArrayList<>(model.getLines());

        model.setLines(linesBeforeAction.stream()
                .map(line -> {
                    if (line.isEmpty()) return line;

                    char[] chars = line.toCharArray();

                    char current = chars[0];
                    if (Character.isAlphabetic(current)) {
                        chars[0] = Character.toUpperCase(current);
                    }

                    char previous = current;
                    for (int i = 1, n = chars.length; i < n; i++) {
                        current = chars[i];
                        if (Character.isAlphabetic(current) && !Character.isAlphabetic(previous)) {
                            chars[i] = Character.toUpperCase(current);
                        }
                        previous = current;
                    }

                    return String.valueOf(chars);
                })
                .collect(Collectors.toList()));
    }

    @Override
    public void executeUndo() {
        model.setLines(linesBeforeAction);
    }
}
