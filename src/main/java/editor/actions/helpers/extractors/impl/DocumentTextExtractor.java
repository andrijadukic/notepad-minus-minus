package editor.actions.helpers.extractors.impl;

import editor.components.model.TextEditorModel;

public class DocumentTextExtractor extends AbstractTextExtractor {

    public DocumentTextExtractor(TextEditorModel model) {
        super(model);
    }

    @Override
    public String extractText() {
        return String.join(System.lineSeparator(), model.getLines());
    }
}
