package com.comp2042;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    private final GameMode gameMode;

    private final AudioManager audioManager = AudioManager.getInstance();

    public GameController(GuiController c, GameMode mode) {
        this.viewGuiController = c;
        this.gameMode = mode;

        audioManager.playBackground(gameMode);

        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData(), gameMode);
        viewGuiController.updateHeldBrick(board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    public void onHoldEvent() {
                if (board.holdBrick()) {
            ViewData vd = board.getViewData();
            viewGuiController.refreshBrick(vd);
            viewGuiController.updateNextBrick(vd);
            viewGuiController.updateHeldBrick(vd);
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                audioManager.playSound("clear");
            }
            boolean newBrickCollided = board.createNewBrick();
            if (newBrickCollided) {
                if (gameMode != GameMode.ZEN) {
                    audioManager.playSound("gameover");
                    viewGuiController.gameOver();
                } else {
                    board.clearBoard();
                }
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
                audioManager.playSound("drop");
            }
        }
        return new DownData(clearRow, board.getViewData());
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

        // added this to prevent when start a new game, the panel still not refreshed
        board.newGame();
        ViewData vd = board.getViewData();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(vd);
        viewGuiController.updateNextBrick(vd);
        viewGuiController.updateHeldBrick(vd);
    }

    public ViewData getCurrentViewData() {
        return board.getViewData();
    }

    public int[][] getCurrentBoardMatrix() {
        return board.getBoardMatrix();
    }
}
