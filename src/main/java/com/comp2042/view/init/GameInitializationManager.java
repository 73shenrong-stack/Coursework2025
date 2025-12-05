package com.comp2042.view.init;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.game.GameMode;
import com.comp2042.view.renderer.GameRenderer;
import com.comp2042.view.renderer.PreviewPanelRenderer;
import com.comp2042.view.theme.ColorTheme;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;

/**
 * Manages initialization and configuration of core game view components.
 */

public class GameInitializationManager {

    private final StackPane rootPane;
    private final GridPane gamePanel;
    private final GridPane brickPanel;

    /**
     * Constructs a new {@code GameInitializationManager} with the required UI containers.
     *
     * @param rootPane   the root StackPane containing the game UI
     * @param gamePanel  the GridPane used for the main game board
     * @param brickPanel the GridPane used for rendering the active brick
     */
    public GameInitializationManager(StackPane rootPane, GridPane gamePanel, GridPane brickPanel) {
        this.rootPane = rootPane;
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
    }

    /**
     * Initializes and configures the game and preview renderers.
     *
     * @param boardMatrix    the current game board matrix
     * @param brick          the ViewData containing current, next, and held brick data
     * @param nextBrickPanel the GridPane for the next piece preview
     * @param holdBrickPanel the GridPane for the hold piece preview
     * @param colorTheme     the ColorTheme used to style the renderers
     * @return a RendererBundle containing the initialized game and preview renderers
     */
    public RendererBundle initializeRenderers(int[][] boardMatrix, ViewData brick,
                                              GridPane nextBrickPanel, GridPane holdBrickPanel,
                                              ColorTheme colorTheme) {
        // Initialize game renderer
        GameRenderer gameRenderer = new GameRenderer(colorTheme);
        gameRenderer.initializeDisplayMatrix(boardMatrix, gamePanel);
        gameRenderer.initializeBrickRectangles(brick.getBrickData(), brickPanel);
        gameRenderer.initializeShadowRectangles(brick.getBrickData(), gamePanel);

        // Initialize preview renderer
        PreviewPanelRenderer previewRenderer = new PreviewPanelRenderer(colorTheme);
        previewRenderer.initializeNextBrickPanel(nextBrickPanel);
        previewRenderer.initializeHoldBrickPanel(holdBrickPanel);

        // Update initial displays
        previewRenderer.updateNextBrickDisplay(brick.getNextBrickData());
        previewRenderer.updateHoldBrickDisplay(brick.getHeldBrickData());

        return new RendererBundle(gameRenderer, previewRenderer);
    }


    /**
     * Applies the specified ColorTheme to the root pane.
     * @param colorTheme the theme whose CSS file should be applied
     */
    public void applyTheme(ColorTheme colorTheme) {
        if (rootPane != null) {
            rootPane.getStylesheets().clear();
            String cssFile = colorTheme.getCssFile();
            URL cssUrl = getClass().getResource("/" + cssFile);
            if (cssUrl == null) {
                cssUrl = getClass().getResource(cssFile);
            }
            if (cssUrl != null) {
                rootPane.getStylesheets().add(cssUrl.toExternalForm());
            }
        }
    }

    /**
     * Positions the active brick panel within the game panel based on the current brick position.
     *
     * @param brick the ViewData describing the current brick's position
     */
    public void positionBrickPanel(ViewData brick) {
        double xPos = gamePanel.getLayoutX() +
                brick.getXPosition() * (brickPanel.getVgap() + GameConstants.BRICK_SIZE);
        double yPos = -42 + gamePanel.getLayoutY() +
                brick.getYPosition() * (brickPanel.getHgap() + GameConstants.BRICK_SIZE);

        brickPanel.setLayoutX(xPos);
        brickPanel.setLayoutY(yPos);
    }

    /**
     * Creates the main game loop {@link Timeline} for the specified GameMode.
     *
     * @param mode   the current game mode, which determines the drop speed
     * @param onTick the callback to execute on each game loop tick
     * @return a configured Timeline set to run indefinitely
     */
    public Timeline createGameLoop(GameMode mode, Runnable onTick) {
        Duration speed = getSpeedForMode(mode);
        Timeline timeline = new Timeline(new KeyFrame(speed, event -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    /**
     * Returns the drop speed Duration corresponding to the given GameMode.
     *
     * @param mode the game mode whose speed should be returned
     * @return a Duration representing the drop speed for the given mode
     */
    private Duration getSpeedForMode(GameMode mode) {
        switch (mode) {
            case BLITZ:
                return Duration.millis(GameConstants.BLITZ_MODE_SPEED);
            case ZEN:
                return Duration.millis(GameConstants.ZEN_MODE_SPEED);
            case FORTY_LINES:
            default:
                return Duration.millis(GameConstants.NORMAL_MODE_SPEED);
        }
    }

    // Bundle class to hold initialized renderers

    public static class RendererBundle {
        private final GameRenderer gameRenderer;
        private final PreviewPanelRenderer previewRenderer;

        /**
         * Constructs a new {@code RendererBundle}.
         *
         * @param gameRenderer    the initialized GameRenderer
         * @param previewRenderer the initialized PreviewPanelRenderer
         */
        public RendererBundle(GameRenderer gameRenderer, PreviewPanelRenderer previewRenderer) {
            this.gameRenderer = gameRenderer;
            this.previewRenderer = previewRenderer;
        }

        /**
         * Returns the GameRenderer contained in this bundle.
         *
         * @return the game renderer
         */
        public GameRenderer getGameRenderer() {
            return gameRenderer;
        }

        /**
         * Returns the PreviewPanelRenderer contained in this bundle.
         *
         * @return the preview panel renderer
         */
        public PreviewPanelRenderer getPreviewRenderer() {
            return previewRenderer;
        }
    }
}