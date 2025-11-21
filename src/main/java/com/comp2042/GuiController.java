package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private Pane gameOverOverlay;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private Pane nextBrickContainer;

    @FXML
    private GridPane HoldBrickPanel;

    @FXML
    private Pane HoldBrickContainer;

    @FXML
    private Label modeTimerLabel;

    @FXML
    private Label linesLabel;

    @FXML
    private Pane pauseOverlay;

    private Rectangle[][] nextBrickRectangles;

    private  Rectangle[][] holdBrickRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] shadowRectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private GameMode gameMode;
    private Timeline gameTimer;
    private int timeRemaining = 120;
    private int timeElapsed = 0;

    private int linesCleared = 0;
    private int targetLines = 40;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.C) {
                        if (eventListener instanceof GameController gc) {
                            gc.onHoldEvent();
                            ViewData vd = gc.getCurrentViewData();
                            refreshBrick(vd);
                            updateNextBrick(vd);
                            updateHeldBrick(vd);
                            refreshGameBackground(gc.getCurrentBoardMatrix());
                        }
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
                if (keyEvent.getCode() == KeyCode.P) {
                    pauseGame(null);
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    returnToMainMenu();
                }
            }
        });

        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick, GameMode mode) {
        this.gameMode = mode;
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        initHoldBrickPanel();

        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                brickPanel.getChildren().remove(rectangles[i][j]);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        shadowRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setOpacity(0.3); // Make shadow semi-transparent
                shadowRectangles[i][j] = rectangle;
                gamePanel.add(rectangle, j, i);
            }
        }

        initNextBrickPanel(brick.getNextBrickData());

        if (timeLine != null) {
            timeLine.stop();
        }

        Duration speed = getSpeedForMode(mode);
        timeLine = new Timeline(new KeyFrame(
                speed,
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        if (modeTimerLabel != null) {
            modeTimerLabel.setVisible(true);
        }
        if (linesLabel != null) {
            linesLabel.setVisible(true);
        }

        if (mode == GameMode.BLITZ) {
            startBlitzMode();
        }
        else if (mode == GameMode.FORTY_LINES) {
            start40LinesMode();
        }
        else {
            startZenMode();
        }
    }

    private void startBlitzMode() {
        timeRemaining = 120;
        linesCleared = 0;
        updateTimerDisplay();
        updateLinesDisplay();

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeRemaining--;
                    updateTimerDisplay();

                    if (timeRemaining <= 0) {
                        endBlitzMode();
                    }

                    if (timeRemaining == 10 && modeTimerLabel != null) {
                        modeTimerLabel.setTextFill(Color.RED);
                    }
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void start40LinesMode() {
        timeElapsed = 0;
        linesCleared = 0;
        targetLines = 40;
        updateTimerDisplay();
        updateLinesDisplay();

        if (modeTimerLabel != null) {
            modeTimerLabel.setTextFill(Color.YELLOW);
        }
        if (linesLabel != null) {
            linesLabel.setTextFill(Color.YELLOW);
        }

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeElapsed++;
                    updateTimerDisplay();
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void startZenMode() {
        timeElapsed = 0;
        linesCleared = 0;
        updateTimerDisplay();
        updateLinesDisplay();

        gameTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeElapsed++;
                    updateTimerDisplay();
                }
        ));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }

    private void updateTimerDisplay() {
        if (modeTimerLabel != null) {
            if (gameMode == GameMode.BLITZ) {
                // Countdown timer for Blitz
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                modeTimerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
            } else {
                // Counting up timer for 40 Lines and Zen
                int minutes = timeElapsed / 60;
                int seconds = timeElapsed % 60;
                modeTimerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
            }
        }
    }

    private void updateLinesDisplay() {
        if (linesLabel != null) {
            if (gameMode == GameMode.FORTY_LINES) {
                // Show progress towards 40 lines
                linesLabel.setText(String.format("Lines: %d / %d", linesCleared, targetLines));
            } else {
                // Show total lines cleared for Blitz and Zen
                linesLabel.setText(String.format("Lines: %d", linesCleared));
            }
        }
    }

    private void end40LinesMode() {
        if (timeLine != null) {
            timeLine.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Don't call gameOver() - just stop the game and show completion
        isGameOver.setValue(Boolean.TRUE);

        if (linesLabel != null) {
            linesLabel.setText("COMPLETE!");
            linesLabel.setTextFill(Color.LIME);
            linesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        }

        if (modeTimerLabel != null) {
            int minutes = timeElapsed / 60;
            int seconds = timeElapsed % 60;
            modeTimerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
        }
        showCompletionMessage();
    }

    private void showCompletionMessage() {
        NotificationPanel completionPanel = new NotificationPanel("40 LINES COMPLETE!");
        groupNotification.getChildren().add(completionPanel);
    }

    private void endBlitzMode() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOver();
    }

    private Duration getSpeedForMode(GameMode mode) {
        switch (mode) {
            case BLITZ:
                return Duration.millis(300); // Faster for Blitz
            case ZEN:
                return Duration.millis(600); // Slower for Zen
            case FORTY_LINES:
            default:
                return Duration.millis(400); // Normal speed
        }
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            // 1. CLEAR the old falling piece (the 4×4 rectangles)
            for (Rectangle[] row : rectangles) {
                for (Rectangle r : row) {
                    r.setFill(Color.TRANSPARENT);
                    if (r.getParent() == gamePanel) {
                        gamePanel.getChildren().remove(r);
                    }
                }
            }


            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    if (brick.getBrickData()[i][j] != 0) {
                        int gridX = brick.getxPosition() + j;
                        int gridY = brick.getyPosition() + i;

                        // Only draw inside the visible area (rows 2–24)
                        if (gridY >= 2) {
                            rectangles[i][j].setFill(getFillColor(brick.getBrickData()[i][j]));
                            rectangles[i][j].setArcWidth(6);
                            rectangles[i][j].setArcHeight(6);
                            gamePanel.add(rectangles[i][j], gridX, gridY - 2);
                        }
                    }
                }
            }

            updateShadow(brick);
            updateNextBrickDisplay(brick.getNextBrickData());
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(6);
        rectangle.setArcWidth(6);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);

            // Handle notifications for cleared lines
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {

                linesCleared += downData.getClearRow().getLinesRemoved();
                updateLinesDisplay();

                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());

                // Update lines cleared for all modes
                linesCleared += downData.getClearRow().getLinesRemoved();
                updateLinesDisplay();

                // Check 40 Lines mode completion
                if (gameMode == GameMode.FORTY_LINES && linesCleared >= targetLines) {
                    end40LinesMode();
                    gamePanel.requestFocus();
                    return;
                }
            }

            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
    }

    public void gameOver() {
        if (timeLine != null) {
            timeLine.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }
        if (nextBrickContainer != null) {
            nextBrickContainer.setVisible(false);
        }
        if (HoldBrickContainer != null) {
            HoldBrickContainer.setVisible(false);
        }
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }

        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }

        if (nextBrickContainer != null) {
            nextBrickContainer.setVisible(true);
        }
        if (HoldBrickContainer != null) {
            HoldBrickContainer.setVisible(true);
        }
        eventListener.createNewGame();

        if (gameMode == GameMode.BLITZ) {
            startBlitzMode();
        } else if (gameMode == GameMode.FORTY_LINES) {
            start40LinesMode();
        } else if (gameMode == GameMode.ZEN) {
            startZenMode();
        }

        gamePanel.requestFocus();

        if (timeLine != null) {
            timeLine.play();
        }
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        // Don't allow pausing when game is over
        if (isGameOver.getValue() == Boolean.TRUE) {
            return;
        }

        if (isPause.getValue() == Boolean.FALSE) {
            isPause.setValue(Boolean.TRUE);
            if (timeLine != null) {
                timeLine.pause();
            }
            if (gameTimer != null) {
                gameTimer.pause();
            }
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(true);
            }

            if (nextBrickContainer != null) {
                nextBrickContainer.setVisible(false);
            }
            if (HoldBrickContainer != null) {
                HoldBrickContainer.setVisible(false);
            }
        } else {
            isPause.setValue(Boolean.FALSE);
            if (timeLine != null) {
                timeLine.play();
            }
            if (gameTimer != null) {
                gameTimer.play();
            }
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(false);
            }
            if (nextBrickContainer != null) {
                nextBrickContainer.setVisible(true);
            }
            if (HoldBrickContainer != null) {
                HoldBrickContainer.setVisible(true);
            }
        }
        gamePanel.requestFocus();
    }

    private void returnToMainMenu() {
        // Stop all timers
        if (timeLine != null) {
            timeLine.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Use the static method from MainMenuController
        MainMenuController.returnToMainMenu(gamePanel);
    }

    private void initNextBrickPanel(int[][] nextBrickData) {
        // Create a 4x4 grid for next brick preview
        nextBrickRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcHeight(6);
                rectangle.setArcWidth(6);
                nextBrickRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
        updateNextBrickDisplay(nextBrickData);
    }

    private void updateNextBrickDisplay(int[][] nextBrickData) {
        // Clear the display first
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nextBrickRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // Display the next brick centered in the 4x4 grid
        if (nextBrickData != null) {
            // Count non-empty cells to find actual brick dimensions
            int minRow = 4, maxRow = -1, minCol = 4, maxCol = -1;
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    if (nextBrickData[i][j] != 0) {
                        minRow = Math.min(minRow, i);
                        maxRow = Math.max(maxRow, i);
                        minCol = Math.min(minCol, j);
                        maxCol = Math.max(maxCol, j);
                    }
                }
            }

            // Calculate actual brick size
            int brickHeight = maxRow - minRow + 1;
            int brickWidth = maxCol - minCol + 1;

            // Center the brick in the 4x4 grid
            int offsetX = (4 - brickWidth) / 2;
            int offsetY = (4 - brickHeight) / 2;

            // Display only the non-empty part of the brick, centered
            for (int i = minRow; i <= maxRow; i++) {
                for (int j = minCol; j <= maxCol; j++) {
                    int targetY = offsetY + (i - minRow);
                    int targetX = offsetX + (j - minCol);
                    if (targetY < 4 && targetX < 4 && nextBrickData[i][j] != 0) {
                        nextBrickRectangles[targetY][targetX].setFill(getFillColor(nextBrickData[i][j]));
                    }
                }
            }
        }
    }

    public void updateNextBrick(ViewData viewData) {
        updateNextBrickDisplay(viewData.getNextBrickData());
    }

    private void initHoldBrickPanel() {
        // Create a 4x4 grid for hold brick preview
        holdBrickRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcHeight(6);
                rectangle.setArcWidth(6);
                holdBrickRectangles[i][j] = rectangle;
                HoldBrickPanel.add(rectangle, j, i);  // column = j, row = i
            }
        }
    }

    public void updateHeldBrick(ViewData viewData) {
        // Clear the display first
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                holdBrickRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        if (viewData == null) return;

        int[][] heldBrickData = viewData.getHeldBrickData();

        // Display the held brick centered in the 4x4 grid
        if (heldBrickData != null) {
            // Count non-empty cells to find actual brick dimensions
            int minRow = 4, maxRow = -1, minCol = 4, maxCol = -1;
            for (int i = 0; i < heldBrickData.length; i++) {
                for (int j = 0; j < heldBrickData[i].length; j++) {
                    if (heldBrickData[i][j] != 0) {
                        minRow = Math.min(minRow, i);
                        maxRow = Math.max(maxRow, i);
                        minCol = Math.min(minCol, j);
                        maxCol = Math.max(maxCol, j);
                    }
                }
            }

            // Only proceed if we found blocks
            if (maxRow != -1) {
                // Calculate actual brick size
                int brickHeight = maxRow - minRow + 1;
                int brickWidth = maxCol - minCol + 1;

                // Center the brick in the 4x4 grid
                int offsetX = (4 - brickWidth) / 2;
                int offsetY = (4 - brickHeight) / 2;

                // Display only the non-empty part of the brick, centered
                for (int i = minRow; i <= maxRow; i++) {
                    for (int j = minCol; j <= maxCol; j++) {
                        int targetY = offsetY + (i - minRow);
                        int targetX = offsetX + (j - minCol);
                        if (targetY < 4 && targetX < 4 && heldBrickData[i][j] != 0) {
                            holdBrickRectangles[targetY][targetX].setFill(getFillColor(heldBrickData[i][j]));
                            holdBrickRectangles[targetY][targetX].setArcWidth(6);
                            holdBrickRectangles[targetY][targetX].setArcHeight(6);
                        }
                    }
                }
            }
        }
    }

    private void updateShadow(ViewData brick) {
        for (int i = 0; i < shadowRectangles.length; i++) {
            for (int j = 0; j < shadowRectangles[i].length; j++) {
                shadowRectangles[i][j].setFill(Color.TRANSPARENT);
                gamePanel.getChildren().remove(shadowRectangles[i][j]);
            }
        }

        if (brick.getShadowYPosition() != brick.getyPosition()) {
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    if (brick.getBrickData()[i][j] != 0) {
                        int gridX = brick.getxPosition() + j;
                        int gridY = brick.getShadowYPosition() + i;

                        if (gridY >= 2 && gridY < 25) {
                            shadowRectangles[i][j].setFill(getFillColor(brick.getBrickData()[i][j]));
                            shadowRectangles[i][j].setArcHeight(6);
                            shadowRectangles[i][j].setArcWidth(6);
                            gamePanel.add(shadowRectangles[i][j], gridX, gridY - 2);
                        }
                    }
                }
            }
        }
    }

    private void hardDrop() {
        if (eventListener == null) return;

        DownData downData;
        ViewData viewData;

        while (true) {
            downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
            viewData = downData.getViewData();

            refreshBrick(viewData);
            updateShadow(viewData);

            if (viewData.getyPosition() == viewData.getShadowYPosition()) {
                break;
            }
        }
        downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));

        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            linesCleared += downData.getClearRow().getLinesRemoved();
            updateLinesDisplay();

            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());

            // Check 40 Lines mode completion
            if (gameMode == GameMode.FORTY_LINES && linesCleared >= targetLines) {
                end40LinesMode();
                return;
            }
        }
        if (eventListener instanceof GameController gc) {
            refreshGameBackground(gc.getCurrentBoardMatrix());
        }
    }
}
