package com.comp2042.controller;

import com.comp2042.model.data.MoveDownResult;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;

/**
 * Interface for handling game input events
 * Defines callbacks for all piece movement, rotation, and game lifecycle events.
 * Implementations should process the event, update game state and return updated view data for rendering.
 */
public interface InputEventListener {

    /**
     * Handles a downward movement event for the falling piece.
     * This is called both for user-initiated soft drops and automatic piece falling.
     * If the piece lands, handles merging, line clears, and spawning the next piece.
     *
     * @param event the move event containing event type and source (USER or THREAD)
     * @return MoveDownResult containing line clear information and updated view data
     */
    MoveDownResult onDownEvent(MoveEvent event);

    /**
     * Handles a leftward movement event for the falling piece.
     * Attempts to move the piece one column to the left if not blocked by walls or other pieces.
     *
     * @param event the move event containing event type and source
     * @return updated view data after attempting the move
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a rightward movement event for the falling piece.
     * Attempts to move the piece one column to the right if not blocked by walls or other pieces.
     *
     * @param event the move event containing event type and source
     * @return updated view data after attempting the move
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event for the falling piece.
     * Should implement SRS (Super Rotation System) rotation with wall kick to allow rotation in tight spaces.
     *
     * @param event the move event containing event type and source
     * @return updated view data after attempting the rotation
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Creates a new game, resetting all game state to initial conditions.
     * Should clear the board, reset score to 0, clear held brick and spawn the first piece.
     */
    void createNewGame();
}