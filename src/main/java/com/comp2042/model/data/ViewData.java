package com.comp2042.model.data;

import com.comp2042.util.MatrixUtils;

/**
 * Immutable data class containing all information needed to render the game state.
 * Serves as a data transfer object (DTO) between the game model and view, packaging together the current brick, its position, preview information and shadow/ghost piece position.
 */

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int shadowYPosition;
    private final int[][] heldBrickData;

    /**
     * Constructs a new view data object with all rendering information.
     *
     * @param brickData the current falling brick's shape matrix
     * @param xPosition the X position (column) of the brick
     * @param yPosition the Y position (row) of the brick
     * @param nextBrickData the next brick's shape matrix
     * @param shadowYPosition the Y position of the shadow/ghost piece
     * @param heldBrickData the held brick's shape matrix, or null if none held
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    int[][] nextBrickData, int shadowYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.shadowYPosition = shadowYPosition;
        this.heldBrickData = heldBrickData;
    }

    /**
     * Gets the held brick's shape matrix.
     * Returns a defensive copy to prevent modification, or null if no brick is held.
     *
     * @return 4x4 matrix of the held brick, or null if none
     */
    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixUtils.copy(heldBrickData) : null;
    }

    /**
     * Gets the current falling brick's shape matrix.
     * Returns a defensive copy to prevent modification.
     *
     * @return 4x4 matrix representing the current brick
     */
    public int[][] getBrickData() {
        return MatrixUtils.copy(brickData);
    }

    /**
     * Gets the X position (column) of the current brick.
     *
     * @return X coordinate on the board (0 to BOARD_WIDTH-1)
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Gets the Y position (row) of the current brick.
     *
     * @return Y coordinate on the board (0 to BOARD_HEIGHT-1)
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Gets the next brick's shape matrix for preview.
     * Returns a defensive copy to prevent modification, or null if not available.
     *
     * @return 4x4 matrix of the next brick, or null
     */
    public int[][] getNextBrickData() {
        return nextBrickData != null ? MatrixUtils.copy(nextBrickData) : null;
    }

    /**
     * Gets the Y position of the shadow/ghost piece.
     * This is where the current brick would land if hard-dropped.
     * Used to render the landing preview for the player.
     *
     * @return Y coordinate where the brick would land
     */
    public int getShadowYPosition() {
        return shadowYPosition;
    }
}