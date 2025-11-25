package comp2042.view;

import comp2042.controller.InputEventListener;
import comp2042.controller.input.GameInputHandler;
import comp2042.model.data.EventSource;
import comp2042.model.data.EventType;
import comp2042.model.data.MoveEvent;
import comp2042.model.data.ViewData;
import comp2042.model.game.GameMode;
import comp2042.view.actions.GameActionHandler;
import comp2042.view.init.GameInitializationManager;
import comp2042.view.lifecycle.GameLifecycleManager;
import comp2042.view.renderer.GameRenderer;
import comp2042.view.renderer.PreviewPanelRenderer;
import comp2042.view.theme.ColorTheme;
import comp2042.view.timer.GameModeTimerManager;
import comp2042.view.ui.UIStateManager;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
*Main view controller for the Tetris game.
*Delegates responsibilities to specialized managers and handlers.
 */
public class GameViewController implements Initializable {

    // FXML Components
    @FXML private StackPane rootPane;
    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private Pane gameOverOverlay;
    @FXML private Label scoreLabel;
    @FXML private GridPane nextBrickPanel;
    @FXML private VBox nextBrickContainer;
    @FXML private GridPane HoldBrickPanel;
    @FXML private VBox HoldBrickContainer;
    @FXML private Label modeTimerLabel;
    @FXML private Label linesLabel;
    @FXML private Pane pauseOverlay;

    // Specialized Managers and Handlers
    private GameRenderer gameRenderer;
    private PreviewPanelRenderer previewRenderer;
    private GameModeTimerManager timerManager;
    private GameActionHandler actionHandler;
    private GameLifecycleManager lifecycleManager;
    private UIStateManager uiStateManager;
    private GameInitializationManager initManager;
    private GameInputHandler inputHandler;

    // Core game state
    private InputEventListener eventListener;
    private Timeline gameLoopTimeline;
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private GameMode currentGameMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupManagers();
        setupInputHandler();
        setupGamePanel();
    }
    //Setup all managers
    private void setupManagers() {
        uiStateManager = new UIStateManager(gameOverOverlay, pauseOverlay, nextBrickContainer, HoldBrickContainer, modeTimerLabel, linesLabel);

        initManager = new GameInitializationManager(rootPane, gamePanel, brickPanel);
    }
    //Setup the game panel for keyboard input
    private void setupGamePanel() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(event -> {
            if (inputHandler != null) {inputHandler.handleKeyPressed(event, isPaused.getValue(), isGameOver.getValue());}
        });
        gamePanel.setOnKeyReleased(event -> {
            if (inputHandler != null) {inputHandler.handleKeyReleased(event);}
        });
    }
    //Setup the input handler with action callbacks
    private void setupInputHandler() {
        inputHandler = new GameInputHandler(new GameInputHandler.InputCallback() {
            @Override
            public void onRotate() {actionHandler.handleRotate();}
            @Override
            public void onMoveLeft() {actionHandler.handleMoveLeft();}
            @Override
            public void onMoveRight() {actionHandler.handleMoveRight();}
            @Override
            public void onMoveDown() {
                actionHandler.handleMoveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                actionHandler.requestFocus();}
            @Override
            public void onHold() {actionHandler.handleHoldPiece();}
            @Override
            public void onHardDrop() {actionHandler.handleHardDrop();}
            @Override
            public void onNewGame() {newGame(null);}
            @Override
            public void onPause() {pauseGame(null);}
            @Override
            public void onExit() {lifecycleManager.returnToMainMenu();}
        });
    }
    //Initialize the game view with board and game mode
    public void initGameView(int[][] boardMatrix, ViewData brick, GameMode mode) {
        this.currentGameMode = mode;
        // Setup theme
        ColorTheme colorTheme = ColorTheme.forMode(mode);
        initManager.applyTheme(colorTheme);
        // Initialize renderers
        GameInitializationManager.RendererBundle renderers = initManager.initializeRenderers(boardMatrix, brick, nextBrickPanel, HoldBrickPanel, colorTheme);
        this.gameRenderer = renderers.getGameRenderer();
        this.previewRenderer = renderers.getPreviewRenderer();
        // Position brick panel
        initManager.positionBrickPanel(brick);
        // Initialize timer manager
        timerManager = new GameModeTimerManager(modeTimerLabel, linesLabel);
        // Initialize action handler
        actionHandler = new GameActionHandler(eventListener, gameRenderer, previewRenderer, timerManager, gamePanel, groupNotification.getChildren()
        );
        // Create game loop
        gameLoopTimeline = initManager.createGameLoop(mode,
                () -> {
                    if (!isPaused.getValue()) {
                        actionHandler.handleMoveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                        actionHandler.requestFocus();
                    }
                });
        // Initialize lifecycle manager
        lifecycleManager = new GameLifecycleManager(eventListener, gameLoopTimeline, timerManager, uiStateManager, isPaused, isGameOver, currentGameMode, gamePanel
        );
        // Set lifecycle callbacks on timer manager
        timerManager.setOnBlitzComplete(lifecycleManager::handleBlitzComplete);
        timerManager.setOnFortyLinesComplete(lifecycleManager::handleFortyLinesComplete);
        // Start game
        gameLoopTimeline.play();
        timerManager.startTimer(mode);
        // Reset state
        isPaused.setValue(false);
        isGameOver.setValue(false);
        uiStateManager.showGameUI();
    }
    //Update next brick display
    public void updateNextBrick(ViewData viewData) {previewRenderer.updateNextBrickDisplay(viewData.getNextBrickData());
    }
    //Update held brick display
    public void updateHeldBrick(ViewData viewData) {previewRenderer.updateHoldBrickDisplay(viewData.getHeldBrickData());
    }
    //Refresh brick display
    public void refreshBrick(ViewData brick) {
        if (!isPaused.getValue()) {
            gameRenderer.refreshBrick(brick, gamePanel);
            previewRenderer.updateNextBrickDisplay(brick.getNextBrickData());
        }
    }
    //Refresh game background (locked pieces)
    public void refreshGameBackground(int[][] board) {gameRenderer.refreshGameBackground(board);
    }
    //Bind score property to label
    public void bindScore(IntegerProperty scoreProperty) {scoreLabel.textProperty().bind(scoreProperty.asString("%d"));
    }
    //Handle game over
    public void gameOver() {lifecycleManager.handleGameOver();
    }
    //Start a new game
    public void newGame(ActionEvent actionEvent) {
        lifecycleManager.startNewGame();
        gamePanel.requestFocus();
    }
    //Pause/resume the game
    public void pauseGame(ActionEvent actionEvent) {
        lifecycleManager.pauseGame();
        gamePanel.requestFocus();
    }
    //Set the event listener
    public void setEventListener(InputEventListener listener) {this.eventListener = listener;
    }
}