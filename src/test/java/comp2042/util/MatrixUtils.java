package comp2042.util;

import comp2042.constants.GameConstants;
import comp2042.model.data.LineClearResult;

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

    // Check if a brick collides with the board or goes out of bounds

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

    // Check if coordinates are out of bounds

    private static boolean isOutOfBounds(int[][] matrix, int targetX, int targetY) {
        return targetX < 0 || targetY >= matrix.length || targetY < 0 || targetX >= matrix[0].length;
    }

    // Create a deep copy of a 2D array
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

    // Merge a brick into the board matrix

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

    // Check for and clear completed lines

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

    // Create a deep copy of a list of 2D arrays

    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixUtils::copy).collect(Collectors.toList());
    }
}