package comp2042.view.renderer;

import comp2042.constants.GameConstants;
import comp2042.model.data.ViewData;
import comp2042.view.theme.ColorTheme;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Responsible for rendering the Tetris game board and pieces
 *
 */

public class GameRenderer {

    private Rectangle[][] displayMatrix;
    private Rectangle[][] brickRectangles;
    private Rectangle[][] shadowRectangles;
    private final ColorTheme colorTheme;

    public GameRenderer(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    // Initialize the background matrix display

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

    // Initialize the falling brick rectangles

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

    // Initialize shadow/ghost piece rectangles

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

    // Refresh the falling brick display

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

    // Update the shadow/ghost piece position

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

    // Refresh the game background (locked pieces)

    public void refreshGameBackground(int[][] board) {
        for (int i = GameConstants.VISIBLE_ROW_START; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    // Set the color and style for a rectangle

    private void setRectangleData(int colorValue, Rectangle rectangle) {
        rectangle.setFill(colorTheme.getColor(colorValue));
        rectangle.setArcHeight(GameConstants.ARC_SIZE);
        rectangle.setArcWidth(GameConstants.ARC_SIZE);
    }
}