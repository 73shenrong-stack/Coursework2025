package com.comp2042.controller.input;

import com.comp2042.model.data.EventSource;
import com.comp2042.model.data.EventType;
import com.comp2042.model.data.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

// Handles keyboard input for the game

public class GameInputHandler {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private InputCallback callback;

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

    public GameInputHandler(InputCallback callback) {
        this.callback = callback;
    }

    // Handle key press event

    public void handleKeyPressed(KeyEvent event, boolean isPaused, boolean isGameOver) {
        pressedKeys.add(event.getCode());

        // Game controls (only when not paused and not game over)
        if (!isPaused && !isGameOver) {
            handleGameControls(event);
        }
        // Menu controls (always available)
        handleMenuControls(event);
    }

    // Handle game-specific controls (movement, rotation, etc.)

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

    // Handle menu controls (new game, pause, exit)

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

    // Handle key release event

    public void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    // Clear all pressed keys

    public void clearPressedKeys() {
        pressedKeys.clear();
    }

    // Check if a key is currently pressed

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}