package com.comp2042;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    private final GameMode gameMode;

    public GameController(GuiController c, GameMode mode) {
        this.viewGuiController = c;
        this.gameMode = mode;

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
            }
            boolean gameOver = board.createNewBrick();
            if (gameOver) {
                if (gameMode == GameMode.ZEN) {
                    board.clearBoard();
                } else {
                    viewGuiController.gameOver();
                }
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
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
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    public ViewData getCurrentViewData() {
        return board.getViewData();
    }

    public int[][] getCurrentBoardMatrix() {
        return board.getBoardMatrix();
    }
}
