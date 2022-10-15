package editor.actions.textarea;

import editor.components.model.TextEditorModel;

import java.awt.event.KeyEvent;

public class DeletionKeyListener extends AbstractKeyListener {

    public DeletionKeyListener(TextEditorModel model) {
        super(model);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode != KeyEvent.VK_BACK_SPACE && keyCode != KeyEvent.VK_DELETE) return;

        if (model.getSelectionRange() != null) {
            model.deleteRange(model.getSelectionRange());
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            model.deleteBefore();
        } else {
            model.deleteAfter();
        }
    }
}
