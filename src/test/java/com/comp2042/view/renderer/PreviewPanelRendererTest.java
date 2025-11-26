package com.comp2042.view.renderer;

import com.comp2042.constants.GameConstants;
import com.comp2042.view.theme.ColorTheme;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreviewPanelRendererTest {

    private PreviewPanelRenderer renderer;
    private ColorTheme colorTheme;

    @BeforeEach
    void setUp() {
        colorTheme = ColorTheme.arcade();
        renderer = new PreviewPanelRenderer(colorTheme);
    }

    @Test
    void testConstruction_NotNull() {
        assertNotNull(renderer);
    }

    @Test
    void testInitializeNextBrickPanel() {
        GridPane nextBrickPanel = new GridPane();

        assertDoesNotThrow(() -> renderer.initializeNextBrickPanel(nextBrickPanel));

        // Panel should have children added
        assertFalse(nextBrickPanel.getChildren().isEmpty());
    }

    @Test
    void testInitializeHoldBrickPanel() {
        GridPane holdBrickPanel = new GridPane();

        assertDoesNotThrow(() -> renderer.initializeHoldBrickPanel(holdBrickPanel));

        // Panel should have children added
        assertFalse(holdBrickPanel.getChildren().isEmpty());
    }

    @Test
    void testUpdateNextBrickDisplay_WithValidData() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        int[][] brickData = {
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertDoesNotThrow(() -> renderer.updateNextBrickDisplay(brickData));
    }

    @Test
    void testUpdateNextBrickDisplay_WithNullData() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        assertDoesNotThrow(() -> renderer.updateNextBrickDisplay(null));
    }

    @Test
    void testUpdateHoldBrickDisplay_WithValidData() {
        GridPane holdBrickPanel = new GridPane();
        renderer.initializeHoldBrickPanel(holdBrickPanel);

        int[][] brickData = {
                {0, 2, 2, 2},
                {0, 0, 0, 2},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertDoesNotThrow(() -> renderer.updateHoldBrickDisplay(brickData));
    }

    @Test
    void testUpdateHoldBrickDisplay_WithNullData() {
        GridPane holdBrickPanel = new GridPane();
        renderer.initializeHoldBrickPanel(holdBrickPanel);

        assertDoesNotThrow(() -> renderer.updateHoldBrickDisplay(null));
    }

    @Test
    void testInitializeNextBrickPanel_CreatesCorrectSize() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        // Should create 4x4 grid
        int expectedSize = GameConstants.PREVIEW_PANEL_SIZE * GameConstants.PREVIEW_PANEL_SIZE;
        assertEquals(expectedSize, nextBrickPanel.getChildren().size());
    }

    @Test
    void testInitializeHoldBrickPanel_CreatesCorrectSize() {
        GridPane holdBrickPanel = new GridPane();
        renderer.initializeHoldBrickPanel(holdBrickPanel);

        // Should create 4x4 grid
        int expectedSize = GameConstants.PREVIEW_PANEL_SIZE * GameConstants.PREVIEW_PANEL_SIZE;
        assertEquals(expectedSize, holdBrickPanel.getChildren().size());
    }

    @Test
    void testUpdateNextBrickDisplay_MultipleUpdates() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        int[][] data1 = {{1, 1}, {1, 1}};
        int[][] data2 = {{2, 2, 2}, {0, 2, 0}};

        assertDoesNotThrow(() -> {
            renderer.updateNextBrickDisplay(data1);
            renderer.updateNextBrickDisplay(data2);
        });
    }

    @Test
    void testUpdateHoldBrickDisplay_MultipleUpdates() {
        GridPane holdBrickPanel = new GridPane();
        renderer.initializeHoldBrickPanel(holdBrickPanel);

        int[][] data1 = {{3, 3, 3, 3}};
        int[][] data2 = {{4, 4}, {4, 4}};

        assertDoesNotThrow(() -> {
            renderer.updateHoldBrickDisplay(data1);
            renderer.updateHoldBrickDisplay(data2);
        });
    }

    @Test
    void testWithDifferentColorTheme() {
        ColorTheme zenTheme = ColorTheme.zen();
        PreviewPanelRenderer zenRenderer = new PreviewPanelRenderer(zenTheme);

        GridPane panel = new GridPane();
        zenRenderer.initializeNextBrickPanel(panel);

        assertFalse(panel.getChildren().isEmpty());
    }

    @Test
    void testUpdateDisplay_WithEmptyBrick() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        int[][] emptyData = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertDoesNotThrow(() -> renderer.updateNextBrickDisplay(emptyData));
    }

    @Test
    void testUpdateDisplay_WithIBrick() {
        GridPane nextBrickPanel = new GridPane();
        renderer.initializeNextBrickPanel(nextBrickPanel);

        int[][] iBrickData = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertDoesNotThrow(() -> renderer.updateNextBrickDisplay(iBrickData));
    }

    @Test
    void testUpdateDisplay_WithOBrick() {
        GridPane holdBrickPanel = new GridPane();
        renderer.initializeHoldBrickPanel(holdBrickPanel);

        int[][] oBrickData = {
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        };

        assertDoesNotThrow(() -> renderer.updateHoldBrickDisplay(oBrickData));
    }
}