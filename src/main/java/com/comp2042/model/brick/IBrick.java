package com.comp2042.model.brick;

import com.comp2042.util.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * I-shaped Tetris piece (I-tetromino).
 * The iconic straight line piece, 4 blocks long.
 */
public final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixUtils.deepCopyList(brickMatrix);
    }

}
