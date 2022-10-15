package editor.actions.textarea;

import editor.components.model.TextEditorModel;

import java.awt.event.KeyEvent;

public class PrintableCharacterKeyListener extends AbstractKeyListener {

	public PrintableCharacterKeyListener(TextEditorModel model) {
		super(model);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if (!(c >= 32 && c < 127) && c != '\n'  && c != '\t') return;

		model.insert(c);
	}
}
