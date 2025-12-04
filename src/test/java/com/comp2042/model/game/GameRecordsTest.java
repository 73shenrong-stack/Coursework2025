package com.comp2042.model.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class GameRecordsTest {

    private GameRecords records;

    @BeforeEach
    void setUp() {
        records = new GameRecords();
    }

    // ========== BLITZ MODE TESTS ==========

    @Test
    @DisplayName("Blitz: First completion should set new record")
    void testBlitzFirstCompletion() {
        boolean isNewRecord = records.updateBlitzRecord(1000, 120);

        assertTrue(isNewRecord, "First completion should be new record");
        assertEquals(1000, records.getBlitzHighScore());
        assertEquals(120, records.getBlitzBestTime());
    }

    @Test
    @DisplayName("Blitz: Higher score should update record")
    void testBlitzHigherScore() {
        records.updateBlitzRecord(1000, 120);
        boolean isNewRecord = records.updateBlitzRecord(1500, 120);

        assertTrue(isNewRecord, "Higher score should be new record");
        assertEquals(1500, records.getBlitzHighScore());
        assertEquals(120, records.getBlitzBestTime());
    }

    @Test
    @DisplayName("Blitz: Lower score should not update record")
    void testBlitzLowerScore() {
        records.updateBlitzRecord(1500, 120);
        boolean isNewRecord = records.updateBlitzRecord(1000, 120);

        assertFalse(isNewRecord, "Lower score should not be new record");
        assertEquals(1500, records.getBlitzHighScore(), "High score should remain unchanged");
    }

    @Test
    @DisplayName("Blitz: Time should always be 120 seconds")
    void testBlitzTimeAlways120() {
        records.updateBlitzRecord(1000, 120);
        records.updateBlitzRecord(2000, 120);

        assertEquals(120, records.getBlitzBestTime(), "Blitz time should always be 120 seconds");
    }

    @Test
    @DisplayName("Blitz: Multiple updates preserve highest score")
    void testBlitzMultipleUpdates() {
        records.updateBlitzRecord(500, 120);
        records.updateBlitzRecord(1500, 120);
        records.updateBlitzRecord(1000, 120);
        records.updateBlitzRecord(2000, 120);

        assertEquals(2000, records.getBlitzHighScore(), "Should keep highest score");
    }

    // ========== 40 LINES MODE TESTS ==========

    @Test
    @DisplayName("40 Lines: First completion should set new record")
    void testFortyLinesFirstCompletion() {
        boolean isNewRecord = records.updateFortyLinesRecord(1000, 60);

        assertTrue(isNewRecord, "First completion should be new record");
        assertEquals(1000, records.getFortyLinesHighScore());
        assertEquals(60, records.getFortyLinesBestTime());
    }

    @Test
    @DisplayName("40 Lines: Faster time should update record")
    void testFortyLinesFasterTime() {
        records.updateFortyLinesRecord(1000, 60);
        boolean isNewRecord = records.updateFortyLinesRecord(800, 50);

        assertTrue(isNewRecord, "Faster time should be new record");
        assertEquals(800, records.getFortyLinesHighScore());
        assertEquals(50, records.getFortyLinesBestTime());
    }

    @Test
    @DisplayName("40 Lines: Slower time should not update record")
    void testFortyLinesSlowerTime() {
        records.updateFortyLinesRecord(1000, 50);
        boolean isNewRecord = records.updateFortyLinesRecord(1500, 70);

        assertFalse(isNewRecord, "Slower time should not be new record");
        assertEquals(1000, records.getFortyLinesHighScore());
        assertEquals(50, records.getFortyLinesBestTime());
    }

    @Test
    @DisplayName("40 Lines: Higher score with same time should update")
    void testFortyLinesHigherScoreSameTime() {
        records.updateFortyLinesRecord(1000, 60);
        boolean isNewRecord = records.updateFortyLinesRecord(1500, 60);

        assertTrue(isNewRecord, "Higher score with same time should be new record");
        assertEquals(1500, records.getFortyLinesHighScore());
        assertEquals(60, records.getFortyLinesBestTime());
    }

    @Test
    @DisplayName("40 Lines: Lower score with same time should not update")
    void testFortyLinesLowerScoreSameTime() {
        records.updateFortyLinesRecord(1500, 60);
        boolean isNewRecord = records.updateFortyLinesRecord(1000, 60);

        assertFalse(isNewRecord, "Lower score with same time should not be new record");
        assertEquals(1500, records.getFortyLinesHighScore());
    }

    @Test
    @DisplayName("40 Lines: Best time starts at MAX_VALUE")
    void testFortyLinesInitialBestTime() {
        assertEquals(Integer.MAX_VALUE, records.getFortyLinesBestTime(),
                "Initial best time should be MAX_VALUE");
    }

    @Test
    @DisplayName("40 Lines: Multiple completions preserve best time")
    void testFortyLinesMultipleCompletions() {
        records.updateFortyLinesRecord(800, 70);
        records.updateFortyLinesRecord(1000, 50);
        records.updateFortyLinesRecord(1200, 60);

        assertEquals(50, records.getFortyLinesBestTime(), "Should keep fastest time");
    }

    // ========== ZEN MODE TESTS ==========

    @Test
    @DisplayName("Zen: Should update high score")
    void testZenHighScore() {
        boolean isNewRecord = records.updateZenRecord(5000, 300);

        assertTrue(isNewRecord);
        assertEquals(5000, records.getZenHighScore());
        assertEquals(300, records.getZenBestTime());
    }

    @Test
    @DisplayName("Zen: Lower score should not update")
    void testZenLowerScore() {
        records.updateZenRecord(5000, 300);
        boolean isNewRecord = records.updateZenRecord(4000, 400);

        assertFalse(isNewRecord);
        assertEquals(5000, records.getZenHighScore());
    }

    // ========== GET METHODS BY MODE ==========

    @Test
    @DisplayName("getHighScore should return correct score for each mode")
    void testGetHighScoreByMode() {
        records.updateBlitzRecord(1000, 120);
        records.updateFortyLinesRecord(2000, 60);
        records.updateZenRecord(3000, 300);

        assertEquals(1000, records.getHighScore(GameMode.BLITZ));
        assertEquals(2000, records.getHighScore(GameMode.FORTY_LINES));
        assertEquals(3000, records.getHighScore(GameMode.ZEN));
    }

    @Test
    @DisplayName("getBestTime should return correct time for each mode")
    void testGetBestTimeByMode() {
        records.updateBlitzRecord(1000, 120);
        records.updateFortyLinesRecord(2000, 60);
        records.updateZenRecord(3000, 300);

        assertEquals(120, records.getBestTime(GameMode.BLITZ));
        assertEquals(60, records.getBestTime(GameMode.FORTY_LINES));
        assertEquals(300, records.getBestTime(GameMode.ZEN));
    }

    // ========== FORMAT TIME TESTS ==========

    @Test
    @DisplayName("formatTime should format seconds correctly")
    void testFormatTime() {
        assertEquals("0 : 30", GameRecords.formatTime(30));
        assertEquals("1 : 00", GameRecords.formatTime(60));
        assertEquals("2 : 00", GameRecords.formatTime(120));
        assertEquals("5 : 45", GameRecords.formatTime(345));
    }

    @Test
    @DisplayName("formatTime should handle MAX_VALUE")
    void testFormatTimeMaxValue() {
        assertEquals("--:--", GameRecords.formatTime(Integer.MAX_VALUE));
    }

    @Test
    @DisplayName("formatTime should handle zero")
    void testFormatTimeZero() {
        assertEquals("0 : 00", GameRecords.formatTime(0));
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Initial state should have zero scores")
    void testInitialState() {
        assertEquals(0, records.getBlitzHighScore());
        assertEquals(0, records.getFortyLinesHighScore());
        assertEquals(0, records.getZenHighScore());
        assertEquals(0, records.getBlitzBestTime());
        assertEquals(Integer.MAX_VALUE, records.getFortyLinesBestTime());
        assertEquals(0, records.getZenBestTime());
    }

    @Test
    @DisplayName("Equal scores should not be new record")
    void testEqualScores() {
        records.updateBlitzRecord(1000, 120);
        boolean isNewRecord = records.updateBlitzRecord(1000, 120);

        assertFalse(isNewRecord, "Equal score should not be new record");
    }

    @Test
    @DisplayName("40 Lines: Much faster time with lower score should update")
    void testFortyLinesFasterTimeOverridesScore() {
        records.updateFortyLinesRecord(2000, 100);
        boolean isNewRecord = records.updateFortyLinesRecord(1500, 45);

        assertTrue(isNewRecord, "Faster time should override lower score");
        assertEquals(1500, records.getFortyLinesHighScore());
        assertEquals(45, records.getFortyLinesBestTime());
    }
}