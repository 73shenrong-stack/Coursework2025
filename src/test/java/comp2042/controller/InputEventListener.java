package comp2042.controller;

import comp2042.model.data.MoveDownResult;
import comp2042.model.data.MoveEvent;
import comp2042.model.data.ViewData;

/**
 * Interface for handling game input events
 */
public interface InputEventListener {

    //Handle piece moving down
    MoveDownResult onDownEvent(MoveEvent event);

    // Handle piece moving left
    ViewData onLeftEvent(MoveEvent event);

    // Handle piece moving right
    ViewData onRightEvent(MoveEvent event);

    // Handle piece rotation
    ViewData onRotateEvent(MoveEvent event);

    // Create a new game
    void createNewGame();
}