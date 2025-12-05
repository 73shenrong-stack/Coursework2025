package com.comp2042.model.board;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.brick.Brick;
import com.comp2042.model.brick.BrickGenerator;
import com.comp2042.model.brick.RandomBrickGenerator;
import com.comp2042.model.data.LineClearResult;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.game.Score;
import com.comp2042.util.MatrixUtils;

import java.awt.*;

/**
 * Represents the main Tetris board implementation that manages the game state, piece movement, rotations, line clearing, scoring, and held piece functionality.
 * This class acts as the central controller for Tetris gameplay, handling interactions between the board matrix, active brick, and supporting utilities.
 */
public class TetrisBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean hasHeld = false;

    /**
     * Constructs a new {@code TetrisBoard} with the specified dimensions.
     *
     * @param width  the width of the board in cells
     * @param height the height of the board in cells
     */
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Holds the current brick, swapping it with the previously held brick if one exists.
     * A player can only hold once per turn. If no brick is currently held, the active brick is stored and a new brick is spawned.
     * If a brick is already held, it is swapped with the current brick.
     *
     * @return true if the hold action was successful, false if already held this turn
     */
    @Override
    public boolean holdBrick() {
        if (hasHeld) {
            return false;
        }

        Brick currentBrick = brickRotator.brick;

        if (heldBrick == null) {
            heldBrick = currentBrick;
            createNewBrick();
            hasHeld = true;
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);
            brickRotator.setCurrentShape(0);
            hasHeld = true;
        }
        currentOffset = new Point(GameConstants.SPAWN_X, GameConstants.SPAWN_Y);
        return true;
    }

    /**
     * Attempts to move the current brick down by one cell.
     *
     * @return true if the move was successful, false if blocked by collision
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixUtils.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixUtils.hasCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current brick left by one cell.
     *
     * @return true if the move was successful, false if blocked by collision
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixUtils.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixUtils.hasCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to move the current piece right by one column.
     *
     * @return true if the piece moved successfully, false if blocked by wall or pieces
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixUtils.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixUtils.hasCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempts to rotate the current piece counter-clockwise using SRS wall kicks.
     * When a piece cannot rotate in place, the system automatically tests several nearby positions (wall kicks) to find a valid rotation.
     * This makes rotation feel more forgiving and natural, especially near walls and other pieces.
     *
     * @return true if rotation succeeded (with or without kick), false if all kicks failed
     * @see SRSKickData#getKicks(Brick, int, int, boolean)
     */
    @Override
    public boolean rotateLeftBrick() {
        if (brickRotator.getBrick().getShapeMatrix().size() <= 1) {
            return false;
        }

        int oldRotation = brickRotator.getRotationIndex();
        int numRotations = brickRotator.getBrick().getShapeMatrix().size();
        int newRotation = (oldRotation - 1 + numRotations) % numRotations;

        int[][] kickTable = SRSKickData.getKicks(brickRotator.getBrick(), oldRotation, newRotation, false);

        Point originalOffset = new Point(currentOffset);
        int[][] testShape = brickRotator.getBrick().getShapeMatrix().get(newRotation);

        for (int[] kick : kickTable) {
            int testX = (int) originalOffset.getX() + kick[0];
            int testY = (int) originalOffset.getY() + kick[1];

            if (!MatrixUtils.hasCollision(currentGameMatrix, testShape, testX, testY)) {
                currentOffset.setLocation(testX, testY);
                brickRotator.setRotationIndex(newRotation);
                return true;
            }
        }
        return false;
    }

    /**
     * Spawns a new piece at the top-center of the board.
     *
     * @return true if the new piece collides at spawn (game over), false if spawn successful
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(GameConstants.SPAWN_X, GameConstants.SPAWN_Y);
        hasHeld = false;
        return MatrixUtils.hasCollision(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Gets the current state of the game board with all locked pieces.
     *
     * @return the 2D array representing the board state
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Creates a ViewData object containing all information needed to render the game.
     *
     * @return an immutable ViewData object with complete rendering information
     * @see ViewData
     * @see #getShadowYPosition()
     */
    @Override
    public ViewData getViewData() {
        int[][] heldData;
        if (heldBrick != null) {
            // Always use the first rotation (index 0) for display
            heldData = heldBrick.getShapeMatrix().get(0);
        } else {
            heldData = new int[4][4];
        }
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                getShadowYPosition(),
                heldData
        );
    }


    /**
     * Merges the current piece into the background board, permanently locking it in place.
     *
     * This is called when a piece can no longer move down, finalizing its position on the board.
     * The piece becomes part of the permanent board state and can no longer be moved or rotated.
     *
     * @see #clearRows()
     * @see #createNewBrick()
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixUtils.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Checks for and clears any completed rows on the board.
     *
     * Where BASE_SCORE = 50 points per line
     * The quadratic formula (n²) encourages skillful play:
     * Score = BASE_SCORE × lines²
     *
     * @return a LineClearResult containing lines cleared, new board state, and score bonus
     * @see LineClearResult
     * @see MatrixUtils#clearCompletedLines(int[][])
     */
    @Override
    public LineClearResult clearRows() {
        LineClearResult clearResult = MatrixUtils.clearCompletedLines(currentGameMatrix);
        currentGameMatrix = clearResult.getNewMatrix();
        return clearResult;
    }

    /**
     * Gets the player's score tracker object.
     *
     * @return the Score object managing the player's current score
     * @see Score
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Resets the game to its initial state for a new game.
     * Called when the player starts a new game after game over or presses the "New Game" button.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        hasHeld = false;
        createNewBrick();
    }

    /**
     * Calculates where the current piece would land if dropped straight down.
     *
     * @return the Y-coordinate where the piece would land (bottom-most valid position)
     * @see ViewData#getShadowYPosition()
     */
    @Override
    public int getShadowYPosition() {
        int[][] currentMatrix = MatrixUtils.copy(currentGameMatrix);
        int shadowY = (int) currentOffset.getY();

        while (!MatrixUtils.hasCollision(currentMatrix, brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), shadowY + 1)) {
            shadowY++;
        }
        return shadowY;
    }

    /**
     * Clears all blocks from the board, leaving it completely empty.
     */
    @Override
    public void clearBoard() {
        currentGameMatrix = new int[width][height];
    }
}