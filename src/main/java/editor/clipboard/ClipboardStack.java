package editor.clipboard;

import editor.observers.ClipboardObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {

    private static ClipboardStack instance;

    private final Stack<String> stack;

    private final List<ClipboardObserver> clipboardObservers;

    private ClipboardStack() {
        stack = new Stack<>();
        clipboardObservers = new ArrayList<>();
    }

    public static ClipboardStack getInstance() {
        if (instance == null) {
            instance = new ClipboardStack();
        }
        return instance;
    }

    public void push(String text) {
        stack.push(text);
        notifyClipboardObservers();
    }

    public String pop() {
        String text = stack.pop();

        notifyClipboardObservers();

        return text;
    }

    public String peek() {
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void addClipboardObserver(ClipboardObserver observer) {
        clipboardObservers.add(observer);
    }

    public boolean removeClipboardObserver(ClipboardObserver observer) {
        return clipboardObservers.remove(observer);
    }

    public void notifyClipboardObservers() {
        clipboardObservers.forEach(observer -> observer.updateClipboard(stack.isEmpty()));
    }
}
