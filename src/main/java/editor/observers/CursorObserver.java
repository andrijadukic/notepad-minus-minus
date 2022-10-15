package editor.observers;

import editor.components.model.location.Location;

public interface CursorObserver {

	void updateCursorLocation(Location location);
}
