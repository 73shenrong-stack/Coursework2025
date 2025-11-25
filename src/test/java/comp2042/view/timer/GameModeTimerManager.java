package comp2042.view.timer;

import comp2042.model.game.GameMode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/** Manages game mode-specific timers (Blitz countdown, 40 Lines/Zen count-up)
 *
 */

public class GameModeTimerManager {

    private static final int BLITZ_DURATION_SECONDS = 120;
    private static final int TARGET_LINES_40 = 40;

    private Timeline gameTimer;
    private int timeRemaining;
    private int timeElapsed;
    private int linesCleared;
    private GameMode currentMode;

    private Label timerLabel;
    private Label linesLabel;

    private Runnable onBlitzComplete;
    private Runnable onFortyLinesComplete;

    public GameModeTimerManager(Label timerLabel, Label linesLabel) {
        this.timerLabel = timerLabel;
        this.linesLabel = linesLabel;
    }

    // Start timer for the specified game mode

    public void startTimer(GameMode mode) {
        this.currentMode = mode;
        stopTimer(); // Stop any existing timer

        switch (mode) {
            case BLITZ:
                startBlitzMode();
                break;
            case FORTY_LINES:
                start40LinesMode();
                break;
            case ZEN:
                startZenMode();
                break;
        }
    }

    // Start Blitz mode (2-minute countdown)

    private void startBlitzMode() {
        timeRemaining = BLITZ_DURATION_SECONDS;
        linesCleared = 0;
        updateDisplays();

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeRemaining--;
                    updateDisplays();

                    if (timeRemaining <= 0 && onBlitzComplete != null) {
                        onBlitzComplete.run();
                    }
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    // Start 40 Lines mode (count-up timer)

    private void start40LinesMode() {
        linesCleared = 0;
        timeElapsed = 0;
        updateDisplays();

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeElapsed++;
                    updateDisplays();
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    // Start Zen mode (count-up timer)

    private void startZenMode() {
        timeElapsed = 0;
        linesCleared = 0;
        updateDisplays();

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeElapsed++;
                    updateDisplays();
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    // Add cleared lines and check for completion

    public void addLinesCleared(int lines) {
        linesCleared += lines;
        updateDisplays();

        // Check 40 Lines completion
        if (currentMode == GameMode.FORTY_LINES &&
                linesCleared >= TARGET_LINES_40 &&
                onFortyLinesComplete != null) {
            onFortyLinesComplete.run();
        }
    }

    // Update timer and lines display

    private void updateDisplays() {
        updateTimerDisplay();
        updateLinesDisplay();
    }

    // Update the timer label

    private void updateTimerDisplay() {
        if (timerLabel == null) return;

        if (currentMode == GameMode.BLITZ) {
            // Countdown timer for Blitz
            int minutes = timeRemaining / 60;
            int seconds = timeRemaining % 60;
            timerLabel.setText(String.format("%d : %02d", minutes, seconds));
        } else {
            // Count-up timer for 40 Lines and Zen
            int minutes = timeElapsed / 60;
            int seconds = timeElapsed % 60;
            timerLabel.setText(String.format("%d : %02d", minutes, seconds));
        }
    }

    // Update the lines cleared label

    private void updateLinesDisplay() {
        if (linesLabel == null) return;

        if (currentMode == GameMode.FORTY_LINES) {
            // Show progress towards 40 lines
            linesLabel.setText(String.format("%d / %d", linesCleared, TARGET_LINES_40));
        } else {
            // Show total lines cleared for Blitz and Zen
            linesLabel.setText(String.format("%d", linesCleared));
        }
    }

    // Stop the timer

    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    // Pause the timer

    public void pauseTimer() {
        if (gameTimer != null) {
            gameTimer.pause();
        }
    }

    // Resume the timer

    public void resumeTimer() {
        if (gameTimer != null) {
            gameTimer.play();
        }
    }

    // Setters for completion callbacks
    public void setOnBlitzComplete(Runnable callback) {
        this.onBlitzComplete = callback;
    }

    public void setOnFortyLinesComplete(Runnable callback) {
        this.onFortyLinesComplete = callback;
    }
}