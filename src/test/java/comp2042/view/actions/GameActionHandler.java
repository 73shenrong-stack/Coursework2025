package comp2042.view.actions;

import comp2042.audio.AudioManager;
import comp2042.controller.GameController;
import comp2042.controller.InputEventListener;
import comp2042.model.data.*;
import comp2042.view.components.NotificationPanel;
import comp2042.view.renderer.GameRenderer;
import comp2042.view.renderer.PreviewPanelRenderer;
import comp2042.view.timer.GameModeTimerManager;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Handles game actions like move down, hold piece, and hard drop
 */
public class GameActionHandler {

    private final InputEventListener eventListener;
    private final GameRenderer gameRenderer;
    private final PreviewPanelRenderer previewRenderer;
    private final GameModeTimerManager timerManager;
    private final GridPane gamePanel;
    private final ObservableList<Node> notificationContainer;
    private final AudioManager audioManager;

    public GameActionHandler(InputEventListener eventListener, GameRenderer gameRenderer, PreviewPanelRenderer previewRenderer, GameModeTimerManager timerManager, GridPane gamePanel, ObservableList<Node> notificationContainer) {
        this.eventListener = eventListener;
        this.gameRenderer = gameRenderer;
        this.previewRenderer = previewRenderer;
        this.timerManager = timerManager;
        this.gamePanel = gamePanel;
        this.notificationContainer = notificationContainer;
        this.audioManager = AudioManager.getInstance();
    }

    // Handle piece moving down

    public MoveDownResult handleMoveDown(MoveEvent event) {
        MoveDownResult result = eventListener.onDownEvent(event);

        // Check for line clears
        if (result.getClearRow() != null && result.getClearRow().getLinesRemoved() > 0) {
            int linesCleared = result.getClearRow().getLinesRemoved();
            timerManager.addLinesCleared(linesCleared);
            showScoreNotification(result.getClearRow().getScoreBonus());
        }

        // Update display
        gameRenderer.refreshBrick(result.getViewData(), gamePanel);
        previewRenderer.updateNextBrickDisplay(result.getViewData().getNextBrickData());
        return result;
    }

    // Handle hold piece functionality

    public void handleHoldPiece() {
        if (eventListener instanceof GameController) {
            GameController gc = (GameController) eventListener;
            gc.onHoldEvent();

            ViewData vd = gc.getCurrentViewData();
            gameRenderer.refreshBrick(vd, gamePanel);
            previewRenderer.updateNextBrickDisplay(vd.getNextBrickData());
            previewRenderer.updateHoldBrickDisplay(vd.getHeldBrickData());
            gameRenderer.refreshGameBackground(gc.getCurrentBoardMatrix());
        }
    }

    // Handle hard drop

    public void handleHardDrop() {
        if (eventListener == null) {
            return;
        }

        MoveDownResult result;
        ViewData viewData;

        // Drop until landing
        while (true) {
            result = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
            viewData = result.getViewData();

            gameRenderer.refreshBrick(viewData, gamePanel);
            previewRenderer.updateNextBrickDisplay(result.getViewData().getNextBrickData());

            if (viewData.getYPosition() == viewData.getShadowYPosition()) {
                break;
            }
        }

        audioManager.playSound("drop");

        // Final merge
        result = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));

        if (result.getClearRow() != null && result.getClearRow().getLinesRemoved() > 0) {
            int linesCleared = result.getClearRow().getLinesRemoved();
            timerManager.addLinesCleared(linesCleared);
            showScoreNotification(result.getClearRow().getScoreBonus());
        }

        if (eventListener instanceof GameController) {
            GameController gc = (GameController) eventListener;
            gameRenderer.refreshGameBackground(gc.getCurrentBoardMatrix());
        }
    }

    // Handle rotation

    public void handleRotate() {
        ViewData vd = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        gameRenderer.refreshBrick(vd, gamePanel);
        previewRenderer.updateNextBrickDisplay(vd.getNextBrickData());
    }

    // Handle move left

    public void handleMoveLeft() {
        ViewData vd = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        gameRenderer.refreshBrick(vd, gamePanel);
    }

    // Handle move right

    public void handleMoveRight() {
        ViewData vd = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        gameRenderer.refreshBrick(vd, gamePanel);
    }

    // Show score notification animation

    private void showScoreNotification(int score) {
        NotificationPanel notification = new NotificationPanel("+" + score);
        notificationContainer.add(notification);
        notification.showScore(notificationContainer);
    }

    // Request focus on the game panel

    public void requestFocus() {
        gamePanel.requestFocus();
    }
}