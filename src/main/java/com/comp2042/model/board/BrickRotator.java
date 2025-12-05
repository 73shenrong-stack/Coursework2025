package com.comp2042.model.board;

import com.comp2042.model.data.RotationInfo;
import com.comp2042.model.brick.Brick;

/**
 * Manages the rotation state of Tetris bricks (tetrominoes).
 *This class maintains the current brick and its rotation index, allowing the game to track which rotation state of a piece is currently being used.
 * Each brick type can have multiple rotation states (e.g.,the I-piece has 2 states, while the J, L, S, T, and Z pieces have 4 states).
 * @see Brick
 */
public class BrickRotator {

    public Brick brick;
    private int currentShape = 0;

    /**
     * Gets the current shape matrix for the brick at its current rotation state.
     *
     * @return a 2D integer array representing the brick's current rotated shape
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index directly.
     *
     * @param currentShape the rotation index to set
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick and resets the rotation to the initial state (index 0).
     *
     * @param brick the brick to set as the current brick
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Gets the current brick instance.
     *
     * @return the current brick being managed by this rotator
     */
    public Brick getBrick() {
        return brick;
    }

    /**
     * Gets the current rotation index.
     *
     * @return the current rotation index (0-based)
     */
    public int getRotationIndex() {
        return currentShape;
    }

    /**
     * Sets the rotation index to a specific value.
     *
     * @param index the rotation index to set (should be within valid range)
     */
    public void setRotationIndex(int index) {
        this.currentShape = index;
    }

}
