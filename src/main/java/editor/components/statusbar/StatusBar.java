package editor.components.statusbar;

import editor.components.model.TextEditorModel;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final TextEditorModel model;

    private JLabel cursorLocationLabel;
    private JLabel lineCounterLabel;

    public StatusBar(TextEditorModel model) {
        this.model = model;

        initCursorObserver();
        initTextObserver();
        
        initGUI();
    }

    private void initCursorObserver() {
        model.addCursorObserver(location -> cursorLocationLabel.setText(location.toString()));
    }

    private void initTextObserver() {
        model.addTextObserver((isEmpty) -> lineCounterLabel.setText(String.format("Lines: %d", model.lineCount())));
    }

    private void initGUI() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        cursorLocationLabel = new JLabel(model.getCursorLocation().toString());
        lineCounterLabel = new JLabel(String.format("Lines: %d", model.lineCount()));

        add(lineCounterLabel);
        add(cursorLocationLabel);
    }
}
