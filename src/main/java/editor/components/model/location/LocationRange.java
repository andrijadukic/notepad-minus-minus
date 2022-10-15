package editor.components.model.location;

public class LocationRange {

    private Location start;
    private Location end;

    public LocationRange(Location location) {
        start = end = location;
    }

    public LocationRange(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    public LocationRange copy() {
        return new LocationRange(start, end);
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}
