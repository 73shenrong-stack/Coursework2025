package com.comp2042.controller;

import com.comp2042.controller.GameController;
import com.comp2042.model.data.EventSource;
import com.comp2042.model.data.EventType;
import com.comp2042.model.data.MoveDownResult;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.game.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;
    private TestGameViewController viewController;

    @BeforeEach
    void setUp() {
        viewController = new TestGameViewController();
        controller = new GameController(viewController, GameMode.BLITZ);
    }

    @Test
    void testOnDownEvent_UserSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveDownResult result = controller.onDownEvent(event);

        assertNotNull(result);
        assertNotNull(result.getViewData());
    }

    @Test
    void testOnDownEvent_ThreadSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        MoveDownResult result = controller.onDownEvent(event);

        assertNotNull(result);
    }

    @Test
    void testOnLeftEvent() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        ViewData viewData = controller.onLeftEvent(event);

        assertNotNull(viewData);
    }

    @Test
    void testOnRightEvent() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData viewData = controller.onRightEvent(event);

        assertNotNull(viewData);
    }

    @Test
    void testOnRotateEvent() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        ViewData viewData = controller.onRotateEvent(event);

        assertNotNull(viewData);
    }

    @Test
    void testGetCurrentViewData() {
        ViewData viewData = controller.getCurrentViewData();

        assertNotNull(viewData);
        assertNotNull(viewData.getBrickData());
    }

    @Test
    void testGetCurrentBoardMatrix() {
        int[][] matrix = controller.getCurrentBoardMatrix();

        assertNotNull(matrix);
        assertTrue(matrix.length > 0);
        assertTrue(matrix[0].length > 0);
    }

    @Test
    void testCreateNewGame() {
        controller.createNewGame();

        int[][] matrix = controller.getCurrentBoardMatrix();
        assertNotNull(matrix);
    }

    @Test
    void testOnHoldEvent_FirstTime() {
        controller.onHoldEvent();

        ViewData viewData = controller.getCurrentViewData();
        assertNotNull(viewData);
    }

    @Test
    void testMultipleMovements() {
        controller.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        controller.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        controller.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        ViewData viewData = controller.getCurrentViewData();
        assertNotNull(viewData);
    }

    // Test stub for GameViewController
    private static class TestGameViewController extends com.comp2042.view.GameViewController {
        @Override
        public void initGameView(int[][] boardMatrix, ViewData brick, GameMode mode) {
            // Stub implementation
        }

        @Override
        public void refreshBrick(ViewData brick) {
            // Stub implementation
        }

        @Override
        public void refreshGameBackground(int[][] board) {
            // Stub implementation
        }

        @Override
        public void updateNextBrick(ViewData viewData) {
            // Stub implementation
        }

        @Override
        public void updateHeldBrick(ViewData viewData) {
            // Stub implementation
        }

        @Override
        public void bindScore(javafx.beans.property.IntegerProperty scoreProperty) {
            // Stub implementation
        }

        @Override
        public void gameOver() {
            // Stub implementation
        }
    }
}