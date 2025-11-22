package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick = null;
    private boolean hasHeld = false;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    public boolean holdBrick() {
        if (hasHeld) {
            return false;
        }

        Brick currentBrick = brickRotator.brick;

        if (heldBrick == null) {
            heldBrick = currentBrick;
            hasHeld = true;
            createNewBrick();
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickRotator.setBrick(temp);
            brickRotator.setCurrentShape(0);
            hasHeld = true;
        }
        currentOffset = new Point(3, 0);
        return true;
    }

    public Brick getHeldBrick() {
        return heldBrick;
    }

    public void resetHasHeld() {
        hasHeld = false;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
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

            if (!MatrixOperations.intersect(currentGameMatrix, testShape, testX, testY)) {
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
        currentOffset = new Point(3, 0);
        hasHeld = false;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
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
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

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
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int shadowY = (int) currentOffset.getY();

        while (!MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), shadowY + 1)) {
            shadowY++;
        }

        return shadowY;
    }
}
