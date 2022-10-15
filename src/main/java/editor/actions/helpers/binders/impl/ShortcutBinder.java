package editor.actions.helpers.binders.impl;

import editor.actions.helpers.binders.KeyActionBinder;

import javax.swing.*;

public class ShortcutBinder implements KeyActionBinder {

	@Override
	public void bind(KeyStroke keyStroke, Action action) {
		action.putValue(Action.ACCELERATOR_KEY, keyStroke);
	}
}
