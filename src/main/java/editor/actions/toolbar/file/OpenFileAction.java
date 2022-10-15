package editor.actions.toolbar.file;

import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OpenFileAction extends AbstractToolBarAction {

	public OpenFileAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Open");

		if (chooser.showOpenDialog(editor) != JFileChooser.APPROVE_OPTION) return;

		Path path = chooser.getSelectedFile().toPath();

		try {
			editor.getModel().setLines(Files.readAllLines(path));
			editor.setOpenedFilePath(path.toString());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(editor, "Unable to open selected file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
