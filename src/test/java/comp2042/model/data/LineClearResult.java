package comp2042.model.data;

import comp2042.util.MatrixUtils;

/**
 * Result of line clearing operation
 */
public final class LineClearResult {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    public LineClearResult(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public int[][] getNewMatrix() {
        return MatrixUtils.copy(newMatrix);
    }

    public int getScoreBonus() {
        return scoreBonus;
    }
}