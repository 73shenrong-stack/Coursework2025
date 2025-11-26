package com.comp2042.constants;

import com.comp2042.constants.GameConstants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class GameConstantsTest {

    @Test
    void testConstructor_ThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<?> constructor = GameConstants.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // Check if the cause of the exception is an AssertionError
        assertTrue(exception instanceof InvocationTargetException);
        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof AssertionError);
        assertEquals("Cannot instantiate this class", cause.getMessage());
    }

    @Test
    void testBoardHeight_Positive() {
        assertTrue(GameConstants.BOARD_HEIGHT > 0);
    }

    @Test
    void testBoardWidth_Positive() {
        assertTrue(GameConstants.BOARD_WIDTH > 0);
    }

    @Test
    void testVisibleRowStart_Valid() {
        assertTrue(GameConstants.VISIBLE_ROW_START >= 0);
        assertTrue(GameConstants.VISIBLE_ROW_START < GameConstants.BOARD_HEIGHT);
    }

    @Test
    void testBrickSize_Positive() {
        assertTrue(GameConstants.BRICK_SIZE > 0);
    }

    @Test
    void testArcSize_Positive() {
        assertTrue(GameConstants.ARC_SIZE >= 0);
    }

    @Test
    void testPreviewPanelSize_Positive() {
        assertTrue(GameConstants.PREVIEW_PANEL_SIZE > 0);
    }

    @Test
    void testBlitzModeSpeed_Positive() {
        assertTrue(GameConstants.BLITZ_MODE_SPEED > 0);
    }

    @Test
    void testNormalModeSpeed_Positive() {
        assertTrue(GameConstants.NORMAL_MODE_SPEED > 0);
    }

    @Test
    void testZenModeSpeed_Positive() {
        assertTrue(GameConstants.ZEN_MODE_SPEED > 0);
    }

    @Test
    void testSoftDropPoints_Valid() {
        assertTrue(GameConstants.SOFT_DROP_POINTS >= 0);
    }

    @Test
    void testLineClearBaseScore_Positive() {
        assertTrue(GameConstants.LINE_CLEAR_BASE_SCORE > 0);
    }

    @Test
    void testShadowOpacity_InRange() {
        assertTrue(GameConstants.SHADOW_OPACITY >= 0.0);
        assertTrue(GameConstants.SHADOW_OPACITY <= 1.0);
    }

    @Test
    void testSpawnX_InBounds() {
        assertTrue(GameConstants.SPAWN_X >= 0);
        assertTrue(GameConstants.SPAWN_X < GameConstants.BOARD_WIDTH);
    }

    @Test
    void testSpawnY_InBounds() {
        assertTrue(GameConstants.SPAWN_Y >= 0);
        assertTrue(GameConstants.SPAWN_Y < GameConstants.BOARD_HEIGHT);
    }

    @Test
    void testBoardDimensions_Reasonable() {
        assertEquals(25, GameConstants.BOARD_HEIGHT);
        assertEquals(10, GameConstants.BOARD_WIDTH);
    }

    @Test
    void testSpeedOrdering_BlitzFastest() {
        assertTrue(GameConstants.BLITZ_MODE_SPEED < GameConstants.NORMAL_MODE_SPEED);
        assertTrue(GameConstants.BLITZ_MODE_SPEED < GameConstants.ZEN_MODE_SPEED);
    }

    @Test
    void testSpeedOrdering_ZenSlowest() {
        assertTrue(GameConstants.ZEN_MODE_SPEED > GameConstants.NORMAL_MODE_SPEED);
        assertTrue(GameConstants.ZEN_MODE_SPEED > GameConstants.BLITZ_MODE_SPEED);
    }

    @Test
    void testPreviewPanelSize_MatchesBrickSize() {
        assertEquals(4, GameConstants.PREVIEW_PANEL_SIZE);
    }

    @Test
    void testScoring_Positive() {
        assertTrue(GameConstants.SOFT_DROP_POINTS > 0);
        assertTrue(GameConstants.LINE_CLEAR_BASE_SCORE > 0);
    }
}