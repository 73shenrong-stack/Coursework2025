package com.comp2042.model.board;

import com.comp2042.model.data.LineClearResult;
import com.comp2042.model.game.Score;
import com.comp2042.model.data.ViewData;

/**
 * Core interface for the Tetris game board.
 * Defines all essential operations for piece movement, rotation, collision detection and game state management.
 */
public interface Board {

    /**
     * Attempts to move the current falling brick down by one row.
     *
     * @return true if the brick was successfully moved down, false if blocked by collision
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current falling brick one column to the left.
     *
     * @return true if the brick was successfully moved left, false if blocked by collision
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current falling brick one column to the right.
     *
     * @return true if the brick was successfully moved right, false if blocked by collision
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counter-clockwise using the Super Rotation System (SRS).
     * Attempts wall kicks if the basic rotation results in a collision.
     *
     * @return true if rotation was successful (including wall kicks), false if rotation is blocked
     */
    boolean rotateLeftBrick();

    /**
     * Spawns a new brick at the top of the board.
     * Checks for collision at spawn position to detect game over condition.
     *
     * @return true if the new brick collides with existing pieces (game over), false otherwise
     */
    boolean createNewBrick();

    /**
     * Retrieves the current state of the game board matrix.
     * Each cell contains an integer representing the brick color (0 for empty).
     *
     * @return 2D array representing the board state
     */
    int[][] getBoardMatrix();

    /**
     * Gets all data needed for rendering the current game state.
     * Includes current brick position, next brick preview, shadow position, and held brick.
     *
     * @return ViewData object containing all rendering information
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the background board matrix.
     * Called when a brick lands and becomes part of the static game field.
     */
    void mergeBrickToBackground();

    /**
     * Checks for and removes all completed horizontal lines.
     * Applies quadratic scoring based on number of lines cleared simultaneously.
     *
     * @return LineClearResult containing number of lines removed, updated board matrix, and score bonus
     */
    LineClearResult clearRows();

    /**
     * Gets the current score tracker for the game.
     *
     * @return Score object tracking the player's current score
     */
    Score getScore();

    /**
     * Resets the game to initial state.
     * Clears the board, resets score, spawns first brick, and clears held brick.
     */
    void newGame();

    /**
     * Calculates the Y position where the current brick would land if hard-dropped.
     * Used to render the ghost/shadow piece showing landing position.
     *
     * @return Y coordinate of the shadow position
     */
    int getShadowYPosition();

    /**
     * Attempts to hold the current brick for later use.
     * If no brick is held, stores current brick and spawns a new one.
     * If a brick is already held, swaps it with the current brick.
     * Can only be used once per brick placement.
     *
     * @return true if hold was successful, false if hold was already used for current brick
     */
    boolean holdBrick();

    /**
     * Clears all pieces from the board matrix, leaving it empty.
     * Used in Zen mode to prevent game over by clearing the board when pieces reach the top.
     */
    void clearBoard();
}