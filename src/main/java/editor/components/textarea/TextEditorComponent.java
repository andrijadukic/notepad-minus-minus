package editor.components.textarea;

import editor.components.model.TextEditorModel;
import editor.components.model.location.Location;
import editor.components.model.location.LocationRange;
import editor.constants.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class TextEditorComponent extends JComponent {

    private final TextEditorModel model;

    private static final int LEFT_MARGIN = 5;
    private static final int LINE_BOTTOM_MARGIN = 5;

    public TextEditorComponent(TextEditorModel model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        requestFocusInWindow();

        paintBackground(graphics);
        drawCursor(graphics);
        drawLines(graphics);
        drawSelection(graphics);
    }

    private void paintBackground(Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(oldColor);
    }

    private void drawCursor(Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(SystemConstants.TEXT_COLOR);

        Location cursorLocation = model.getCursorLocation();
        int lineNumber = cursorLocation.line();
        int columnNumber = cursorLocation.column();

        String currentLine = model.lineAt(lineNumber);

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        int x = fontMetrics.stringWidth(currentLine.substring(0, columnNumber)) + LEFT_MARGIN;
        int y1 = lineNumber * lineHeight;
        int y2 = y1 + lineHeight;

        graphics.drawLine(x, y1 + LINE_BOTTOM_MARGIN, x, y2);

        graphics.setColor(oldColor);
    }

    private void drawLines(Graphics graphics) {
        Color oldColor = graphics.getColor();

        graphics.setColor(SystemConstants.TEXT_COLOR);

        int lineHeight = graphics.getFontMetrics().getHeight();
        int lineNumber = 1;
        for (Iterator<String> it = model.allLines(); it.hasNext(); ) {
            String line = it.next();
            graphics.drawString(line, LEFT_MARGIN, lineNumber * lineHeight);
            lineNumber++;
        }

        graphics.setColor(oldColor);
    }

    private void drawSelection(Graphics graphics) {
        if (model.getSelectionRange() == null) return;

        Color oldColor = graphics.getColor();
        graphics.setColor(SystemConstants.SELECTION_COLOR);

        LocationRange range = model.getSelectionRange();
        Location start = range.getStart();
        Location end = range.getEnd();

        if (start.compareTo(end) > 0) {
            Location temp = start;
            start = end;
            end = temp;
        }

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        if (start.line() == end.line()) {
            String line = model.lineAt(start.line());
            int width = fontMetrics.stringWidth(line.substring(start.column(), end.column()));
            int x = LEFT_MARGIN + fontMetrics.stringWidth(line.substring(0, start.column()));
            graphics.fillRect(x, start.line() * lineHeight + LINE_BOTTOM_MARGIN, width, lineHeight);
        } else {
            String firstLine = model.lineAt(start.line());
            int width = fontMetrics.stringWidth(firstLine.substring(0, start.column()));
            int x = LEFT_MARGIN + width;
            graphics.fillRect(x, start.line() * lineHeight + LINE_BOTTOM_MARGIN, getWidth() - width, lineHeight);

            for (int i = start.line() + 1, n = end.line(); i < n; i++) {
                graphics.fillRect(LEFT_MARGIN, i * lineHeight + LINE_BOTTOM_MARGIN, getWidth(), lineHeight);
            }

            String lastLine = model.lineAt(end.line());
            width = fontMetrics.stringWidth(lastLine.substring(0, end.column()));
            graphics.fillRect(LEFT_MARGIN, end.line() * lineHeight + LINE_BOTTOM_MARGIN, width, lineHeight);
        }

        graphics.setColor(oldColor);
    }
}