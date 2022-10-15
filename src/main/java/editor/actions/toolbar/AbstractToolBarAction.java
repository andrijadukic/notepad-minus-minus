package editor.actions.toolbar;

import editor.components.main.TextEditorApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class AbstractToolBarAction extends AbstractAction {

	protected final TextEditorApplication editor;

	public AbstractToolBarAction(String name,  TextEditorApplication editor) {
		super(name);
		this.editor = editor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled()) throw new IllegalStateException("Action called while disabled");
	}
}
