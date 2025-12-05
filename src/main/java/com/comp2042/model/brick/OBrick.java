package com.comp2042.model.brick;

import com.comp2042.util.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * O-shaped Tetris piece (O-tetromino).
 * 2x2 square block that looks the same in all rotations.
 */
public final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixUtils.deepCopyList(brickMatrix);
    }

}
