package editor.actions.textarea;

import editor.components.model.TextEditorModel;

import java.awt.event.KeyAdapter;

public abstract class AbstractKeyListener extends KeyAdapter {

    protected final TextEditorModel model;

    protected AbstractKeyListener(TextEditorModel model) {
        this.model = model;
    }
}
