package editor.plugins.impl.statistics;

import editor.clipboard.ClipboardStack;
import editor.components.model.TextEditorModel;
import editor.plugins.Plugin;
import editor.undo.UndoManager;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticsPlugin implements Plugin {

    @Override
    public String getName() {
        return "Statistics";
    }

    @Override
    public String getDescription() {
        return "Counts the number of rows, words and letters in the editor ";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        List<String> lines = model.getLines();
        List<String> words = lines.stream()
                .flatMap(line -> Stream.of(line.split("\\W+")))
                .collect(Collectors.toUnmodifiableList());

        int numberOfRows = model.lineCount();
        int numberOfWords = words.size();
        int numberOfLetters = words.stream().mapToInt(String::length).sum();

        JOptionPane.showMessageDialog(null, String.format("Rows: %d%sWords: %d%sLetters: %d%s",
                numberOfRows, System.lineSeparator(),
                numberOfWords, System.lineSeparator(),
                numberOfLetters, System.lineSeparator()));
    }
}
