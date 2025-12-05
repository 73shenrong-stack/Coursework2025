package com.comp2042.model.data;

import com.comp2042.util.MatrixUtils;

/**
 * Immutable data class representing the result of a line clearing operation.
 * Contains information about how many lines were removed, the updated board matrix and the score bonus awarded.
 */
public final class LineClearResult {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a new line clear result.
     *
     * @param linesRemoved the number of lines removed
     * @param newMatrix the board matrix after clearing lines
     * @param scoreBonus the score bonus awarded
     */
    public LineClearResult(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed.
     *
     * @return number of lines cleared (0-4)
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the updated board matrix after line removal.
     * Returns a defensive copy to prevent external modification.
     *
     * @return 2D array representing the board state after clearing
     */
    public int[][] getNewMatrix() {
        return MatrixUtils.copy(newMatrix);
    }

    /**
     * Gets the score bonus awarded for this line clear.
     * Calculated using quadratic formula: BASE_SCORE × lines²
     *
     * @return score bonus points
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
