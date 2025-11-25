package com.comp2042.view.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/** Manages UI element visibility and overlay states
 *
 */

public class UIStateManager {

    private final Pane gameOverOverlay;
    private final Pane pauseOverlay;
    private final VBox nextBrickContainer;
    private final VBox holdBrickContainer;
    private final Label modeTimerLabel;
    private final Label linesLabel;

    public UIStateManager(Pane gameOverOverlay, Pane pauseOverlay,
                          VBox nextBrickContainer, VBox holdBrickContainer,
                          Label modeTimerLabel, Label linesLabel) {
        this.gameOverOverlay = gameOverOverlay;
        this.pauseOverlay = pauseOverlay;
        this.nextBrickContainer = nextBrickContainer;
        this.holdBrickContainer = holdBrickContainer;
        this.modeTimerLabel = modeTimerLabel;
        this.linesLabel = linesLabel;

        initializeOverlays();
    }

    // Initialize overlays to hidden state

    private void initializeOverlays() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
    }

    // Show the pause overlay

    public void showPauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(true);
        }
    }

    // Hide the pause overlay

    public void hidePauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }
    }

    // Show the game over overlay

    public void showGameOverOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }
    }

    // Hide the game over overlay

    public void hideGameOverOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
    }

    // Show 40 lines completion overlay

    public void showCompletionOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.getChildren().clear();

            VBox box = new VBox(20);
            box.setStyle("-fx-alignment: center;");

            Label titleLabel = new Label("40 LINES CLEARED!");
            titleLabel.getStyleClass().add("overlay-title");

            Label subLabel1 = new Label("Press N for New Game");
            subLabel1.getStyleClass().add("overlay-subtext");

            Label subLabel2 = new Label("Press M for Main Menu");
            subLabel2.getStyleClass().add("overlay-subtext");

            box.getChildren().addAll(titleLabel, subLabel1, subLabel2);
            gameOverOverlay.getChildren().add(box);
        }
    }

    // Set visibility of game UI elements (next piece, hold piece, timer, lines)

    public void setGameUIVisibility(boolean visible) {
        if (nextBrickContainer != null) {
            nextBrickContainer.setVisible(visible);
        }
        if (holdBrickContainer != null) {
            holdBrickContainer.setVisible(visible);
        }
        if (modeTimerLabel != null) {
            modeTimerLabel.setVisible(visible);
        }
        if (linesLabel != null) {
            linesLabel.setVisible(visible);
        }
    }

    // Show all game UI elements

    public void showGameUI() {
        setGameUIVisibility(true);
    }

    // Hide all game UI elements

    public void hideGameUI() {
        setGameUIVisibility(false);
    }
}