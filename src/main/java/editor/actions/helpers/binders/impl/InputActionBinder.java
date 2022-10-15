package editor.actions.helpers.binders.impl;

import editor.actions.helpers.binders.KeyActionBinder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class InputActionBinder implements KeyActionBinder {

	private final JComponent component;

	public InputActionBinder(JComponent component) {
		this.component = component;
	}

	public void bind(KeyStroke stroke, ActionListener actionListener) {
		bind(stroke, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				actionListener.actionPerformed(actionEvent);
			}
		});
	}

	@Override
	public void bind(KeyStroke stroke, Action action) {
		String key = UUID.randomUUID().toString();
		bind(stroke, key, action);
	}

	private void bind(KeyStroke stroke, String key, Action action) {
		component.getInputMap().put(stroke, key);
		component.getActionMap().put(key, action);
	}
}
