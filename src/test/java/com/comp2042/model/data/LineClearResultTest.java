package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class LineClearResultTest {

    @Test
    @DisplayName("Should store line clear data correctly")
    void testLineClearData() {
        int[][] matrix = {{0, 0}, {0, 0}};
        LineClearResult result = new LineClearResult(2, matrix, 200);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should return defensive copy of matrix")
    void testMatrixDefensiveCopy() {
        int[][] matrix = {{1, 2}, {3, 4}};
        LineClearResult result = new LineClearResult(1, matrix, 50);

        int[][] copy = result.getNewMatrix();
        copy[0][0] = 999;

        assertEquals(1, result.getNewMatrix()[0][0]);
        assertNotSame(matrix, copy);
    }

    @Test
    @DisplayName("Should handle zero lines cleared")
    void testZeroLinesCleared() {
        int[][] matrix = {{0, 0}};
        LineClearResult result = new LineClearResult(0, matrix, 0);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    @DisplayName("Should handle maximum line clear (Tetris)")
    void testTetrisClear() {
        int[][] matrix = new int[4][4];
        LineClearResult result = new LineClearResult(4, matrix, 800);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
    }
}
