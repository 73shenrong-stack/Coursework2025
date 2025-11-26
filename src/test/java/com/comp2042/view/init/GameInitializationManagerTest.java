package com.comp2042.view.init;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.game.GameMode;
import com.comp2042.view.theme.ColorTheme;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInitializationManagerTest {

    private GameInitializationManager initManager;
    private StackPane rootPane;
    private GridPane gamePanel;
    private GridPane brickPanel;

    @BeforeEach
    void setUp() {
        rootPane = new StackPane();
        gamePanel = new GridPane();
        brickPanel = new GridPane();
        initManager = new GameInitializationManager(rootPane, gamePanel, brickPanel);
    }

    @Test
    void testConstruction_NotNull() {
        assertNotNull(initManager);
    }

    @Test
    void testInitializeRenderers_NotNull() {
        int[][] boardMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        ViewData viewData = new ViewData(
                new int[4][4], 3, 5,
                new int[4][4], 10, null
        );
        GridPane nextPanel = new GridPane();
        GridPane holdPanel = new GridPane();
        ColorTheme theme = ColorTheme.arcade();

        GameInitializationManager.RendererBundle bundle =
                initManager.initializeRenderers(boardMatrix, viewData, nextPanel, holdPanel, theme);

        assertNotNull(bundle);
        assertNotNull(bundle.getGameRenderer());
        assertNotNull(bundle.getPreviewRenderer());
    }

    @Test
    void testApplyTheme_ArcadeTheme() {
        ColorTheme theme = ColorTheme.arcade();

        assertDoesNotThrow(() -> initManager.applyTheme(theme));
    }

    @Test
    void testApplyTheme_ZenTheme() {
        ColorTheme theme = ColorTheme.zen();

        assertDoesNotThrow(() -> initManager.applyTheme(theme));
    }

    @Test
    void testPositionBrickPanel() {
        ViewData viewData = new ViewData(
                new int[4][4], 3, 5,
                new int[4][4], 10, null
        );

        assertDoesNotThrow(() -> initManager.positionBrickPanel(viewData));
    }

    @Test
    void testPositionBrickPanel_DifferentPositions() {
        ViewData viewData1 = new ViewData(new int[4][4], 0, 0, null, 0, null);
        ViewData viewData2 = new ViewData(new int[4][4], 5, 10, null, 0, null);

        initManager.positionBrickPanel(viewData1);
        double x1 = brickPanel.getLayoutX();
        double y1 = brickPanel.getLayoutY();

        initManager.positionBrickPanel(viewData2);
        double x2 = brickPanel.getLayoutX();
        double y2 = brickPanel.getLayoutY();

        assertTrue(x2 != x1 || y2 != y1, "Different positions should result in different layouts");
    }

    @Test
    void testCreateGameLoop_BlitzMode() {
        boolean[] tickCalled = {false};
        Runnable onTick = () -> tickCalled[0] = true;

        Timeline timeline = initManager.createGameLoop(GameMode.BLITZ, onTick);

        assertNotNull(timeline);
        assertEquals(Timeline.INDEFINITE, timeline.getCycleCount());
    }

    @Test
    void testCreateGameLoop_ZenMode() {
        Runnable onTick = () -> {};

        Timeline timeline = initManager.createGameLoop(GameMode.ZEN, onTick);

        assertNotNull(timeline);
        assertEquals(Timeline.INDEFINITE, timeline.getCycleCount());
    }

    @Test
    void testCreateGameLoop_FortyLinesMode() {
        Runnable onTick = () -> {};

        Timeline timeline = initManager.createGameLoop(GameMode.FORTY_LINES, onTick);

        assertNotNull(timeline);
        assertEquals(Timeline.INDEFINITE, timeline.getCycleCount());
    }

    @Test
    void testRendererBundle_GetGameRenderer() {
        int[][] matrix = new int[5][5];
        ViewData viewData = new ViewData(new int[4][4], 0, 0, null, 0, null);
        GridPane nextPanel = new GridPane();
        GridPane holdPanel = new GridPane();

        GameInitializationManager.RendererBundle bundle =
                initManager.initializeRenderers(matrix, viewData, nextPanel, holdPanel, ColorTheme.arcade());

        assertNotNull(bundle.getGameRenderer());
    }

    @Test
    void testRendererBundle_GetPreviewRenderer() {
        int[][] matrix = new int[5][5];
        ViewData viewData = new ViewData(new int[4][4], 0, 0, null, 0, null);
        GridPane nextPanel = new GridPane();
        GridPane holdPanel = new GridPane();

        GameInitializationManager.RendererBundle bundle =
                initManager.initializeRenderers(matrix, viewData, nextPanel, holdPanel, ColorTheme.arcade());

        assertNotNull(bundle.getPreviewRenderer());
    }

    @Test
    void testApplyTheme_WithNullRootPane() {
        GameInitializationManager manager = new GameInitializationManager(null, gamePanel, brickPanel);

        assertDoesNotThrow(() -> manager.applyTheme(ColorTheme.arcade()));
    }

    @Test
    void testPositionBrickPanel_AtOrigin() {
        ViewData viewData = new ViewData(new int[4][4], 0, 0, null, 0, null);

        initManager.positionBrickPanel(viewData);

        // Should not throw and should set some position
        assertNotNull(brickPanel);
    }

    @Test
    void testCreateGameLoop_WithNullCallback() {
        // If the method handles null gracefully, test that it doesn't throw
        assertDoesNotThrow(() -> {
            Timeline timeline = initManager.createGameLoop(GameMode.BLITZ, null);
            // The timeline might be created but won't do anything useful
            assertNotNull(timeline);
        });
    }

    @Test
    void testInitializeRenderers_WithDifferentThemes() {
        int[][] matrix = new int[5][5];
        ViewData viewData = new ViewData(new int[4][4], 0, 0, null, 0, null);
        GridPane nextPanel1 = new GridPane();
        GridPane holdPanel1 = new GridPane();
        GridPane nextPanel2 = new GridPane();
        GridPane holdPanel2 = new GridPane();

        GameInitializationManager.RendererBundle bundle1 =
                initManager.initializeRenderers(matrix, viewData, nextPanel1, holdPanel1, ColorTheme.arcade());

        GameInitializationManager.RendererBundle bundle2 =
                initManager.initializeRenderers(matrix, viewData, nextPanel2, holdPanel2, ColorTheme.zen());

        assertNotNull(bundle1);
        assertNotNull(bundle2);
        assertNotSame(bundle1, bundle2);
    }
}