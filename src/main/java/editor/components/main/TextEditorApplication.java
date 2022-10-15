package editor.components.main;

import editor.clipboard.ClipboardStack;
import editor.components.textarea.TextEditorComponent;
import editor.actions.helpers.binders.impl.InputActionBinder;
import editor.actions.helpers.binders.KeyActionBinder;
import editor.actions.helpers.binders.impl.ShortcutBinder;
import editor.constants.Labels;
import editor.constants.Shortcuts;
import editor.constants.SystemConstants;
import editor.plugins.Plugin;
import editor.plugins.PluginServiceLoader;
import editor.actions.textarea.CursorKeyListener;
import editor.actions.textarea.DeletionKeyListener;
import editor.actions.textarea.PrintableCharacterKeyListener;
import editor.components.model.TextEditorModel;
import editor.components.statusbar.StatusBar;
import editor.actions.toolbar.AbstractToolBarAction;
import editor.actions.toolbar.edit.*;
import editor.actions.toolbar.file.OpenFileAction;
import editor.actions.toolbar.file.SaveFileAction;
import editor.actions.toolbar.navigation.MoveToEndAction;
import editor.actions.toolbar.navigation.MoveToStartAction;
import editor.undo.UndoManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class TextEditorApplication extends JFrame {

    private final TextEditorModel model;
    private final UndoManager undoManager;
    private final ClipboardStack clipboard;

    private final TextEditorComponent textArea;

    private List<Action> fileActions;
    private List<Action> editActions;
    private List<Action> navigationActions;

    private final PluginServiceLoader pluginServiceLoader;
    private List<Plugin> plugins;

    private String openedFilePath;

    public TextEditorApplication(TextEditorModel model) {
        this.model = model;
        undoManager = UndoManager.getInstance();
        clipboard = ClipboardStack.getInstance();
        pluginServiceLoader = PluginServiceLoader.getInstance();

        textArea = new TextEditorComponent(model);
        initTextEditorComponent();

        initCursorObserver();
        initTextObserver();

        initKeyListeners();
        initSelectionListeners();

        initFileActions();
        initEditActions();
        initNavigationActions();
        initPlugins();

        initGUI();
    }

    private void initTextEditorComponent() {
        textArea.setFocusTraversalKeysEnabled(false);
        textArea.setBackground(SystemConstants.BACKGROUND_COLOR);
        add(textArea);
    }

    private void initCursorObserver() {
        model.addCursorObserver(location -> {
            textArea.revalidate();
            textArea.repaint();
        });
    }

    private void initTextObserver() {
        model.addTextObserver(isEmpty -> {
            textArea.revalidate();
            textArea.repaint();
        });
    }

    private void initKeyListeners() {
        textArea.addKeyListener(new CursorKeyListener(model));
        textArea.addKeyListener(new PrintableCharacterKeyListener(model));
        textArea.addKeyListener(new DeletionKeyListener(model));
    }

    private void initSelectionListeners() {
        InputActionBinder binder = new InputActionBinder(textArea);

        binder.bind(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK), e -> model.moveSelectionUp());
        binder.bind(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK), e -> model.moveSelectionDown());
        binder.bind(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK), e -> model.moveSelectionLeft());
        binder.bind(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK), e -> model.moveSelectionRight());
    }

    private void initFileActions() {
        Action openFileAction = new OpenFileAction(Labels.OPEN, this);
        Action saveFileAction = new SaveFileAction(Labels.SAVE, this);

        KeyActionBinder binder = new ShortcutBinder();

        binder.bind(KeyStroke.getKeyStroke(Shortcuts.OPEN), openFileAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.SAVE), saveFileAction);

        fileActions = List.of(openFileAction, saveFileAction, new AbstractToolBarAction(Labels.EXIT, this) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editor.dispose();
            }
        });
    }

    private void initEditActions() {
        UndoAction undoAction = new UndoAction(Labels.UNDO, this);
        RedoAction redoAction = new RedoAction(Labels.REDO, this);
        CutAction cutAction = new CutAction(Labels.CUT, this);
        CopyAction copyAction = new CopyAction(Labels.COPY, this);
        PasteAction pasteAction = new PasteAction(Labels.PASTE, this);
        PasteAndTakeAction pasteAndTakeAction = new PasteAndTakeAction(Labels.PASTE_AND_TAKE, this);
        DeleteSelectionAction deleteSelectionAction = new DeleteSelectionAction(Labels.DELETE_SELECTION, this);
        ClearAction clearAction = new ClearAction(Labels.CLEAR, this);

        KeyActionBinder binder = new ShortcutBinder();

        binder.bind(KeyStroke.getKeyStroke(Shortcuts.UNDO), undoAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.REDO), redoAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.CUT), cutAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.COPY), copyAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.PASTE), pasteAction);
        binder.bind(KeyStroke.getKeyStroke(Shortcuts.PASTE_AND_TAKE), pasteAndTakeAction);

        undoManager.addUndoObserver(undoAction);
        undoManager.addRedoObserver(redoAction);

        model.addSelectionObserver(cutAction);
        model.addSelectionObserver(copyAction);
        model.addSelectionObserver(deleteSelectionAction);

        clipboard.addClipboardObserver(pasteAction);
        clipboard.addClipboardObserver(pasteAndTakeAction);

        model.addTextObserver(clearAction);

        editActions = List.of(undoAction, redoAction, cutAction, copyAction, pasteAction, pasteAndTakeAction, deleteSelectionAction, clearAction);

        editActions.forEach(action -> action.setEnabled(false));
        clearAction.updateText(model.isEmpty());
    }

    private void initNavigationActions() {
        navigationActions = List.of(new MoveToStartAction(Labels.MOVE_TO_START, this), new MoveToEndAction(Labels.MOVE_TO_END, this));
    }

    private void initPlugins() {
        plugins = pluginServiceLoader.loadPlugins();
    }

    private void initGUI() {
        setSize(SystemConstants.INITIAL_WIDTH, SystemConstants.INITIAL_HEIGHT);
        setTitle(SystemConstants.TITLE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setFocusTraversalKeysEnabled(false);

        initMenuBar();
        initToolBar();
        initStatusBar();
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(Labels.FILE_MENU);
        fileActions.forEach(fileMenu::add);

        JMenu editMenu = new JMenu(Labels.EDIT_MENU);
        editActions.forEach(editMenu::add);

        JMenu navigationMenu = new JMenu(Labels.NAVIGATION_MENU);
        navigationActions.forEach(navigationMenu::add);

        JMenu pluginsMenu = new JMenu(Labels.PLUGIN_MENU);
        plugins.stream()
                .map(plugin -> {
                    JMenuItem jMenuItem = new JMenuItem(new AbstractAction(plugin.getName()) {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            plugin.execute(model, undoManager, clipboard);
                        }
                    });
                    jMenuItem.setToolTipText(plugin.getDescription());
                    return jMenuItem;
                }).forEach(pluginsMenu::add);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(navigationMenu);
        menuBar.add(pluginsMenu);

        setJMenuBar(menuBar);
    }

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setFocusTraversalKeysEnabled(false);

        editActions.forEach(toolBar::add);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private void initStatusBar() {
        add(new StatusBar(model), BorderLayout.PAGE_END);
    }

    public TextEditorModel getModel() {
        return model;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public ClipboardStack getClipboard() {
        return clipboard;
    }

    public String getOpenedFilePath() {
        return openedFilePath;
    }

    public void setOpenedFilePath(String openedFilePath) {
        this.openedFilePath = openedFilePath;
    }
}
