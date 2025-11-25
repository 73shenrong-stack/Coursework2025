package comp2042.view.renderer;

import comp2042.constants.GameConstants;
import comp2042.view.theme.ColorTheme;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Responsible for rendering preview panels (Next piece and Hold piece)
 *
 */
public class PreviewPanelRenderer {

    private Rectangle[][] nextBrickRectangles;
    private Rectangle[][] holdBrickRectangles;
    private final ColorTheme colorTheme;

    public PreviewPanelRenderer(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    // Initialize the next brick preview panel

    public void initializeNextBrickPanel(GridPane nextBrickPanel) {
        nextBrickRectangles = new Rectangle[GameConstants.PREVIEW_PANEL_SIZE][GameConstants.PREVIEW_PANEL_SIZE];
        for (int i = 0; i < GameConstants.PREVIEW_PANEL_SIZE; i++) {
            for (int j = 0; j < GameConstants.PREVIEW_PANEL_SIZE; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcHeight(GameConstants.ARC_SIZE);
                rectangle.setArcWidth(GameConstants.ARC_SIZE);
                nextBrickRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    // Initialize the hold brick preview panel

    public void initializeHoldBrickPanel(GridPane holdBrickPanel) {
        holdBrickRectangles = new Rectangle[GameConstants.PREVIEW_PANEL_SIZE][GameConstants.PREVIEW_PANEL_SIZE];
        for (int i = 0; i < GameConstants.PREVIEW_PANEL_SIZE; i++) {
            for (int j = 0; j < GameConstants.PREVIEW_PANEL_SIZE; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcHeight(GameConstants.ARC_SIZE);
                rectangle.setArcWidth(GameConstants.ARC_SIZE);
                holdBrickRectangles[i][j] = rectangle;
                holdBrickPanel.add(rectangle, j, i);
            }
        }
    }

    // Update the next brick preview display

    public void updateNextBrickDisplay(int[][] nextBrickData) {
        // Clear all rectangles
        for (int i = 0; i < GameConstants.PREVIEW_PANEL_SIZE; i++) {
            for (int j = 0; j < GameConstants.PREVIEW_PANEL_SIZE; j++) {
                nextBrickRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        if (nextBrickData == null) return;

        renderCenteredBrick(nextBrickData, nextBrickRectangles);
    }

    // Update the hold brick preview display

    public void updateHoldBrickDisplay(int[][] holdBrickData) {
        // Clear all rectangles
        for (int i = 0; i < GameConstants.PREVIEW_PANEL_SIZE; i++) {
            for (int j = 0; j < GameConstants.PREVIEW_PANEL_SIZE; j++) {
                holdBrickRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        if (holdBrickData == null) return;

        renderCenteredBrick(holdBrickData, holdBrickRectangles);
    }

    // Render a brick centered in a 4x4 preview grid

    private void renderCenteredBrick(int[][] brickData, Rectangle[][] targetRectangles) {
        // Find the bounding box of the brick
        int minRow = GameConstants.PREVIEW_PANEL_SIZE, maxRow = -1;
        int minCol = GameConstants.PREVIEW_PANEL_SIZE, maxCol = -1;

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    minRow = Math.min(minRow, i);
                    maxRow = Math.max(maxRow, i);
                    minCol = Math.min(minCol, j);
                    maxCol = Math.max(maxCol, j);
                }
            }
        }

        if (maxRow == -1) return; // No brick to render

        // Calculate centering offset
        int brickHeight = maxRow - minRow + 1;
        int brickWidth = maxCol - minCol + 1;
        int offsetX = (GameConstants.PREVIEW_PANEL_SIZE - brickWidth) / 2;
        int offsetY = (GameConstants.PREVIEW_PANEL_SIZE - brickHeight) / 2;

        // Render the brick centered
        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                int targetY = offsetY + (i - minRow);
                int targetX = offsetX + (j - minCol);

                if (targetY >= 0 && targetY < GameConstants.PREVIEW_PANEL_SIZE &&
                        targetX >= 0 && targetX < GameConstants.PREVIEW_PANEL_SIZE &&
                        brickData[i][j] != 0) {

                    targetRectangles[targetY][targetX].setFill(colorTheme.getColor(brickData[i][j]));
                    targetRectangles[targetY][targetX].setArcWidth(GameConstants.ARC_SIZE);
                    targetRectangles[targetY][targetX].setArcHeight(GameConstants.ARC_SIZE);
                }
            }
        }
    }
}