package com.comp2042.model.board;

import com.comp2042.model.brick.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SRSKickDataTest {

    @Test
    void testGetKicks_OBrick_ReturnsNoKick() {
        Brick oBrick = new OBrick();
        int[][] kicks = SRSKickData.getKicks(oBrick, 0, 1, false);

        assertEquals(1, kicks.length);
        assertArrayEquals(new int[]{0, 0}, kicks[0]);
    }

    @Test
    void testGetKicks_IBrick_ReturnsMultipleKicks() {
        Brick iBrick = new IBrick();
        int[][] kicks = SRSKickData.getKicks(iBrick, 0, 1, false);

        assertTrue(kicks.length > 1, "I-brick should have multiple kick options");
    }

    @Test
    void testGetKicks_JBrick_ReturnsKickTable() {
        Brick jBrick = new JBrick();
        int[][] kicks = SRSKickData.getKicks(jBrick, 0, 3, false);

        assertNotNull(kicks);
        assertTrue(kicks.length > 0);
    }

    @Test
    void testGetKicks_LBrick_ReturnsKickTable() {
        Brick lBrick = new LBrick();
        int[][] kicks = SRSKickData.getKicks(lBrick, 1, 0, false);

        assertNotNull(kicks);
        assertTrue(kicks.length > 0);
    }

    @Test
    void testGetKicks_SBrick_ReturnsKickTable() {
        Brick sBrick = new SBrick();
        int[][] kicks = SRSKickData.getKicks(sBrick, 0, 1, false);

        assertNotNull(kicks);
        assertTrue(kicks.length > 0);
    }

    @Test
    void testGetKicks_TBrick_ReturnsKickTable() {
        Brick tBrick = new TBrick();
        int[][] kicks = SRSKickData.getKicks(tBrick, 2, 1, false);

        assertNotNull(kicks);
        assertTrue(kicks.length > 0);
    }

    @Test
    void testGetKicks_ZBrick_ReturnsKickTable() {
        Brick zBrick = new ZBrick();
        int[][] kicks = SRSKickData.getKicks(zBrick, 0, 1, false);

        assertNotNull(kicks);
        assertTrue(kicks.length > 0);
    }

    @Test
    void testGetKicks_IBrick_State0() {
        Brick iBrick = new IBrick();
        int[][] kicks = SRSKickData.getKicks(iBrick, 0, 1, false);

        // First kick should always be (0,0)
        assertArrayEquals(new int[]{0, 0}, kicks[0]);
    }

    @Test
    void testGetKicks_IBrick_State1() {
        Brick iBrick = new IBrick();
        int[][] kicks = SRSKickData.getKicks(iBrick, 1, 0, false);

        assertNotNull(kicks);
        assertEquals(5, kicks.length, "I-brick should have 5 kick attempts");
    }

    @Test
    void testGetKicks_JLSTZ_FirstKickIsNoOffset() {
        Brick jBrick = new JBrick();
        int[][] kicks = SRSKickData.getKicks(jBrick, 0, 3, false);

        assertArrayEquals(new int[]{0, 0}, kicks[0],
                "First kick should always be no offset");
    }

    @Test
    void testGetKicks_AllStates_ValidArrays() {
        Brick tBrick = new TBrick();

        for (int from = 0; from < 4; from++) {
            int to = (from - 1 + 4) % 4;
            int[][] kicks = SRSKickData.getKicks(tBrick, from, to, false);

            assertNotNull(kicks);
            for (int[] kick : kicks) {
                assertEquals(2, kick.length, "Each kick should have x and y offset");
            }
        }
    }

    @Test
    void testGetKicks_ClockwiseRotation_ReturnsFallback() {
        Brick jBrick = new JBrick();
        int[][] kicks = SRSKickData.getKicks(jBrick, 0, 1, true);

        // Clockwise not implemented, should return fallback
        assertEquals(1, kicks.length);
        assertArrayEquals(new int[]{0, 0}, kicks[0]);
    }
}