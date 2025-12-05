package com.comp2042.model.brick;

import com.comp2042.util.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Z-shaped Tetris piece (Z-tetromino).
 * Zigzag shape pointing to the left (mirror of S-piece).
 */
public final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixUtils.deepCopyList(brickMatrix);
    }
}
