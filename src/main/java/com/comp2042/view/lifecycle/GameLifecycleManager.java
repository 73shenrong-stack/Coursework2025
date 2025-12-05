package com.comp2042.view.lifecycle;

import com.comp2042.audio.AudioManager;
import com.comp2042.controller.InputEventListener;
import com.comp2042.controller.MainMenuController;
import com.comp2042.model.game.GameMode;
import com.comp2042.model.game.GameRecords;
import com.comp2042.model.game.RecordsPersistence;
import com.comp2042.model.game.Score;
import com.comp2042.view.timer.GameModeTimerManager;
import com.comp2042.view.ui.UIStateManager;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;

/**
 * Central manager responsible for controlling the entire game lifecycle.
 */
public class GameLifecycleManager {

    private final InputEventListener eventListener;
    private final Timeline gameLoopTimeline;
    private final GameModeTimerManager timerManager;
    private final UIStateManager uiStateManager;
    private final BooleanProperty isPaused;
    private final BooleanProperty isGameOver;
    private final GameMode currentGameMode;
    private final AudioManager audioManager;
    private final Node sceneNode;
    private final Score score;
    private final GameRecords records;

    /**
     * Constructs a new {@code GameLifecycleManager} with all required dependencies.
     *
     * @param eventListener     the controller that handles game input events
     * @param gameLoopTimeline  the main game loop Timeline
     * @param timerManager      manages mode-specific timers (Blitz, 40 Lines)
     * @param uiStateManager    controls visibility of overlays and UI elements
     * @param isPaused          property indicating whether the game is currently paused
     * @param isGameOver        property indicating whether the game has ended
     * @param currentGameMode   the active GameMode
     * @param sceneNode         the root node of the current scene (used for navigation)
     * @param score             the current game Score object
     */
    public GameLifecycleManager(InputEventListener eventListener, Timeline gameLoopTimeline, GameModeTimerManager timerManager, UIStateManager uiStateManager, BooleanProperty isPaused, BooleanProperty isGameOver, GameMode currentGameMode, Node sceneNode, Score score) {
        this.eventListener = eventListener;
        this.gameLoopTimeline = gameLoopTimeline;
        this.timerManager = timerManager;
        this.uiStateManager = uiStateManager;
        this.isPaused = isPaused;
        this.isGameOver = isGameOver;
        this.currentGameMode = currentGameMode;
        this.audioManager = AudioManager.getInstance();
        this.sceneNode = sceneNode;
        this.score = score;
        this.records = RecordsPersistence.loadRecords();
    }

    /**
     * Starts a completely new game from scratch.
     * Resets pause/game-over states, hides all overlays, initializes a fresh game
     */
    public void startNewGame() {
        stopAllTimelines();

        isPaused.setValue(false);
        isGameOver.setValue(false);

        uiStateManager.hideGameOverOverlay();
        uiStateManager.hidePauseOverlay();
        uiStateManager.showGameUI();

        eventListener.createNewGame();

        // Restart timers
        timerManager.startTimer(currentGameMode);
        gameLoopTimeline.play();
    }

    // Pause the game

    public void pauseGame() {
        if (isGameOver.getValue()) {
            return;
        }

        if (!isPaused.getValue()) {
            // Pause
            isPaused.setValue(true);
            gameLoopTimeline.pause();
            timerManager.pauseTimer();
            uiStateManager.showPauseOverlay();
            audioManager.pauseBackground();
            uiStateManager.hideGameUI();
        } else {
            // Resume
            resumeGame();
        }
    }

    /** Resume the game
    * Restores the game loop, timer, background music, and hides the pause overlay.
     */
    public void resumeGame() {
        isPaused.setValue(false);
        gameLoopTimeline.play();
        timerManager.resumeTimer();
        uiStateManager.hidePauseOverlay();
        audioManager.resumeBackground();
        uiStateManager.showGameUI();
    }

    // Handle game over
    // Stop all the timers
    public void handleGameOver() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        audioManager.playSound("gameover");

        int finalScore = score.scoreProperty().get();
        int finalTime = timerManager.getCurrentTime();

        // Show overlay WITHOUT saving records (game was not completed)
        uiStateManager.showGameOverOverlay(currentGameMode, finalScore, finalTime, records, false);
        uiStateManager.hideGameUI();
    }

    // Handle Blitz mode completion

    public void handleBlitzComplete() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        audioManager.playSound("gameover");

        int finalScore = score.scoreProperty().get();
        int finalTime = 120; // Always 2 minutes for Blitz completion

        // Save record ONLY on completion (2 minutes reached)
        boolean isNewRecord = records.updateBlitzRecord(finalScore, finalTime);
        if (isNewRecord) {
            RecordsPersistence.saveRecords(records);
        }

        uiStateManager.showGameOverOverlay(currentGameMode, finalScore, finalTime, records, isNewRecord);
        uiStateManager.hideGameUI();
    }

    /**
     * Handles successful completion of 40 Lines mode.
     * Plays victory sound, saves the result if it's a new record and displays the special completion overlay with time and score.
     */

    public void handleFortyLinesComplete() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        audioManager.playSound("victory");

        int finalScore = score.scoreProperty().get();
        int finalTime = timerManager.getCurrentTime();

        // Save record ONLY on completion (40 lines cleared)
        boolean isNewRecord = records.updateFortyLinesRecord(finalScore, finalTime);
        if (isNewRecord) {
            RecordsPersistence.saveRecords(records);
        }

        uiStateManager.showCompletionOverlay(finalScore, finalTime, records, isNewRecord);
        uiStateManager.hideGameUI();
    }

    // Return to main menu

    public void returnToMainMenu() {
        stopAllTimelines();
        audioManager.stopBackground();
        MainMenuController.returnToMainMenu(sceneNode);
    }

    // Stop all game timelines

    private void stopAllTimelines() {
        if (gameLoopTimeline != null) {
            gameLoopTimeline.stop();
        }
        if (timerManager != null) {
            timerManager.stopTimer();
        }
    }

    /**
     * Returns the current loaded high score records.
     *
     * @return the GameRecords object containing all persisted best scores
     */
    public GameRecords getRecords() {
        return records;
    }
}