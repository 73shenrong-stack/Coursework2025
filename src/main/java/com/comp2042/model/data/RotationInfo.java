package com.comp2042.model.data;

import com.comp2042.util.MatrixUtils;

/**
 * Immutable data class containing information about a brick's rotation state.
 * Stores the shape matrix for a specific rotation and its position index.
 */

public final class RotationInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a new rotation info object.
     *
     * @param shape the 4x4 shape matrix for this rotation
     * @param position the rotation index (0-3 for most pieces)
     */
    public RotationInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets the shape matrix for this rotation.
     * Returns a defensive copy to prevent external modification.
     *
     * @return 4x4 matrix representing the brick shape
     */
    public int[][] getShape() {
        return MatrixUtils.copy(shape);
    }

    /**
     * Gets the rotation position index.
     *
     * @return the rotation index (0 to numRotations-1)
     */
    public int getPosition() {
        return position;
    }
}