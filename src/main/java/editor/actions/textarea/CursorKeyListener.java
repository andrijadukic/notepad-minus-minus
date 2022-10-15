package editor.actions.textarea;

import editor.components.model.TextEditorModel;

import java.awt.event.KeyEvent;

public class CursorKeyListener extends AbstractKeyListener {

    public CursorKeyListener(TextEditorModel model) {
        super(model);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isShiftDown()) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> model.moveCursorUp();
            case KeyEvent.VK_DOWN -> model.moveCursorDown();
            case KeyEvent.VK_LEFT -> model.moveCursorLeft();
            case KeyEvent.VK_RIGHT -> model.moveCursorRight();
            default -> {
                return;
            }
        }

        model.setSelectionRange(null);
    }
}
