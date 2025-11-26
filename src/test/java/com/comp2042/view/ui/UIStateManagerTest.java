package com.comp2042.view.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UIStateManagerTest {

    private UIStateManager uiStateManager;
    private Pane gameOverOverlay;
    private Pane pauseOverlay;
    private VBox nextBrickContainer;
    private VBox holdBrickContainer;
    private Label modeTimerLabel;
    private Label linesLabel;

    @BeforeEach
    void setUp() {
        gameOverOverlay = new Pane();
        pauseOverlay = new Pane();
        nextBrickContainer = new VBox();
        holdBrickContainer = new VBox();
        modeTimerLabel = new Label();
        linesLabel = new Label();

        uiStateManager = new UIStateManager(
                gameOverOverlay, pauseOverlay, nextBrickContainer,
                holdBrickContainer, modeTimerLabel, linesLabel
        );
    }

    @Test
    void testConstruction_InitializesOverlaysHidden() {
        assertFalse(pauseOverlay.isVisible(), "Pause overlay should be initially hidden");
        assertFalse(gameOverOverlay.isVisible(), "Game over overlay should be initially hidden");
    }

    @Test
    void testShowPauseOverlay() {
        uiStateManager.showPauseOverlay();

        assertTrue(pauseOverlay.isVisible(), "Pause overlay should be visible");
    }

    @Test
    void testHidePauseOverlay() {
        uiStateManager.showPauseOverlay();
        uiStateManager.hidePauseOverlay();

        assertFalse(pauseOverlay.isVisible(), "Pause overlay should be hidden");
    }

    @Test
    void testShowGameOverOverlay() {
        uiStateManager.showGameOverOverlay();

        assertTrue(gameOverOverlay.isVisible(), "Game over overlay should be visible");
    }

    @Test
    void testHideGameOverOverlay() {
        uiStateManager.showGameOverOverlay();
        uiStateManager.hideGameOverOverlay();

        assertFalse(gameOverOverlay.isVisible(), "Game over overlay should be hidden");
    }

    @Test
    void testShowCompletionOverlay() {
        uiStateManager.showCompletionOverlay();

        assertTrue(gameOverOverlay.isVisible(), "Completion overlay should be visible");
        assertFalse(gameOverOverlay.getChildren().isEmpty(),
                "Completion overlay should have content");
    }

    @Test
    void testShowGameUI() {
        // Initially hide UI
        uiStateManager.hideGameUI();

        uiStateManager.showGameUI();

        assertTrue(nextBrickContainer.isVisible());
        assertTrue(holdBrickContainer.isVisible());
        assertTrue(modeTimerLabel.isVisible());
        assertTrue(linesLabel.isVisible());
    }

    @Test
    void testHideGameUI() {
        uiStateManager.hideGameUI();

        assertFalse(nextBrickContainer.isVisible());
        assertFalse(holdBrickContainer.isVisible());
        assertFalse(modeTimerLabel.isVisible());
        assertFalse(linesLabel.isVisible());
    }

    @Test
    void testSetGameUIVisibility_True() {
        uiStateManager.setGameUIVisibility(true);

        assertTrue(nextBrickContainer.isVisible());
        assertTrue(holdBrickContainer.isVisible());
        assertTrue(modeTimerLabel.isVisible());
        assertTrue(linesLabel.isVisible());
    }

    @Test
    void testSetGameUIVisibility_False() {
        uiStateManager.setGameUIVisibility(false);

        assertFalse(nextBrickContainer.isVisible());
        assertFalse(holdBrickContainer.isVisible());
        assertFalse(modeTimerLabel.isVisible());
        assertFalse(linesLabel.isVisible());
    }

    @Test
    void testMultipleShowHideCycles_PauseOverlay() {
        uiStateManager.showPauseOverlay();
        uiStateManager.hidePauseOverlay();
        uiStateManager.showPauseOverlay();

        assertTrue(pauseOverlay.isVisible());
    }

    @Test
    void testMultipleShowHideCycles_GameOverOverlay() {
        uiStateManager.showGameOverOverlay();
        uiStateManager.hideGameOverOverlay();
        uiStateManager.showGameOverOverlay();

        assertTrue(gameOverOverlay.isVisible());
    }

    @Test
    void testShowGameUI_AfterHide() {
        uiStateManager.hideGameUI();
        uiStateManager.showGameUI();

        assertTrue(nextBrickContainer.isVisible());
    }

    @Test
    void testConstruction_WithNullComponents() {
        assertDoesNotThrow(() -> {
            new UIStateManager(null, null, null, null, null, null);
        });
    }

    @Test
    void testShowPauseOverlay_WithNullPane() {
        UIStateManager manager = new UIStateManager(null, null, nextBrickContainer,
                holdBrickContainer, modeTimerLabel, linesLabel);

        assertDoesNotThrow(() -> manager.showPauseOverlay());
    }

    @Test
    void testShowGameOverOverlay_WithNullPane() {
        UIStateManager manager = new UIStateManager(null, null, nextBrickContainer,
                holdBrickContainer, modeTimerLabel, linesLabel);

        assertDoesNotThrow(() -> manager.showGameOverOverlay());
    }

    @Test
    void testHideGameUI_WithNullComponents() {
        UIStateManager manager = new UIStateManager(gameOverOverlay, pauseOverlay,
                null, null, null, null);

        assertDoesNotThrow(() -> manager.hideGameUI());
    }

    @Test
    void testShowCompletionOverlay_HasCorrectContent() {
        uiStateManager.showCompletionOverlay();

        assertTrue(gameOverOverlay.isVisible());
        assertTrue(gameOverOverlay.getChildren().size() > 0);
    }

    @Test
    void testOverlayStateIndependence() {
        uiStateManager.showPauseOverlay();
        uiStateManager.showGameOverOverlay();

        assertTrue(pauseOverlay.isVisible(), "Both overlays can be visible");
        assertTrue(gameOverOverlay.isVisible());
    }
}