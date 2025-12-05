package com.comp2042.view.ui;

import com.comp2042.model.game.GameMode;
import com.comp2042.model.game.GameRecords;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/** Manages UI element visibility and overlay states
 *  All overlay content is dynamically generated with proper styling, high score comparison, new record indication and player instructions.
 */

public class UIStateManager {

    private final Pane gameOverOverlay;
    private final Pane pauseOverlay;
    private final VBox nextBrickContainer;
    private final VBox holdBrickContainer;
    private final Label modeTimerLabel;
    private final Label linesLabel;

    /**
     * Constructs a new UI state manager with references to all managed UI components.
     *
     * @param gameOverOverlay   overlay pane shown on game over or mode completion
     * @param pauseOverlay      overlay pane shown when game is paused
     * @param nextBrickContainer container for the "Next" piece preview
     * @param holdBrickContainer container for the "Hold" piece preview
     * @param modeTimerLabel    label displaying mode-specific timer
     * @param linesLabel        label displaying lines cleared or progress
     */
    public UIStateManager(Pane gameOverOverlay, Pane pauseOverlay, VBox nextBrickContainer, VBox holdBrickContainer, Label modeTimerLabel, Label linesLabel) {
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

    /**
     * Displays the standard Game Over overlay (used when player loses).
     *
     * @param mode        the game mode that just ended
     * @param finalScore  player's final score
     * @param finalTime   final time in seconds
     * @param records     current high score records
     * @param isNewRecord true if a new high score was achieved
     */
    public void showGameOverOverlay(GameMode mode, int finalScore, int finalTime, GameRecords records, boolean isNewRecord) {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.getChildren().clear();

            VBox box = new VBox(20);
            box.setStyle("-fx-alignment: center;");

            Label titleLabel = new Label("GAME OVER");
            titleLabel.getStyleClass().add("overlay-title");

            Label scoreLabel = new Label("Score: " + finalScore);
            scoreLabel.getStyleClass().add("overlay-score");

            Label timeLabel = new Label("Time: " + GameRecords.formatTime(finalTime));
            timeLabel.getStyleClass().add("overlay-score");

            box.getChildren().addAll(titleLabel, scoreLabel, timeLabel);

            // Show high score for this mode
            int highScore = records.getHighScore(mode);
            int bestTime = records.getBestTime(mode);

            if (isNewRecord) {
                Label newRecordLabel = new Label("★ NEW RECORD! ★");
                newRecordLabel.getStyleClass().add("overlay-new-record");
                box.getChildren().add(newRecordLabel);
            }

            Label highScoreLabel = new Label("High Score: " + highScore);
            highScoreLabel.getStyleClass().add("overlay-record");

            if (mode == GameMode.FORTY_LINES) {
                Label bestTimeLabel = new Label("Best Time: " + GameRecords.formatTime(bestTime));
                bestTimeLabel.getStyleClass().add("overlay-record");
                box.getChildren().addAll(bestTimeLabel, highScoreLabel);
            } else {
                Label bestTimeLabel = new Label("Best Session: " + GameRecords.formatTime(bestTime));
                bestTimeLabel.getStyleClass().add("overlay-record");
                box.getChildren().addAll(highScoreLabel, bestTimeLabel);
            }

            Label subLabel1 = new Label("Press N for New Game");
            subLabel1.getStyleClass().add("overlay-subtext");

            Label subLabel2 = new Label("Press M for Main Menu");
            subLabel2.getStyleClass().add("overlay-subtext");

            box.getChildren().addAll(subLabel1, subLabel2);
            gameOverOverlay.getChildren().add(box);
        }
    }

    // Hide the game over overlay

    public void hideGameOverOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
    }

    /**
     * Displays the special victory overlay when 40 Lines mode is successfully completed.
     *
     * @param finalScore  final score achieved
     * @param finalTime   time taken to clear 40 lines (in seconds)
     * @param records     current high score records
     * @param isNewRecord true if this is the new fastest time or highest score
     */
    public void showCompletionOverlay(int finalScore, int finalTime, GameRecords records, boolean isNewRecord) {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.getChildren().clear();

            VBox box = new VBox(20);
            box.setStyle("-fx-alignment: center;");

            Label titleLabel = new Label("40 LINES CLEARED!");
            titleLabel.getStyleClass().add("overlay-title");

            Label timeLabel = new Label("Time: " + GameRecords.formatTime(finalTime));
            timeLabel.getStyleClass().add("overlay-score");

            Label scoreLabel = new Label("Score: " + finalScore);
            scoreLabel.getStyleClass().add("overlay-score");

            box.getChildren().addAll(titleLabel, timeLabel, scoreLabel);

            if (isNewRecord) {
                Label newRecordLabel = new Label("★ NEW RECORD! ★");
                newRecordLabel.getStyleClass().add("overlay-new-record");
                box.getChildren().add(newRecordLabel);
            }

            int bestTime = records.getBestTime(GameMode.FORTY_LINES);
            int highScore = records.getHighScore(GameMode.FORTY_LINES);

            Label bestTimeLabel = new Label("Best Time: " + GameRecords.formatTime(bestTime));
            bestTimeLabel.getStyleClass().add("overlay-record");

            Label highScoreLabel = new Label("High Score: " + highScore);
            highScoreLabel.getStyleClass().add("overlay-record");

            Label subLabel1 = new Label("Press N for New Game");
            subLabel1.getStyleClass().add("overlay-subtext");

            Label subLabel2 = new Label("Press M for Main Menu");
            subLabel2.getStyleClass().add("overlay-subtext");

            box.getChildren().addAll(bestTimeLabel, highScoreLabel, subLabel1, subLabel2);
            gameOverOverlay.getChildren().add(box);
        }
    }

    /**
     * Sets visibility of all core game UI elements.
     *
     * @param visible true to show, {@code false} to hide
     */
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