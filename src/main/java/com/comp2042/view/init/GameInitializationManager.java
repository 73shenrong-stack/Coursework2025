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
 * Handles initialization of game components
 */

public class GameInitializationManager {

    private final StackPane rootPane;
    private final GridPane gamePanel;
    private final GridPane brickPanel;

    public GameInitializationManager(StackPane rootPane, GridPane gamePanel, GridPane brickPanel) {
        this.rootPane = rootPane;
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
    }

    // Initialize renderers for the game

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

    //Apply CSS theme to the root pane

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

    // Position the brick panel based on current brick position

    public void positionBrickPanel(ViewData brick) {
        double xPos = gamePanel.getLayoutX() +
                brick.getXPosition() * (brickPanel.getVgap() + GameConstants.BRICK_SIZE);
        double yPos = -42 + gamePanel.getLayoutY() +
                brick.getYPosition() * (brickPanel.getHgap() + GameConstants.BRICK_SIZE);

        brickPanel.setLayoutX(xPos);
        brickPanel.setLayoutY(yPos);
    }

    // Create the game loop timeline
    public Timeline createGameLoop(GameMode mode, Runnable onTick) {
        Duration speed = getSpeedForMode(mode);
        Timeline timeline = new Timeline(new KeyFrame(speed, event -> onTick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    // Get the drop speed for the specified game mode

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

        public RendererBundle(GameRenderer gameRenderer, PreviewPanelRenderer previewRenderer) {
            this.gameRenderer = gameRenderer;
            this.previewRenderer = previewRenderer;
        }

        public GameRenderer getGameRenderer() {
            return gameRenderer;
        }

        public PreviewPanelRenderer getPreviewRenderer() {
            return previewRenderer;
        }
    }
}