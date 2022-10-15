package editor.components.model;

import editor.actions.model.*;
import editor.components.model.location.Location;
import editor.components.model.location.LocationRange;
import editor.constants.SystemConstants;
import editor.observers.CursorObserver;
import editor.observers.SelectionObserver;
import editor.observers.TextObserver;
import editor.undo.UndoManager;

import java.util.*;

public class TextEditorModel {

    private List<String> lines;
    private List<String> roLines;

    private LocationRange selectionRange;

    private Location cursorLocation;

    private final List<CursorObserver> cursorObservers;
    private final List<TextObserver> textObservers;
    private final List<SelectionObserver> selectionObservers;

    private final UndoManager undoManager;

    private TextEditorModel() {
        lines = new ArrayList<>();
        roLines = Collections.unmodifiableList(lines);

        cursorLocation = Location.start();

        cursorObservers = new ArrayList<>();
        textObservers = new ArrayList<>();
        selectionObservers = new ArrayList<>();

        undoManager = UndoManager.getInstance();
    }

    public TextEditorModel(String sequence) {
        this();
        lines.addAll(Arrays.asList(sequence.split(SystemConstants.LINE_SEPARATORS_REGEX)));
    }

    public void addCursorObserver(CursorObserver cursorObserver) {
        cursorObservers.add(cursorObserver);
    }

    public void removeCursorObserver(CursorObserver cursorObserver) {
        cursorObservers.remove(cursorObserver);
    }

    public void notifyCursorObservers() {
        cursorObservers.forEach(cursorObserver -> cursorObserver.updateCursorLocation(cursorLocation));
    }

    public Location getCursorLocation() {
        return cursorLocation;
    }

    public void setCursorLocation(Location cursorLocation) {
        this.cursorLocation = cursorLocation;
        notifyCursorObservers();
    }

    public void moveCursorLeft() {
        int columnNumber = cursorLocation.column();
        int lineNumber = cursorLocation.line();

        if (columnNumber != 0) {
            setCursorLocation(new Location(lineNumber, columnNumber - 1));
        } else if (lineNumber != 0) {
            int newLine = lineNumber - 1;
            int newColumn = lines.get(newLine).length();
            setCursorLocation(new Location(newLine, newColumn));
        }
    }

    public void moveCursorRight() {
        int columnNumber = cursorLocation.column();
        int lineNumber = cursorLocation.line();

        if (columnNumber != lines.get(lineNumber).length()) {
            setCursorLocation(new Location(lineNumber, columnNumber + 1));
        } else if (lineNumber < lineCount() - 1) {
            setCursorLocation(new Location(lineNumber + 1, 0));
        }
    }

    public void moveCursorUp() {
        int currentLineNumber = cursorLocation.line();

        if (currentLineNumber == 0) return;

        int columnNumber = cursorLocation.column();
        int lineAboveNumber = currentLineNumber - 1;
        int lineAboveLength = lineAt(lineAboveNumber).length();

        setCursorLocation(lineAboveLength < columnNumber ?
                new Location(lineAboveNumber, lineAboveLength) :
                new Location(lineAboveNumber, columnNumber));
    }

    public void moveCursorDown() {
        int currentLineNumber = cursorLocation.line();

        if (currentLineNumber == lines.size() - 1) return;

        int columnNumber = cursorLocation.column();
        int nextLineNumber = currentLineNumber + 1;
        int nextLineLength = lineAt(nextLineNumber).length();

        setCursorLocation(nextLineLength < columnNumber ?
                new Location(nextLineNumber, nextLineLength) :
                new Location(nextLineNumber, columnNumber));
    }

    public void addSelectionObserver(SelectionObserver observer) {
        selectionObservers.add(observer);
    }

    public void removeSelectionObserver(SelectionObserver observer) {
        selectionObservers.remove(observer);
    }

    public void notifySelectionObservers() {
        selectionObservers.forEach(observer -> observer.updateTextSelection(selectionRange != null));
    }

    public LocationRange getSelectionRange() {
        return selectionRange != null ? selectionRange.copy() : null;
    }

    public void setSelectionRange(LocationRange selectionRange) {
        this.selectionRange = selectionRange;
        notifySelectionObservers();
    }

    public void moveSelectionUp() {
        if (selectionRange == null) {
            selectionRange = new LocationRange(cursorLocation);
        }

        moveCursorUp();
        selectionRange.setEnd(cursorLocation);

        notifySelectionObservers();
    }

    public void moveSelectionDown() {
        if (selectionRange == null) {
            selectionRange = new LocationRange(cursorLocation);
        }

        moveCursorDown();
        selectionRange.setEnd(cursorLocation);

        notifySelectionObservers();
    }

    public void moveSelectionLeft() {
        if (selectionRange == null) {
            selectionRange = new LocationRange(cursorLocation);
        }

        moveCursorLeft();
        selectionRange.setEnd(cursorLocation);

        notifySelectionObservers();
    }

    public void moveSelectionRight() {
        if (selectionRange == null) {
            selectionRange = new LocationRange(cursorLocation);
        }

        moveCursorRight();
        selectionRange.setEnd(cursorLocation);

        notifySelectionObservers();
    }

    public void deleteBefore() {
        EditAction deleteBeforeAction = new DeleteBeforeAction(this);
        deleteBeforeAction.executeDo();
        undoManager.push(deleteBeforeAction);
    }

    public void deleteAfter() {
        EditAction deleteAfterAction = new DeleteAfterAction(this);
        deleteAfterAction.executeDo();
        undoManager.push(deleteAfterAction);
    }

    public void deleteRange(LocationRange range) {
        if (range == null) return;

        EditAction deleteRangeAction = new DeleteRangeAction(this, range);
        deleteRangeAction.executeDo();
        undoManager.push(deleteRangeAction);
    }

    public void addTextObserver(TextObserver textObserver) {
        textObservers.add(textObserver);
    }

    public void removeTextObserver(TextObserver textObserver) {
        textObservers.remove(textObserver);
    }

    public void notifyTextObservers() {
        textObservers.forEach(observer -> observer.updateText(isEmpty()));
    }

    public void insert(char c) {
        EditAction insertCharacterAction = new InsertCharacterAction(this, c);
        insertCharacterAction.executeDo();
        undoManager.push(insertCharacterAction);
    }

    public void insertText(String text) {
        EditAction insertTextAction = new InsertTextAction(this, text);
        insertTextAction.executeDo();
        undoManager.push(insertTextAction);
    }

    public void clear() {
        EditAction clearAction = new ClearDocumentAction(this);
        clearAction.executeDo();
        undoManager.push(clearAction);
    }

    public boolean isEmpty() {
        return lines.size() == 1 && lines.get(0).isEmpty();
    }

    public void moveToStart() {
        setCursorLocation(Location.start());
    }

    public void moveToEnd() {
        int lastLineNumber = lineCount() - 1;
        String lastLine = lineAt(lastLineNumber);

        setCursorLocation(new Location(lastLineNumber, lastLine.length()));
    }

    public List<String> getLines() {
        return roLines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
        roLines = Collections.unmodifiableList(lines);

        notifyTextObservers();
    }

    public int lineCount() {
        return lines.size();
    }

    public String lineAt(int lineNumber) {
        return lines.get(lineNumber);
    }

    public Iterator<String> allLines() {
        return lines.iterator();
    }

    public Iterator<String> linesRange(int index1, int index2) {
        return lines.subList(index1, index2).iterator();
    }
}
