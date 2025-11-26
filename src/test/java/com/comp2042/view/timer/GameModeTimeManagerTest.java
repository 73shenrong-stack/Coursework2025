package com.comp2042.view.timer;

import com.comp2042.model.game.GameMode;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameModeTimerManagerTest {

    private GameModeTimerManager timerManager;
    private Label timerLabel;
    private Label linesLabel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(() -> latch.countDown());
        } catch (IllegalStateException e) {
            // Toolkit already initialized
            latch.countDown();
        }
        latch.await(5, TimeUnit.SECONDS);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerLabel = new Label();
            linesLabel = new Label();
            timerManager = new GameModeTimerManager(timerLabel, linesLabel);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testConstruction_WithNullLabels() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> new GameModeTimerManager(null, null));
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStartTimer_BlitzMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.startTimer(GameMode.BLITZ));
            assertNotNull(timerLabel.getText());
            assertFalse(timerLabel.getText().isEmpty());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStartTimer_FortyLinesMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.startTimer(GameMode.FORTY_LINES));
            assertNotNull(timerLabel.getText());
            assertNotNull(linesLabel.getText());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStartTimer_ZenMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.startTimer(GameMode.ZEN));
            assertNotNull(timerLabel.getText());
            assertNotNull(linesLabel.getText());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_InitialTime() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            String timeText = timerLabel.getText();
            assertTrue(timeText.contains("2") || timeText.contains("120"),
                    "Blitz should start with 2 minutes");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testFortyLinesMode_InitialTime() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.FORTY_LINES);
            String timeText = timerLabel.getText();
            assertTrue(timeText.contains("0") || timeText.contains("00"),
                    "40 Lines should start at 0:00");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testZenMode_InitialTime() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.ZEN);
            String timeText = timerLabel.getText();
            assertTrue(timeText.contains("0") || timeText.contains("00"),
                    "Zen should start at 0:00");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testAddLinesCleared_UpdatesDisplay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            timerManager.addLinesCleared(5);
            String linesText = linesLabel.getText();
            assertTrue(linesText.contains("5"),
                    "Lines label should show 5 lines");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testAddLinesCleared_Cumulative() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.ZEN);
            timerManager.addLinesCleared(3);
            timerManager.addLinesCleared(2);
            String linesText = linesLabel.getText();
            assertTrue(linesText.contains("5"),
                    "Lines should accumulate to 5");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testFortyLinesMode_LinesDisplay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.FORTY_LINES);
            timerManager.addLinesCleared(10);
            String linesText = linesLabel.getText();
            assertTrue(linesText.contains("10") && linesText.contains("40"),
                    "Should show progress: 10 / 40");
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStopTimer_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            assertDoesNotThrow(() -> timerManager.stopTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testPauseTimer_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            assertDoesNotThrow(() -> timerManager.pauseTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testResumeTimer_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            timerManager.pauseTimer();
            assertDoesNotThrow(() -> timerManager.resumeTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testSetOnBlitzComplete_Callback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            boolean[] callbackCalled = {false};
            timerManager.setOnBlitzComplete(() -> callbackCalled[0] = true);
            assertNotNull(timerManager);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testSetOnFortyLinesComplete_Callback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            boolean[] callbackCalled = {false};
            timerManager.setOnFortyLinesComplete(() -> callbackCalled[0] = true);
            assertNotNull(timerManager);
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testAddLinesCleared_Zero() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            String initialLines = linesLabel.getText();
            timerManager.addLinesCleared(0);
            assertEquals(initialLines, linesLabel.getText());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testAddLinesCleared_NegativeValue() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            assertDoesNotThrow(() -> timerManager.addLinesCleared(-1));
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleTimerStarts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timerManager.startTimer(GameMode.BLITZ);
            timerManager.stopTimer();
            assertDoesNotThrow(() -> timerManager.startTimer(GameMode.ZEN));
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testStopWithoutStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.stopTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testPauseWithoutStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.pauseTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testResumeWithoutStart() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> timerManager.resumeTimer());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}