package editor.undo;

import editor.actions.model.EditAction;
import editor.observers.StackObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoManager {

    private static UndoManager instance;

    private final Stack<EditAction> undoStack;
    private final Stack<EditAction> redoStack;

    private final List<StackObserver> undoObservers;
    private final List<StackObserver> redoObservers;

    private UndoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        undoObservers = new ArrayList<>();
        redoObservers = new ArrayList<>();
    }

    public static UndoManager getInstance() {
        if (instance == null) {
            instance = new UndoManager();
        }
        return instance;
    }

    public void undo() {
        EditAction action = undoStack.pop();

        action.executeUndo();
        notifyUndoObservers();

        redoStack.push(action);
        notifyRedoObservers();
    }

    public void redo() {
        EditAction action = redoStack.pop();

        action.executeDo();
        notifyRedoObservers();

        undoStack.push(action);
        notifyUndoObservers();
    }

    public void push(EditAction action) {
        redoStack.clear();
        notifyRedoObservers();
        undoStack.push(action);
        notifyUndoObservers();
    }

    public void addUndoObserver(StackObserver observer) {
        undoObservers.add(observer);
    }

    public void removeUndoObserver(StackObserver observer) {
        undoObservers.remove(observer);
    }

    private void notifyUndoObservers() {
        undoObservers.forEach(observer -> observer.updateStack(undoStack.isEmpty()));
    }

    public void addRedoObserver(StackObserver observer) {
        redoObservers.add(observer);
    }

    public void removeRedoObserver(StackObserver observer) {
        redoObservers.remove(observer);
    }

    private void notifyRedoObservers() {
        redoObservers.forEach(observer -> observer.updateStack(redoStack.isEmpty()));
    }
}
