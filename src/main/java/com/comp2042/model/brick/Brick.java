package com.comp2042.model.brick;

import java.util.List;

/**
 * Represents a Tetris piece (tetromino) with its rotation states.
 * In Tetris, all pieces are built from four connected squares called "minos", forming shapes called "tetrominoes" (tetra = four).
 * This interface defines the contract for all seven standard Tetris pieces.
 */
public interface Brick {

    /**
     * Returns all rotation states for this piece as a list of 2D matrices.
     *
     * This method provides access to every possible orientation of the piece, ordered from rotation state 0 (spawn orientation) through subsequent counter-clockwise rotations.
     * The game uses these matrices for rendering, collision detection and rotation mechanics.
     *
     * @return a list of 4×4 integer matrices representing all rotation states,
     *         ordered from spawn orientation (index 0) through counter-clockwise
     *         rotations. Never returns null or an empty list.
     */
    List<int[][]> getShapeMatrix();
}
