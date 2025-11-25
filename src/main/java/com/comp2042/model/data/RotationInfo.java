package com.comp2042.model.data;

import com.comp2042.util.MatrixUtils;

/**
 * Information about the next rotation state
 */

public final class RotationInfo {

    private final int[][] shape;
    private final int position;

    public RotationInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixUtils.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}