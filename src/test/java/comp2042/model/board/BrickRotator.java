package comp2042.model.board;

import comp2042.model.brick.Brick;

public class BrickRotator {

    public Brick brick;
    private int currentShape = 0;

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    public Brick getBrick() {
        return brick;
    }

    public int getRotationIndex() {
        return currentShape;
    }

    public void setRotationIndex(int index) {
        this.currentShape = index;
    }

}
