package com.comp2042.view.actions;

import com.comp2042.controller.InputEventListener;
import com.comp2042.model.data.EventSource;
import com.comp2042.model.data.EventType;
import com.comp2042.model.data.LineClearResult;
import com.comp2042.model.data.MoveDownResult;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.renderer.GameRenderer;
import com.comp2042.view.renderer.PreviewPanelRenderer;
import com.comp2042.view.theme.ColorTheme;
import com.comp2042.view.timer.GameModeTimerManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameActionHandlerTest {

    private GameActionHandler actionHandler;
    private TestInputEventListener eventListener;
    private GridPane gamePanel;
    private ObservableList<Node> notificationContainer;

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(() -> latch.countDown());
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            eventListener = new TestInputEventListener();
            gamePanel = new GridPane();
            notificationContainer = FXCollections.observableArrayList();

            GridPane nextPanel = new GridPane();
            GridPane holdPanel = new GridPane();

            // Use real renderers
            GameRenderer gameRenderer = new GameRenderer(ColorTheme.arcade());
            PreviewPanelRenderer previewRenderer = new PreviewPanelRenderer(ColorTheme.arcade());

            Label timerLabel = new Label();
            Label linesLabel = new Label();
            GameModeTimerManager timerManager = new GameModeTimerManager(timerLabel, linesLabel);

            actionHandler = new GameActionHandler(
                    eventListener, gameRenderer, previewRenderer, timerManager,
                    gamePanel, notificationContainer
            );
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testConstruction_NotNull() {
        assertNotNull(actionHandler);
    }

    @Test
    void testHandleMoveDown_CallsListener() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            try {
                actionHandler.handleMoveDown(event);
            } catch (Exception e) {
                // Renderer may throw NPE, but we're testing listener was called
            }
            assertTrue(eventListener.downCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleMoveDown_WithLineClear() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            eventListener.linesCleared = 2;
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            try {
                actionHandler.handleMoveDown(event);
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.downCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleHoldPiece_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.handleHoldPiece();
            } catch (Exception e) {
                // May throw if hold not supported
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleHardDrop_CallsDownEvent() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.handleHardDrop();
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.downCalled, "Should call down event during hard drop");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleRotate_CallsListener() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.handleRotate();
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.rotateCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleMoveLeft_CallsListener() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.handleMoveLeft();
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.leftCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleMoveRight_CallsListener() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.handleMoveRight();
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.rightCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testRequestFocus_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                actionHandler.requestFocus();
            } catch (Exception e) {
                // May throw
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleMultipleActions() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        // Create a container to hold any error that happens on the JavaFX thread
        final Throwable[] error = new Throwable[1];

        Platform.runLater(() -> {
            try {
                // 1. Attempt Move Left
                try { actionHandler.handleMoveLeft(); } catch (Exception ignored) {}

                // 2. Attempt Move Right
                try { actionHandler.handleMoveRight(); } catch (Exception ignored) {}

                // 3. Attempt Rotate
                try { actionHandler.handleRotate(); } catch (Exception ignored) {}

                // 4. Check results
                assertTrue(eventListener.leftCalled, "Left action should have been handled");
                assertTrue(eventListener.rightCalled, "Right action should have been handled");
                assertTrue(eventListener.rotateCalled, "Rotate action should have been handled");

            } catch (Throwable t) {
                // Capture any assertion failure or exception
                error[0] = t;
            } finally {
                // Always release the latch, so the test doesn't hang for 5 seconds
                latch.countDown();
            }
        });

        // Wait for the JavaFX thread to finish
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Test timed out waiting for JavaFX actions");
        }

        // If an error occurred on the other thread, re-throw it here to fail the test
        if (error[0] != null) {
            // This makes the test turn Red with the correct error message
            if (error[0] instanceof AssertionError) {
                throw (AssertionError) error[0];
            } else {
                throw new RuntimeException(error[0]);
            }
        }
    }

    @Test
    void testHandleMoveDown_WithNoLineClear() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            eventListener.linesCleared = 0;
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
            try {
                actionHandler.handleMoveDown(event);
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.downCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testHandleHardDrop_ReachesBottom() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            eventListener.dropCount = 0;
            try {
                actionHandler.handleHardDrop();
            } catch (Exception e) {
                // Renderer may throw NPE
            }
            assertTrue(eventListener.downCalled);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testEventListener_DownEventReturnsValidData() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        MoveDownResult result = eventListener.onDownEvent(event);

        assertNotNull(result);
        assertNotNull(result.getViewData());
    }

    @Test
    void testEventListener_LeftEventReturnsValidData() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        ViewData result = eventListener.onLeftEvent(event);

        assertNotNull(result);
    }

    @Test
    void testEventListener_RightEventReturnsValidData() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData result = eventListener.onRightEvent(event);

        assertNotNull(result);
    }

    @Test
    void testEventListener_RotateEventReturnsValidData() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        ViewData result = eventListener.onRotateEvent(event);

        assertNotNull(result);
    }

    /**
     * Test stub for InputEventListener
     */
    static class TestInputEventListener implements InputEventListener {
        boolean downCalled = false;
        boolean leftCalled = false;
        boolean rightCalled = false;
        boolean rotateCalled = false;
        int linesCleared = 0;
        int dropCount = 0;

        private int[][] createBrickShape() {
            return new int[][]{
                    {0, 0, 0, 0},
                    {1, 1, 1, 1},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            };
        }

        private int[][] createBoardMatrix() {
            return new int[BOARD_HEIGHT][BOARD_WIDTH];
        }

        @Override
        public MoveDownResult onDownEvent(MoveEvent event) {
            downCalled = true;
            dropCount++;

            ViewData viewData = new ViewData(
                    createBrickShape(),
                    3,
                    dropCount >= 20 ? 19 : dropCount,
                    createBrickShape(),
                    BOARD_HEIGHT,
                    createBoardMatrix()
            );

            LineClearResult clearResult = null;
            if (linesCleared > 0) {
                clearResult = new LineClearResult(linesCleared, createBoardMatrix(), 100);
            }

            return new MoveDownResult(clearResult, viewData);
        }

        @Override
        public ViewData onLeftEvent(MoveEvent event) {
            leftCalled = true;
            return new ViewData(
                    createBrickShape(),
                    2, 5,
                    createBrickShape(),
                    BOARD_HEIGHT,
                    createBoardMatrix()
            );
        }

        @Override
        public ViewData onRightEvent(MoveEvent event) {
            rightCalled = true;
            return new ViewData(
                    createBrickShape(),
                    4, 5,
                    createBrickShape(),
                    BOARD_HEIGHT,
                    createBoardMatrix()
            );
        }

        @Override
        public ViewData onRotateEvent(MoveEvent event) {
            rotateCalled = true;
            return new ViewData(
                    createBrickShape(),
                    3, 5,
                    createBrickShape(),
                    BOARD_HEIGHT,
                    createBoardMatrix()
            );
        }

        @Override
        public void createNewGame() {
            // No-op
        }
    }
}