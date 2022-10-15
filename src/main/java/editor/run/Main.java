package editor.run;

import editor.components.main.TextEditorApplication;
import editor.components.model.TextEditorModel;

import javax.swing.*;

public class Main {

    private static final String INITIAL_TEXT = "Text editor\nide...\nbrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextEditorApplication(new TextEditorModel(INITIAL_TEXT)).setVisible(true);
        });
    }
}
