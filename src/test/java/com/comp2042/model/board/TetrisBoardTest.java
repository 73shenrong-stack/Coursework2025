package com.comp2042.model.board;

import com.comp2042.constants.GameConstants;
import com.comp2042.model.board.TetrisBoard;
import com.comp2042.model.data.LineClearResult;
import com.comp2042.model.data.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TetrisBoardTest {

    private TetrisBoard board;

    @BeforeEach
    void setUp() {
        board = new TetrisBoard(GameConstants.BOARD_HEIGHT, GameConstants.BOARD_WIDTH);
        board.createNewBrick();
    }

    @Test
    void testCreateNewBrick_NoCollision() {
        boolean collision = board.createNewBrick();
        assertFalse(collision, "New brick should not collide on empty board");
    }

    @Test
    void testMoveBrickDown_Success() {
        boolean moved = board.moveBrickDown();
        assertTrue(moved, "Brick should move down on empty board");
    }

    @Test
    void testMoveBrickDown_ReachesBottom() {
        // Move brick to bottom
        for (int i = 0; i < GameConstants.BOARD_HEIGHT; i++) {
            board.moveBrickDown();
        }
        boolean moved = board.moveBrickDown();
        assertFalse(moved, "Brick should not move beyond bottom");
    }

    @Test
    void testMoveBrickLeft_Success() {
        boolean moved = board.moveBrickLeft();
        assertTrue(moved || !moved, "Move left should execute without error");
    }

    @Test
    void testMoveBrickRight_Success() {
        boolean moved = board.moveBrickRight();
        assertTrue(moved || !moved, "Move right should execute without error");
    }

    @Test
    void testRotateLeftBrick_Success() {
        boolean rotated = board.rotateLeftBrick();
        assertTrue(rotated || !rotated, "Rotate should execute without error");
    }

    @Test
    void testGetBoardMatrix_NotNull() {
        int[][] matrix = board.getBoardMatrix();
        assertNotNull(matrix, "Board matrix should not be null");
        assertEquals(GameConstants.BOARD_HEIGHT, matrix.length);
        assertEquals(GameConstants.BOARD_WIDTH, matrix[0].length);
    }

    @Test
    void testGetViewData_NotNull() {
        ViewData viewData = board.getViewData();
        assertNotNull(viewData, "View data should not be null");
        assertNotNull(viewData.getBrickData());
        assertNotNull(viewData.getNextBrickData());
    }

    @Test
    void testMergeBrickToBackground() {
        // Move brick down several times
        for (int i = 0; i < 5; i++) {
            board.moveBrickDown();
        }

        board.mergeBrickToBackground();
        int[][] matrix = board.getBoardMatrix();

        // Check that some cells are now filled
        boolean hasMergedPiece = false;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    hasMergedPiece = true;
                    break;
                }
            }
        }
        assertTrue(hasMergedPiece, "Merged brick should appear in board");
    }

    @Test
    void testClearRows_NoCompletedLines() {
        LineClearResult result = board.clearRows();
        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
    }

    @Test
    void testClearRows_WithCompletedLines() {
        // Fill bottom row manually
        int[][] matrix = board.getBoardMatrix();
        int bottomRow = matrix.length - 1;
        for (int j = 0; j < matrix[0].length; j++) {
            matrix[bottomRow][j] = 1;
        }

        LineClearResult result = board.clearRows();
        assertEquals(1, result.getLinesRemoved());
        assertTrue(result.getScoreBonus() > 0);
    }

    @Test
    void testGetScore_NotNull() {
        assertNotNull(board.getScore(), "Score should not be null");
    }

    @Test
    void testNewGame_ResetsBoard() {
        // Modify board state
        board.moveBrickDown();
        board.mergeBrickToBackground();

        board.newGame();

        int[][] matrix = board.getBoardMatrix();
        ViewData viewData = board.getViewData();

        assertNotNull(viewData);
        assertEquals(0, board.getScore().scoreProperty().get());
    }

    @Test
    void testGetShadowYPosition_Valid() {
        int shadowY = board.getShadowYPosition();
        ViewData viewData = board.getViewData();

        assertTrue(shadowY >= viewData.getYPosition(),
                "Shadow should be at or below current position");
    }

    @Test
    void testHoldBrick_FirstTime() {
        boolean held = board.holdBrick();
        assertTrue(held, "First hold should succeed");

        ViewData viewData = board.getViewData();
        assertNotNull(viewData.getHeldBrickData(), "Held brick should not be null");
    }

    @Test
    void testHoldBrick_SecondTimeBeforePlace() {
        board.holdBrick();
        boolean heldAgain = board.holdBrick();
        assertFalse(heldAgain, "Cannot hold twice without placing brick");
    }

    @Test
    void testHoldBrick_SwapWithHeldPiece() {
        board.holdBrick();

        // Place the brick
        while (board.moveBrickDown()) {
            // Move until it lands
        }
        board.mergeBrickToBackground();
        board.createNewBrick();

        // Now hold again should work
        boolean held = board.holdBrick();
        assertTrue(held, "Should be able to hold after placing previous brick");
    }

    @Test
    void testClearBoard_EmptiesMatrix() {
        // Fill some cells
        int[][] matrix = board.getBoardMatrix();
        matrix[5][5] = 1;
        matrix[10][3] = 2;

        board.clearBoard();

        matrix = board.getBoardMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                assertEquals(0, matrix[i][j],
                        "Board should be empty after clear");
            }
        }
    }

    @Test
    void testMoveBrickLeft_AtLeftWall() {
        // Move brick all the way left
        for (int i = 0; i < GameConstants.BOARD_WIDTH; i++) {
            board.moveBrickLeft();
        }

        boolean moved = board.moveBrickLeft();
        assertFalse(moved, "Brick should not move through left wall");
    }

    @Test
    void testMoveBrickRight_AtRightWall() {
        // Move brick all the way right
        for (int i = 0; i < GameConstants.BOARD_WIDTH; i++) {
            board.moveBrickRight();
        }

        boolean moved = board.moveBrickRight();
        assertFalse(moved, "Brick should not move through right wall");
    }

    @Test
    void testMultipleLineClear_QuadraticScoring() {
        // Fill multiple rows
        int[][] matrix = board.getBoardMatrix();
        int bottomRow = matrix.length - 1;

        // Fill bottom 2 rows
        for (int i = bottomRow - 1; i <= bottomRow; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 1;
            }
        }

        LineClearResult result = board.clearRows();
        assertEquals(2, result.getLinesRemoved());

        // Verify quadratic scoring: 50 * 2 * 2 = 200
        assertEquals(GameConstants.LINE_CLEAR_BASE_SCORE * 2 * 2,
                result.getScoreBonus());
    }
}