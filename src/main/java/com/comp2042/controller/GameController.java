package com.comp2042.controller;

import com.comp2042.audio.AudioManager;
import com.comp2042.constants.GameConstants;
import com.comp2042.model.board.Board;
import com.comp2042.model.board.TetrisBoard;
import com.comp2042.model.data.EventSource;
import com.comp2042.model.data.LineClearResult;
import com.comp2042.model.data.MoveDownResult;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.game.GameMode;
import com.comp2042.model.game.Score;
import com.comp2042.view.GameViewController;

/**
 * Main game controller that handles game logic and coordinates between model and view
 */

public class GameController implements InputEventListener {

    private Board board = new TetrisBoard(GameConstants.BOARD_HEIGHT, GameConstants.BOARD_WIDTH);
    private final GameViewController viewController;
    private final GameMode gameMode;
    private final AudioManager audioManager = AudioManager.getInstance();

    public GameController(GameViewController controller, GameMode mode) {
        this.viewController = controller;
        this.gameMode = mode;

        audioManager.playBackground(gameMode);

        board.createNewBrick();
        viewController.setEventListener(this);
        viewController.initGameView(board.getBoardMatrix(), board.getViewData(), gameMode);
        viewController.updateHeldBrick(board.getViewData());
        viewController.bindScore(board.getScore().scoreProperty());
    }

    public void onHoldEvent() {
        if (board.holdBrick()) {
            ViewData vd = board.getViewData();
            viewController.refreshBrick(vd);
            viewController.updateNextBrick(vd);
            viewController.updateHeldBrick(vd);
            viewController.refreshGameBackground(board.getBoardMatrix());
        }
    }

    @Override
    public MoveDownResult onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        LineClearResult clearResult = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearResult = board.clearRows();

            if (clearResult.getLinesRemoved() > 0) {
                board.getScore().add(clearResult.getScoreBonus());
                audioManager.playSound("clear");
            }

            boolean collision = board.createNewBrick();

            if (collision) {
                if (gameMode != GameMode.ZEN) {
                    audioManager.playSound("gameover");
                    viewController.gameOver();
                } else {
                    board.clearBoard();
                }
            }

            viewController.refreshGameBackground(board.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(GameConstants.SOFT_DROP_POINTS);
                audioManager.playSound("drop");
            }
        }

        return new MoveDownResult(clearResult, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (board.rotateLeftBrick()) {
            audioManager.playSound("rotate");
        }
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        audioManager.stopBackground();
        audioManager.playBackground(gameMode);

        board.newGame();
        ViewData vd = board.getViewData();
        viewController.refreshGameBackground(board.getBoardMatrix());
        viewController.refreshBrick(vd);
        viewController.updateNextBrick(vd);
        viewController.updateHeldBrick(vd);
    }

    public ViewData getCurrentViewData() {
        return board.getViewData();
    }

    public int[][] getCurrentBoardMatrix() {
        return board.getBoardMatrix();
    }

    // New method to get Score object
    public Score getScore() {
        return board.getScore();
    }
}