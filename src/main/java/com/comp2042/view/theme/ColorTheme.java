package com.comp2042.view.theme;

import com.comp2042.model.game.GameMode;
import javafx.scene.paint.Color;

/**
 * Manages color themes for different game modes
 */
public class ColorTheme {

    private final Color[] colors;
    private final String name;

    // Arcade theme - bright neon colors
    private static final Color[] ARCADE_COLORS = {
            Color.TRANSPARENT,
            Color.web("#FF00FF"),
            Color.web("#FF2E97"),
            Color.web("#39FF14"),
            Color.web("#FFEA00"),
            Color.web("#FF3131"),
            Color.web("#00D9FF"),
            Color.web("#FF8C00")
    };

    // Zen theme - soft pastel colors
    private static final Color[] ZEN_COLORS = {
            Color.TRANSPARENT,
            Color.web("#FFB997"),
            Color.web("#F49F9F"),
            Color.web("#F6D8AE"),
            Color.web("#FFD580"),
            Color.web("#D3BBDD"),
            Color.web("#A0C4E2"),
            Color.web("#E5C3A6")
    };

    private ColorTheme(Color[] colors, String name) {
        this.colors = colors;
        this.name = name;
    }

    // Get arcade theme (bright, neon colors)

    public static ColorTheme arcade() {
        return new ColorTheme(ARCADE_COLORS, "Arcade");
    }

    // Get zen theme (soft, pastel colors)

    public static ColorTheme zen() {
        return new ColorTheme(ZEN_COLORS, "Zen");
    }

    // Get color theme based on game mode

    public static ColorTheme forMode(GameMode mode) {
        if (mode == GameMode.ZEN) {
            return zen();
        } else {
            return arcade();
        }
    }

    // Get color for a specific brick type

    public Color getColor(int colorIndex) {
        if (colorIndex < 0 || colorIndex >= colors.length) {
            return Color.WHITE; // Fallback color
        }
        return colors[colorIndex];
    }

    // Check if a color index represents an empty cell

    public boolean isEmpty(int colorIndex) {
        return colorIndex == 0;
    }

    // Get the theme name

    public String getName() {
        return name;
    }

    // Get CSS file path for this theme

    public String getCssFile() {
        if (name.equals("Zen")) {
            return "zen_style.css";
        } else {
            return "arcade_style.css";
        }
    }
}