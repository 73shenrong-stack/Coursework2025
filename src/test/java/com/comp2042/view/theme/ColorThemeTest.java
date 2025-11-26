package com.comp2042.view.theme;

import com.comp2042.model.game.GameMode;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorThemeTest {

    @Test
    void testArcade_NotNull() {
        ColorTheme theme = ColorTheme.arcade();
        assertNotNull(theme);
    }

    @Test
    void testArcade_Name() {
        ColorTheme theme = ColorTheme.arcade();
        assertEquals("Arcade", theme.getName());
    }

    @Test
    void testArcade_CssFile() {
        ColorTheme theme = ColorTheme.arcade();
        assertEquals("arcade_style.css", theme.getCssFile());
    }

    @Test
    void testZen_NotNull() {
        ColorTheme theme = ColorTheme.zen();
        assertNotNull(theme);
    }

    @Test
    void testZen_Name() {
        ColorTheme theme = ColorTheme.zen();
        assertEquals("Zen", theme.getName());
    }

    @Test
    void testZen_CssFile() {
        ColorTheme theme = ColorTheme.zen();
        assertEquals("zen_style.css", theme.getCssFile());
    }

    @Test
    void testForMode_Blitz() {
        ColorTheme theme = ColorTheme.forMode(GameMode.BLITZ);
        assertEquals("Arcade", theme.getName());
    }

    @Test
    void testForMode_FortyLines() {
        ColorTheme theme = ColorTheme.forMode(GameMode.FORTY_LINES);
        assertEquals("Arcade", theme.getName());
    }

    @Test
    void testForMode_Zen() {
        ColorTheme theme = ColorTheme.forMode(GameMode.ZEN);
        assertEquals("Zen", theme.getName());
    }

    @Test
    void testGetColor_ValidIndex() {
        ColorTheme theme = ColorTheme.arcade();
        Color color = theme.getColor(1);
        assertNotNull(color);
    }

    @Test
    void testGetColor_Zero_IsTransparent() {
        ColorTheme theme = ColorTheme.arcade();
        Color color = theme.getColor(0);
        assertEquals(Color.TRANSPARENT, color);
    }

    @Test
    void testGetColor_InvalidIndex_ReturnsFallback() {
        ColorTheme theme = ColorTheme.arcade();
        Color color = theme.getColor(999);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void testGetColor_NegativeIndex_ReturnsFallback() {
        ColorTheme theme = ColorTheme.arcade();
        Color color = theme.getColor(-1);
        assertEquals(Color.WHITE, color);
    }

    @Test
    void testIsEmpty_Zero() {
        ColorTheme theme = ColorTheme.arcade();
        assertTrue(theme.isEmpty(0));
    }

    @Test
    void testIsEmpty_NonZero() {
        ColorTheme theme = ColorTheme.arcade();
        assertFalse(theme.isEmpty(1));
        assertFalse(theme.isEmpty(2));
    }

    @Test
    void testArcade_HasAllColors() {
        ColorTheme theme = ColorTheme.arcade();

        // Test indices 0-7
        for (int i = 0; i <= 7; i++) {
            assertNotNull(theme.getColor(i));
        }
    }

    @Test
    void testZen_HasAllColors() {
        ColorTheme theme = ColorTheme.zen();

        // Test indices 0-7
        for (int i = 0; i <= 7; i++) {
            assertNotNull(theme.getColor(i));
        }
    }

    @Test
    void testArcade_Zen_DifferentColors() {
        ColorTheme arcade = ColorTheme.arcade();
        ColorTheme zen = ColorTheme.zen();

        // At least one color should be different (besides transparent)
        boolean foundDifference = false;
        for (int i = 1; i <= 7; i++) {
            if (!arcade.getColor(i).equals(zen.getColor(i))) {
                foundDifference = true;
                break;
            }
        }

        assertTrue(foundDifference, "Arcade and Zen themes should have different colors");
    }

    @Test
    void testGetName_NotEmpty() {
        ColorTheme arcade = ColorTheme.arcade();
        ColorTheme zen = ColorTheme.zen();

        assertFalse(arcade.getName().isEmpty());
        assertFalse(zen.getName().isEmpty());
    }

    @Test
    void testGetCssFile_NotEmpty() {
        ColorTheme arcade = ColorTheme.arcade();
        ColorTheme zen = ColorTheme.zen();

        assertFalse(arcade.getCssFile().isEmpty());
        assertFalse(zen.getCssFile().isEmpty());
    }
}