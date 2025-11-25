package comp2042.model.board;

import comp2042.constants.GameConstants;
import comp2042.model.brick.Brick;
import comp2042.model.brick.BrickGenerator;
import comp2042.model.brick.RandomBrickGenerator;
import comp2042.model.data.LineClearResult;
import comp2042.model.data.ViewData;
import comp2042.model.game.Score;
import comp2042.util.MatrixUtils;

import java.awt.*;

/**
 * Main Tetris board implementation that manages game state and piece movement
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

    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

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

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(GameConstants.SPAWN_X, GameConstants.SPAWN_Y);
        hasHeld = false;
        return MatrixUtils.hasCollision(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

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

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixUtils.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public LineClearResult clearRows() {
        LineClearResult clearResult = MatrixUtils.clearCompletedLines(currentGameMatrix);
        currentGameMatrix = clearResult.getNewMatrix();
        return clearResult;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        hasHeld = false;
        createNewBrick();
    }

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

    @Override
    public void clearBoard() {
        currentGameMatrix = new int[width][height];
    }
}