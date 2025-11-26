package com.comp2042.view.renderer;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.theme.ColorTheme;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRendererTest {

    private GameRenderer renderer;
    private ColorTheme colorTheme;

    @BeforeEach
    void setUp() {
        colorTheme = ColorTheme.arcade();
        renderer = new GameRenderer(colorTheme);
    }

    @Test
    void testConstruction_NotNull() {
        assertNotNull(renderer);
    }

    @Test
    void testInitializeDisplayMatrix() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        assertDoesNotThrow(() -> renderer.initializeDisplayMatrix(boardMatrix, gamePanel));

        // Should add rectangles to panel
        assertFalse(gamePanel.getChildren().isEmpty());
    }

    @Test
    void testInitializeBrickRectangles() {
        int[][] brickData = new int[4][4];
        GridPane brickPanel = new GridPane();

        assertDoesNotThrow(() -> renderer.initializeBrickRectangles(brickData, brickPanel));

        // Should add rectangles to panel
        assertFalse(brickPanel.getChildren().isEmpty());
    }

    @Test
    void testInitializeShadowRectangles() {
        int[][] brickData = new int[4][4];
        GridPane gamePanel = new GridPane();

        assertDoesNotThrow(() -> renderer.initializeShadowRectangles(brickData, gamePanel));
    }

    @Test
    void testRefreshBrick() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = {
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        GridPane gamePanel = new GridPane();
        GridPane brickPanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeBrickRectangles(brickData, brickPanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        ViewData viewData = new ViewData(brickData, 3, 5, null, 10, null);

        assertDoesNotThrow(() -> renderer.refreshBrick(viewData, gamePanel));
    }

    @Test
    void testRefreshGameBackground() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);

        // Add some filled cells
        boardMatrix[10][5] = 1;
        boardMatrix[15][3] = 2;

        assertDoesNotThrow(() -> renderer.refreshGameBackground(boardMatrix));
    }

    @Test
    void testUpdateShadow() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = new int[4][4];
        brickData[1][1] = 1;

        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        ViewData viewData = new ViewData(brickData, 3, 5, null, 10, null);

        assertDoesNotThrow(() -> renderer.updateShadow(viewData, gamePanel));
    }

    @Test
    void testRefreshBrick_DifferentPositions() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = {{1, 1}, {1, 1}};

        GridPane gamePanel = new GridPane();
        GridPane brickPanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeBrickRectangles(brickData, brickPanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        ViewData viewData1 = new ViewData(brickData, 2, 5, null, 10, null);
        ViewData viewData2 = new ViewData(brickData, 4, 8, null, 12, null);

        assertDoesNotThrow(() -> {
            renderer.refreshBrick(viewData1, gamePanel);
            renderer.refreshBrick(viewData2, gamePanel);
        });
    }

    @Test
    void testInitializeDisplayMatrix_CorrectSize() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);

        // Should create rectangles for visible rows only
        int visibleRows = GameConstants.BOARD_HEIGHT - GameConstants.VISIBLE_ROW_START;
        int expectedSize = visibleRows * GameConstants.BOARD_WIDTH;
        assertEquals(expectedSize, gamePanel.getChildren().size());
    }

    @Test
    void testRefreshGameBackground_WithEmptyBoard() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);

        assertDoesNotThrow(() -> renderer.refreshGameBackground(boardMatrix));
    }

    @Test
    void testRefreshGameBackground_WithFullBoard() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);

        // Fill board
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                boardMatrix[i][j] = (i + j) % 7 + 1;
            }
        }

        assertDoesNotThrow(() -> renderer.refreshGameBackground(boardMatrix));
    }

    @Test
    void testWithDifferentColorTheme() {
        ColorTheme zenTheme = ColorTheme.zen();
        GameRenderer zenRenderer = new GameRenderer(zenTheme);

        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        GridPane gamePanel = new GridPane();

        assertDoesNotThrow(() -> zenRenderer.initializeDisplayMatrix(boardMatrix, gamePanel));
    }

    @Test
    void testUpdateShadow_SameAsPosition() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = {{1, 1}, {1, 1}};

        GridPane gamePanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        // Shadow at same position as brick (landed)
        ViewData viewData = new ViewData(brickData, 3, 10, null, 10, null);

        assertDoesNotThrow(() -> renderer.updateShadow(viewData, gamePanel));
    }

    @Test
    void testRefreshBrick_InHiddenRows() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = {{1, 1}, {1, 1}};

        GridPane gamePanel = new GridPane();
        GridPane brickPanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeBrickRectangles(brickData, brickPanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        // Brick in hidden spawn area
        ViewData viewData = new ViewData(brickData, 3, 0, null, 5, null);

        assertDoesNotThrow(() -> renderer.refreshBrick(viewData, gamePanel));
    }

    @Test
    void testMultipleRefreshCycles() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        int[][] brickData = {{1}};

        GridPane gamePanel = new GridPane();
        GridPane brickPanel = new GridPane();

        renderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        renderer.initializeBrickRectangles(brickData, brickPanel);
        renderer.initializeShadowRectangles(brickData, gamePanel);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                ViewData viewData = new ViewData(brickData, 3, 5 + i, null, 15, null);
                renderer.refreshBrick(viewData, gamePanel);
                renderer.updateShadow(viewData, gamePanel);
            }
        });
    }
}