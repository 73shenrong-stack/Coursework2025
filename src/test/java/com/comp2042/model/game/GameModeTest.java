package com.comp2042.model.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModeTest {

    @Test
    void testBlitzMode_DisplayName() {
        assertEquals("Blitz Mode", GameMode.BLITZ.getDisplayName());
    }

    @Test
    void testBlitzMode_Description() {
        assertEquals("Score as high as possible in 2 minutes!",
                GameMode.BLITZ.getDescription());
    }

    @Test
    void testFortyLinesMode_DisplayName() {
        assertEquals("40 Lines", GameMode.FORTY_LINES.getDisplayName());
    }

    @Test
    void testFortyLinesMode_Description() {
        assertEquals("Clear 40 lines as fast as you can!",
                GameMode.FORTY_LINES.getDescription());
    }

    @Test
    void testZenMode_DisplayName() {
        assertEquals("Zen Mode", GameMode.ZEN.getDisplayName());
    }

    @Test
    void testZenMode_Description() {
        assertEquals("Relax and play without pressure",
                GameMode.ZEN.getDescription());
    }

    @Test
    void testAllModes_HaveDisplayNames() {
        for (GameMode mode : GameMode.values()) {
            assertNotNull(mode.getDisplayName());
            assertFalse(mode.getDisplayName().isEmpty());
        }
    }

    @Test
    void testAllModes_HaveDescriptions() {
        for (GameMode mode : GameMode.values()) {
            assertNotNull(mode.getDescription());
            assertFalse(mode.getDescription().isEmpty());
        }
    }

    @Test
    void testGameMode_ValueOf() {
        assertEquals(GameMode.BLITZ, GameMode.valueOf("BLITZ"));
        assertEquals(GameMode.FORTY_LINES, GameMode.valueOf("FORTY_LINES"));
        assertEquals(GameMode.ZEN, GameMode.valueOf("ZEN"));
    }

    @Test
    void testGameMode_Values() {
        GameMode[] modes = GameMode.values();
        assertEquals(3, modes.length);
    }
}