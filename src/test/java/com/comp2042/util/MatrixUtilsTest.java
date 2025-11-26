package com.comp2042.util;

import com.comp2042.model.data.LineClearResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MatrixUtilsTest {

    @Test
    @DisplayName("Should detect collision when brick overlaps with board pieces")
    void testHasCollision_withOverlap() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 1, 1, 0},
                {1, 1, 1, 1}
        };
        int[][] brick = {{1, 1}, {1, 1}};
        assertTrue(MatrixUtils.hasCollision(board, brick, 0, 2));
    }

    @Test
    @DisplayName("Should not detect collision when brick fits in empty space")
    void testHasCollision_noOverlap() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 1, 1, 1}
        };
        int[][] brick = {{1, 1}, {1, 1}};
        assertFalse(MatrixUtils.hasCollision(board, brick, 1, 1));
    }

    @Test
    @DisplayName("Should detect collision at all boundaries")
    void testHasCollision_boundaries() {
        int[][] board = {{0, 0, 0, 0}, {0, 0, 0, 0}};
        int[][] brick = {{1, 1}, {1, 1}};

        assertTrue(MatrixUtils.hasCollision(board, brick, -1, 0), "Left bound");
        assertTrue(MatrixUtils.hasCollision(board, brick, 3, 0), "Right bound");
        assertTrue(MatrixUtils.hasCollision(board, brick, 0, 1), "Bottom bound");
    }

    @Test
    @DisplayName("Should correctly merge brick into board matrix")
    void testMerge_correctPlacement() {
        int[][] board = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        int[][] brick = {{2, 2}, {2, 2}};

        int[][] result = MatrixUtils.merge(board, brick, 1, 0);

        assertEquals(2, result[0][1]);
        assertEquals(2, result[0][2]);
        assertEquals(2, result[1][1]);
        assertEquals(2, result[1][2]);
        assertEquals(0, result[0][0]);
    }

    @Test
    @DisplayName("Should not modify original board when merging")
    void testMerge_originalUnchanged() {
        int[][] board = {{0, 0}, {0, 0}};
        int[][] brick = { {1, 1}, {1, 1} };

        int[][] result = MatrixUtils.merge(board, brick, 0, 0);

        assertEquals(0, board[0][0]);
        assertEquals(1, result[0][0]);
    }

    @Test
    @DisplayName("Should clear single completed line and calculate score")
    void testClearCompletedLines_singleLine() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 1, 0, 1}
        };

        LineClearResult result = MatrixUtils.clearCompletedLines(board);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
        assertArrayEquals(new int[]{0, 1, 0, 1}, result.getNewMatrix()[3]);
    }

    @Test
    @DisplayName("Should clear multiple lines with quadratic scoring")
    void testClearCompletedLines_multipleLines() {
        int[][] board = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {3, 3, 3, 3}
        };

        LineClearResult result = MatrixUtils.clearCompletedLines(board);

        assertEquals(3, result.getLinesRemoved());
        assertEquals(450, result.getScoreBonus()); // 50 * 3 * 3
    }

    @Test
    @DisplayName("Should handle Tetris (4 lines) with correct scoring")
    void testClearCompletedLines_tetris() {
        int[][] board = {
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {3, 3, 3, 3},
                {4, 4, 4, 4}
        };

        LineClearResult result = MatrixUtils.clearCompletedLines(board);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus()); // 50 * 4 * 4
    }

    @Test
    @DisplayName("Should not clear incomplete lines")
    void testClearCompletedLines_noCompleteLines() {
        int[][] board = {{1, 0, 1, 0}, {0, 1, 0, 1}, {1, 1, 0, 1}};

        LineClearResult result = MatrixUtils.clearCompletedLines(board);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should preserve line order after clearing")
    void testClearCompletedLines_orderPreserved() {
        int[][] board = {
                {0, 0, 0, 0},
                {1, 2, 3, 0},
                {4, 4, 4, 4},
                {5, 6, 7, 8}
        };

        LineClearResult result = MatrixUtils.clearCompletedLines(board);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(2, result.getLinesRemoved());
        assertArrayEquals(new int[]{1, 2, 3, 0}, newMatrix[3]);
    }

    @Test
    @DisplayName("Should create deep copy without affecting original")
    void testCopy_deepCopy() {
        int[][] original = {{1, 2, 3}, {4, 5, 6}};

        int[][] copy = MatrixUtils.copy(original);
        copy[0][0] = 999;

        assertEquals(1, original[0][0]);
        assertEquals(999, copy[0][0]);
    }

    @Test
    @DisplayName("Should copy empty matrix correctly")
    void testCopy_emptyMatrix() {
        int[][] original = {{0, 0}, {0, 0}};

        int[][] copy = MatrixUtils.copy(original);

        assertArrayEquals(original[0], copy[0]);
        assertNotSame(original[0], copy[0]);
    }
}
