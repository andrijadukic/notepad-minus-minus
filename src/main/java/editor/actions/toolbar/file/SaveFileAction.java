package editor.actions.toolbar.file;

import editor.actions.helpers.extractors.impl.DocumentTextExtractor;
import editor.actions.toolbar.AbstractToolBarAction;
import editor.components.main.TextEditorApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveFileAction extends AbstractToolBarAction {

	public SaveFileAction(String name, TextEditorApplication editor) {
		super(name, editor);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (editor.getOpenedFilePath() == null) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save");

			if (chooser.showSaveDialog(editor) != JFileChooser.APPROVE_OPTION) return;

			Path selectedFilePath = chooser.getSelectedFile().toPath();

			if (Files.exists(selectedFilePath)) {
				int selectedOption = JOptionPane.showConfirmDialog(editor,
						"Are you sure you want to overwrite the selected file.",
						"Warning",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (selectedOption != JOptionPane.YES_OPTION) return;
			}

			String selectedFilePathString = selectedFilePath.toString();
			editor.setOpenedFilePath(selectedFilePathString.contains(".") ?
					selectedFilePathString :
					String.format("%s.txt", selectedFilePathString));
		}

		try {
			Files.write(Paths.get(editor.getOpenedFilePath()), new DocumentTextExtractor(editor.getModel()).extractText().getBytes());
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(editor, "Unable to open given file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
