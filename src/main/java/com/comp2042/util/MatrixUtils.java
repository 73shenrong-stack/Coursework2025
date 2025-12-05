package com.comp2042.util;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.data.LineClearResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for matrix operations on the Tetris board
 */
public class MatrixUtils {

    // Private constructor to prevent instantiation
    private MatrixUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Checks if a brick collides with the board or goes out of bounds.
     *
     * @param matrix the game board matrix representing filled positions
     * @param brick  the 2D array representing the brick shape
     * @param x      the x-coordinate (column) for the brick's top-left corner
     * @param y      the y-coordinate (row) for the brick's top-left corner
     * @return       true if there is a collision or the brick is out of bounds; false otherwise
     */
    public static boolean hasCollision(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (isOutOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

/**
 * Checks if the specified coordinates are outside the matrix boundaries.
 *
 * @param matrix  the game board matrix to check against
 * @param targetX the x-coordinate (column) to check
 * @param targetY the y-coordinate (row) to check
 * @return true if the coordinates are out of bounds; false otherwise
 */

    private static boolean isOutOfBounds(int[][] matrix, int targetX, int targetY) {
        return targetX < 0 || targetY >= matrix.length || targetY < 0 || targetX >= matrix[0].length;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     *
     * @param original the 2D array to copy
     * @return a new 2D array containing the same values as the original
     */
    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] row = original[i];
            int length = row.length;
            copy[i] = new int[length];
            System.arraycopy(row, 0, copy[i], 0, length);
        }
        return copy;
    }

    /**
     * Merges a brick into the board matrix at the specified position.
     *
     * @param filledFields the current game board matrix
     * @param brick        the 2D array representing the brick shape
     * @param x            the x-coordinate (column) for the brick's top-left corner
     * @param y            the y-coordinate (row) for the brick's top-left corner
     * @return a new matrix with the brick merged into the board
     */

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] result = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    result[targetY][targetX] = brick[j][i];
                }
            }
        }
        return result;
    }

    /**
     * Checks for and clears completed lines from the matrix.
     *
     * @param matrix the current game board matrix to check for completed lines
     * @return a LineClearResult containing the number of lines cleared, the updated matrix, and the score bonus earned
     * @see LineClearResult
     * @see GameConstants#LINE_CLEAR_BASE_SCORE
     */

    public static LineClearResult clearCompletedLines(final int[][] matrix) {
        int[][] temp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        // Find complete rows
        for (int i = 0; i < matrix.length; i++) {
            int[] tempRow = new int[matrix[i].length];
            boolean isComplete = true;

            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    isComplete = false;
                }
                tempRow[j] = matrix[i][j];
            }

            if (isComplete) {
                clearedRows.add(i);
            } else {
                newRows.add(tempRow);
            }
        }

        // Rebuild matrix from bottom up
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                temp[i] = row;
            } else {
                break;
            }
        }

        // Calculate score bonus (quadratic scoring)
        int scoreBonus = GameConstants.LINE_CLEAR_BASE_SCORE * clearedRows.size() * clearedRows.size();

        return new LineClearResult(clearedRows.size(), temp, scoreBonus);
    }

    /**
     * Creates a deep copy of a list containing 2D integer arrays.
     *
     * @param list the list of 2D arrays to copy
     * @return a new list containing deep copies of all 2D arrays from the original list
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixUtils::copy).collect(Collectors.toList());
    }
}