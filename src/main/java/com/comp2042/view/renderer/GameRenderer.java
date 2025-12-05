package com.comp2042.view.renderer;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.theme.ColorTheme;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Responsible for rendering the Tetris game board and pieces
 * All rendering is performed by manipulating pre-created Rectangle instances
 * Added to GridPanes, which provides excellent performance even during fast drops.
 * Visual styling (colors, rounded corners) is controlled via a ColorTheme.
 */

public class GameRenderer {

    private Rectangle[][] displayMatrix;
    private Rectangle[][] brickRectangles;
    private Rectangle[][] shadowRectangles;
    private final ColorTheme colorTheme;

    /**
     * Creates a new renderer with the specified color theme.
     *
     * @param colorTheme the theme defining colors for each tetromino type
     */
    public GameRenderer(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    /**
     * Initializes the background display matrix that shows locked pieces.
     * @param boardMatrix the current board state (2D array)
     * @param gamePanel   the GridPane representing the visible playfield
     */
    public void initializeDisplayMatrix(int[][] boardMatrix, GridPane gamePanel) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = GameConstants.VISIBLE_ROW_START; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - GameConstants.VISIBLE_ROW_START);
            }
        }
    }

    /**
     * Initializes the rectangle grid used to render the currently falling piece.
     * @param brickData  the 2D array representing the current tetromino shape
     * @param brickPanel the GridPane that holds the falling piece
     */
    public void initializeBrickRectangles(int[][] brickData, GridPane brickPanel) {
        brickRectangles = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                brickRectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Initializes the ghost/shadow piece rectangles.
     *
     * @param brickData the current tetromino shape
     * @param gamePanel the main game GridPane
     */

    public void initializeShadowRectangles(int[][] brickData, GridPane gamePanel) {
        shadowRectangles = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setOpacity(GameConstants.SHADOW_OPACITY);
                shadowRectangles[i][j] = rectangle;
                gamePanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Refreshes the visual representation of the currently falling piece and updates the ghost piece.
     *
     * @param viewData  the current game state containing brick data and position
     * @param gamePanel the main game GridPane used for drawing active pieces
     */
    public void refreshBrick(ViewData viewData, GridPane gamePanel) {
        // Clear old falling piece
        for (Rectangle[] row : brickRectangles) {
            for (Rectangle r : row) {
                r.setFill(Color.TRANSPARENT);
                if (r.getParent() == gamePanel) {
                    gamePanel.getChildren().remove(r);
                }
            }
        }

        // Draw new brick position
        int[][] brickData = viewData.getBrickData();
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    int gridX = viewData.getXPosition() + j;
                    int gridY = viewData.getYPosition() + i;

                    // Only draw inside visible area
                    if (gridY >= GameConstants.VISIBLE_ROW_START) {
                        brickRectangles[i][j].setFill(colorTheme.getColor(brickData[i][j]));
                        brickRectangles[i][j].setArcWidth(GameConstants.ARC_SIZE);
                        brickRectangles[i][j].setArcHeight(GameConstants.ARC_SIZE);
                        gamePanel.add(brickRectangles[i][j], gridX, gridY - GameConstants.VISIBLE_ROW_START);
                    }
                }
            }
        }

        updateShadow(viewData, gamePanel);
    }

    /**
     * Updates the ghost/shadow piece to show where the current piece will land.
     *
     * @param viewData  current game state with shadow Y position
     * @param gamePanel the main game GridPane
     */

    public void updateShadow(ViewData viewData, GridPane gamePanel) {
        // Clear old shadow
        for (int i = 0; i < shadowRectangles.length; i++) {
            for (int j = 0; j < shadowRectangles[i].length; j++) {
                shadowRectangles[i][j].setFill(Color.TRANSPARENT);
                gamePanel.getChildren().remove(shadowRectangles[i][j]);
            }
        }

        // Draw new shadow if not at landing position
        if (viewData.getShadowYPosition() != viewData.getYPosition()) {
            int[][] brickData = viewData.getBrickData();
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    if (brickData[i][j] != 0) {
                        int gridX = viewData.getXPosition() + j;
                        int gridY = viewData.getShadowYPosition() + i;

                        if (gridY >= GameConstants.VISIBLE_ROW_START && gridY < GameConstants.BOARD_HEIGHT) {
                            shadowRectangles[i][j].setFill(colorTheme.getColor(brickData[i][j]));
                            shadowRectangles[i][j].setArcHeight(GameConstants.ARC_SIZE);
                            shadowRectangles[i][j].setArcWidth(GameConstants.ARC_SIZE);
                            gamePanel.add(shadowRectangles[i][j], gridX, gridY - GameConstants.VISIBLE_ROW_START);
                        }
                    }
                }
            }
        }
    }

    /**
     * Refreshes the background board display (all locked pieces).
     *
     * @param board the current board matrix containing color values of locked cells
     */

    public void refreshGameBackground(int[][] board) {
        for (int i = GameConstants.VISIBLE_ROW_START; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Applies color and rounded corner styling to a rectangle based on the cell value.
     *
     * @param colorValue numeric identifier of the tetromino type (0 = empty)
     * @param rectangle  the Rectangle to style
     */

    private void setRectangleData(int colorValue, Rectangle rectangle) {
        rectangle.setFill(colorTheme.getColor(colorValue));
        rectangle.setArcHeight(GameConstants.ARC_SIZE);
        rectangle.setArcWidth(GameConstants.ARC_SIZE);
    }
}