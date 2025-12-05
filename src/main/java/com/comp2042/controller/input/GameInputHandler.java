package com.comp2042.controller.input;

import com.comp2042.model.data.EventSource;
import com.comp2042.model.data.EventType;
import com.comp2042.model.data.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles keyboard input for the Tetris game.
 * Manages key press and release events, tracks currently pressed keys and delegates actions to a callback interface.
 * Distinguishes between game controls (movement, rotation) and menu controls (pause, new game).
 */

public class GameInputHandler {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private InputCallback callback;

    /**
     * Callback interface for input action handling.
     * Implementations define what happens for each type of input.
     */
    public interface InputCallback {
        void onRotate();
        void onMoveLeft();
        void onMoveRight();
        void onMoveDown();
        void onHold();
        void onHardDrop();
        void onNewGame();
        void onPause();
        void onExit();
    }

    /**
     * Constructs a new input handler with the specified callback.
     *
     * @param callback the callback interface for handling input actions
     */
    public GameInputHandler(InputCallback callback) {
        this.callback = callback;
    }

    /**
     * Handles key press events.
     * Adds the key to the pressed keys set and delegates to appropriate handlers
     * based on game state. Game controls are only active when the game is not
     * paused and not over. Menu controls are always active.
     *
     * @param event the key press event
     * @param isPaused whether the game is currently paused
     * @param isGameOver whether the game is over
     */

    public void handleKeyPressed(KeyEvent event, boolean isPaused, boolean isGameOver) {
        pressedKeys.add(event.getCode());

        // Game controls (only when not paused and not game over)
        if (!isPaused && !isGameOver) {
            handleGameControls(event);
        }
        // Menu controls (always available)
        handleMenuControls(event);
    }

    /**
     * Handles game-specific controls (movement, rotation, hold, drops).
     * Supports simultaneous rotation and horizontal movement for advanced techniques.
     *
     * @param event the key press event
     */

    private void handleGameControls(KeyEvent event) {
        KeyCode code = event.getCode();

        // Rotation
        if (code == KeyCode.UP || code == KeyCode.W) {
            callback.onRotate();

            // Allow rotation + movement
            if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
                callback.onMoveLeft();
            } else if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
                callback.onMoveRight();
            }
            event.consume();
            return;
        }

        // Horizontal movement
        if (code == KeyCode.LEFT || code == KeyCode.A) {
            callback.onMoveLeft();
            event.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            callback.onMoveRight();
            event.consume();
        }

        // Down movement (soft drop)
        else if (code == KeyCode.DOWN || code == KeyCode.S) {
            callback.onMoveDown();
            event.consume();
        }

        // Hold piece
        else if (code == KeyCode.C) {
            callback.onHold();
            event.consume();
        }

        // Hard drop
        else if (code == KeyCode.SPACE) {
            callback.onHardDrop();
            event.consume();
        }
    }

    /**
     * Handles menu controls (new game, pause, exit).
     * These controls are active regardless of game state.
     *
     * @param event the key press event
     */

    private void handleMenuControls(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.N) {
            callback.onNewGame();
        } else if (code == KeyCode.P) {
            callback.onPause();
        } else if (code == KeyCode.M) {
            callback.onExit();
        }
    }

    /**
     * Handles key release events.
     * Removes the released key from the pressed keys set.
     *
     * @param event the key release event
     */

    public void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    /**
     * Clears all currently pressed keys.
     * Useful when losing focus or changing game states.
     */

    public void clearPressedKeys() {
        pressedKeys.clear();
    }

    /**
     * Checks if a specific key is currently pressed.
     *
     * @param keyCode the key code to check
     * @return true if the key is currently pressed, false otherwise
     */

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}