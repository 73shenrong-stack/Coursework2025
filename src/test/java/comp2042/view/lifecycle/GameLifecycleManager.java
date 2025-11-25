package comp2042.view.lifecycle;

import comp2042.audio.AudioManager;
import comp2042.controller.InputEventListener;
import comp2042.controller.MainMenuController;
import comp2042.model.game.GameMode;
import comp2042.view.timer.GameModeTimerManager;
import comp2042.view.ui.UIStateManager;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;

/**
 * Manages game lifecycle events (start, pause, resume, game over, new game)
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

    public GameLifecycleManager(InputEventListener eventListener, Timeline gameLoopTimeline, GameModeTimerManager timerManager, UIStateManager uiStateManager, BooleanProperty isPaused, BooleanProperty isGameOver, GameMode currentGameMode, Node sceneNode) {
        this.eventListener = eventListener;
        this.gameLoopTimeline = gameLoopTimeline;
        this.timerManager = timerManager;
        this.uiStateManager = uiStateManager;
        this.isPaused = isPaused;
        this.isGameOver = isGameOver;
        this.currentGameMode = currentGameMode;
        this.audioManager = AudioManager.getInstance();
        this.sceneNode = sceneNode;
    }

    // Start a new game

    public void startNewGame() {
        stopAllTimelines();

        uiStateManager.hideGameOverOverlay();
        uiStateManager.showGameUI();

        eventListener.createNewGame();

        // Restart timers
        timerManager.startTimer(currentGameMode);
        gameLoopTimeline.play();

        isPaused.setValue(false);
        isGameOver.setValue(false);
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

    // Resume the game

    public void resumeGame() {
        isPaused.setValue(false);
        gameLoopTimeline.play();
        timerManager.resumeTimer();
        uiStateManager.hidePauseOverlay();
        audioManager.resumeBackground();
        uiStateManager.showGameUI();
    }

    // Handle game over

    public void handleGameOver() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        uiStateManager.showGameOverOverlay();
        uiStateManager.hideGameUI();
    }

    // Handle Blitz mode completion

    public void handleBlitzComplete() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        audioManager.playSound("gameover");
        uiStateManager.showGameOverOverlay();
        uiStateManager.hideGameUI();
    }

    // Handle 40 Lines mode completion

    public void handleFortyLinesComplete() {
        stopAllTimelines();
        isGameOver.setValue(true);
        audioManager.stopBackground();
        audioManager.playSound("victory");
        uiStateManager.showCompletionOverlay();
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

}