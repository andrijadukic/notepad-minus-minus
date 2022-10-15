package editor.components.model.location;

public record Location(int line, int column) implements Comparable<Location> {

    public static Location start() {
        return new Location(0, 0);
    }

    @Override
    public int compareTo(Location other) {
        int lineDifference = this.line - other.line;

        if (lineDifference != 0) return lineDifference;

        return this.column - other.column;
    }

    @Override
    public String toString() {
        return String.format("%d:%d", line + 1, column + 1);
    }
}
