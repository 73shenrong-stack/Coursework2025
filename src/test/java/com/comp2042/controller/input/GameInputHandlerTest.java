package com.comp2042.controller.input;

import com.comp2042.controller.input.GameInputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInputHandlerTest {

    private GameInputHandler handler;
    private TestInputCallback callback;

    @BeforeEach
    void setUp() {
        callback = new TestInputCallback();
        handler = new GameInputHandler(callback);
    }

    @Test
    void testIsKeyPressed_InitiallyEmpty() {
        assertFalse(handler.isKeyPressed(KeyCode.UP));
        assertFalse(handler.isKeyPressed(KeyCode.DOWN));
    }

    @Test
    void testClearPressedKeys() {
        // Simulate key press
        KeyEvent pressEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.UP,
                false, false, false, false
        );
        handler.handleKeyPressed(pressEvent, false, false);

        handler.clearPressedKeys();

        assertFalse(handler.isKeyPressed(KeyCode.UP));
    }

    @Test
    void testHandleKeyPressed_RotateWithUp() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.UP,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.rotateCalled);
    }

    @Test
    void testHandleKeyPressed_RotateWithW() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.W,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.rotateCalled);
    }

    @Test
    void testHandleKeyPressed_MoveLeftWithLeft() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveLeftCalled);
    }

    @Test
    void testHandleKeyPressed_MoveLeftWithA() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.A,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveLeftCalled);
    }

    @Test
    void testHandleKeyPressed_MoveRightWithRight() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveRightCalled);
    }

    @Test
    void testHandleKeyPressed_MoveRightWithD() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.D,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveRightCalled);
    }

    @Test
    void testHandleKeyPressed_MoveDownWithDown() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveDownCalled);
    }

    @Test
    void testHandleKeyPressed_MoveDownWithS() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.S,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.moveDownCalled);
    }

    @Test
    void testHandleKeyPressed_HoldWithC() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.C,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.holdCalled);
    }

    @Test
    void testHandleKeyPressed_HardDropWithSpace() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.hardDropCalled);
    }

    @Test
    void testHandleKeyPressed_NewGameWithN() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.N,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.newGameCalled);
    }

    @Test
    void testHandleKeyPressed_PauseWithP() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.P,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.pauseCalled);
    }

    @Test
    void testHandleKeyPressed_ExitWithM() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.M,
                false, false, false, false
        );

        handler.handleKeyPressed(event, false, false);

        assertTrue(callback.exitCalled);
    }

    @Test
    void testHandleKeyPressed_WhenPaused_OnlyMenuControls() {
        KeyEvent moveEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT,
                false, false, false, false
        );

        handler.handleKeyPressed(moveEvent, true, false);

        assertFalse(callback.moveLeftCalled, "Game controls should be disabled when paused");

        KeyEvent menuEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.N,
                false, false, false, false
        );

        handler.handleKeyPressed(menuEvent, true, false);

        assertTrue(callback.newGameCalled, "Menu controls should work when paused");
    }

    @Test
    void testHandleKeyPressed_WhenGameOver_OnlyMenuControls() {
        KeyEvent moveEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN,
                false, false, false, false
        );

        handler.handleKeyPressed(moveEvent, false, true);

        assertFalse(callback.moveDownCalled, "Game controls should be disabled when game over");
    }

    @Test
    void testHandleKeyReleased() {
        KeyEvent pressEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT,
                false, false, false, false
        );
        handler.handleKeyPressed(pressEvent, false, false);

        KeyEvent releaseEvent = new KeyEvent(
                KeyEvent.KEY_RELEASED, "", "", KeyCode.LEFT,
                false, false, false, false
        );
        handler.handleKeyReleased(releaseEvent);

        assertFalse(handler.isKeyPressed(KeyCode.LEFT));
    }

    // Test callback implementation
    private static class TestInputCallback implements GameInputHandler.InputCallback {
        boolean rotateCalled = false;
        boolean moveLeftCalled = false;
        boolean moveRightCalled = false;
        boolean moveDownCalled = false;
        boolean holdCalled = false;
        boolean hardDropCalled = false;
        boolean newGameCalled = false;
        boolean pauseCalled = false;
        boolean exitCalled = false;

        @Override
        public void onRotate() { rotateCalled = true; }

        @Override
        public void onMoveLeft() { moveLeftCalled = true; }

        @Override
        public void onMoveRight() { moveRightCalled = true; }

        @Override
        public void onMoveDown() { moveDownCalled = true; }

        @Override
        public void onHold() { holdCalled = true; }

        @Override
        public void onHardDrop() { hardDropCalled = true; }

        @Override
        public void onNewGame() { newGameCalled = true; }

        @Override
        public void onPause() { pauseCalled = true; }

        @Override
        public void onExit() { exitCalled = true; }
    }
}