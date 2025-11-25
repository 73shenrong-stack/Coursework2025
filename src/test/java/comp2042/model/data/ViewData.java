package comp2042.model.data;

import comp2042.util.MatrixUtils;

/** Immutable data class containing view information for rendering the game
 *
 */

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int shadowYPosition;
    private final int[][] heldBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    int[][] nextBrickData, int shadowYPosition, int[][] heldBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.shadowYPosition = shadowYPosition;
        this.heldBrickData = heldBrickData;
    }

    // Get the held brick data (defensive copy)

    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixUtils.copy(heldBrickData) : null;
    }

    // Get the current brick data (defensive copy)

    public int[][] getBrickData() {
        return MatrixUtils.copy(brickData);
    }

    // Get the X position of the brick

    public int getXPosition() {
        return xPosition;
    }

    // Get the Y position of the brick

    public int getYPosition() {
        return yPosition;
    }

    // Get the next brick data (defensive copy)

    public int[][] getNextBrickData() {
        return nextBrickData != null ? MatrixUtils.copy(nextBrickData) : null;
    }

    // Get the shadow (ghost piece) Y position

    public int getShadowYPosition() {
        return shadowYPosition;
    }
}