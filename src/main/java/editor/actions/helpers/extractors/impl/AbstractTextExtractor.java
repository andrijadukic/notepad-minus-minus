package editor.actions.helpers.extractors.impl;

import editor.actions.helpers.extractors.TextExtractor;
import editor.components.model.TextEditorModel;

public abstract class AbstractTextExtractor implements TextExtractor {

    protected final TextEditorModel model;

    protected AbstractTextExtractor(TextEditorModel model) {
        this.model = model;
    }
}
