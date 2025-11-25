package comp2042.model.board;

import comp2042.model.data.LineClearResult;
import comp2042.model.data.ViewData;
import comp2042.model.game.Score;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    LineClearResult clearRows();

    Score getScore();

    void newGame();

    int getShadowYPosition();

    boolean holdBrick();

    void clearBoard();
}
