package comp2042.controller;

import comp2042.audio.AudioManager;
import comp2042.constants.GameConstants;
import comp2042.model.board.Board;
import comp2042.model.board.TetrisBoard;
import comp2042.model.data.*;
import comp2042.model.game.GameMode;
import comp2042.view.GameViewController;

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

    // Handle hold brick action

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

    // Get current view data

    public ViewData getCurrentViewData() {
        return board.getViewData();
    }

    // Get current board matrix

    public int[][] getCurrentBoardMatrix() {
        return board.getBoardMatrix();
    }
}