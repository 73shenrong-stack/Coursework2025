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
 * Main game controller that coordinates between the game model and view.
 * Handles all game logic including piece movement, rotation, collision detection, line clearing, scoring, and audio feedback. Acts as the central hub connecting user input to game state changes and view updates.
 */

public class GameController implements InputEventListener {

    private Board board = new TetrisBoard(GameConstants.BOARD_HEIGHT, GameConstants.BOARD_WIDTH);
    private final GameViewController viewController;
    private final GameMode gameMode;
    private final AudioManager audioManager = AudioManager.getInstance();

    /**
     * Constructs a new game controller and initializes the game.
     * Sets up the board, binds score to view, starts background music and spawns the first brick.
     *
     * @param controller the view controller for rendering
     * @param mode the game mode to play (Blitz, 40 Lines, or Zen)
     */
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

    /**
     * Handles the hold brick action.
     * Swaps the current brick with the held brick (or stores current brick if none held).
     * Can only be used once per brick placement.
     * Updates all relevant view components after the hold operation.
     */
    public void onHoldEvent() {
        if (board.holdBrick()) {
            ViewData vd = board.getViewData();
            viewController.refreshBrick(vd);
            viewController.updateNextBrick(vd);
            viewController.updateHeldBrick(vd);
            viewController.refreshGameBackground(board.getBoardMatrix());
        }
    }

    /**
     * Processes a downward movement event for the falling brick.
     * If the brick can move down, it moves and soft drop points are awarded if user-initiated.
     * If the brick cannot move down (landed), it is merged into the board, lines are cleared,
     * and a new brick is spawned. In non-Zen modes, collision at spawn triggers game over.
     * In Zen mode, the board is cleared instead of ending the game.
     * @param event the move event containing event type and source (USER or THREAD)
     * @return MoveDownResult containing line clear information and updated view data
     */
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

    /**
     * Processes a leftward movement event for the falling brick.
     * Attempts to move the brick one column to the left if not blocked.
     *
     * @param event the move event
     * @return updated view data after attempting the move
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Processes a rightward movement event for the falling brick.
     * Attempts to move the brick one column to the right if not blocked.
     *
     * @param event the move event
     * @return updated view data after attempting the move
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Processes a rotation event for the falling brick.
     * Uses the Super Rotation System (SRS) with wall kicks to attempt rotation.
     * Plays rotation sound effect if rotation is successful.
     *
     * @param event the move event
     * @return updated view data after attempting the rotation
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        if (board.rotateLeftBrick()) {
            audioManager.playSound("rotate");
        }
        return board.getViewData();
    }

    /**
     * Resets the game to initial state.
     * Stops current background music, starts fresh music for the mode,
     * resets the board, and updates all view components to reflect the new game state.
     */
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

    /**
     * Gets the current view data for rendering.
     * Includes current brick position, next brick preview, shadow position, and held brick.
     *
     * @return ViewData object containing all rendering information
     */
    public ViewData getCurrentViewData() {
        return board.getViewData();
    }

    /**
     * Gets the current state of the game board matrix.
     * Each cell contains an integer representing the brick color (0 for empty).
     *
     * @return 2D array representing the board state
     */
    public int[][] getCurrentBoardMatrix() {
        return board.getBoardMatrix();
    }

    /**
     * Gets the score tracker for the game.
     * Used by lifecycle manager to access score for record updates.
     *
     * @return Score object tracking the player's current score
     */
    public Score getScore() {
        return board.getScore();
    }
}