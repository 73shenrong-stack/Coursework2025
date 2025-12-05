package com.comp2042.view.theme;

import com.comp2042.model.game.GameMode;
import javafx.scene.paint.Color;

/**
 * Manages color themes for different game modes
 * Provides two distinct visual themes:
 * Arcade - vibrant, high-contrast neon colors
 * Zen - soft, calming pastel colors
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

    /**
     * Private constructor - instances should be created via factory methods.
     *
     * @param colors array of colors for indices 0–7
     * @param name   name of the theme
     */
    private ColorTheme(Color[] colors, String name) {
        this.colors = colors;
        this.name = name;
    }

    /**
     * Returns the Arcade (neon) color theme.
     *
     * @return a ColorTheme instance with bright, vibrant colors
     */
    public static ColorTheme arcade() {
        return new ColorTheme(ARCADE_COLORS, "Arcade");
    }

    /**
     * Returns the Zen (pastel) color theme.
     *
     * @return a ColorTheme instance with soft, calming colors
     */
    public static ColorTheme zen() {
        return new ColorTheme(ZEN_COLORS, "Zen");
    }

/**
 * Returns the appropriate color theme based on the current GameMode.
 *
 * @param mode the current game mode
 * @return the matching ColorTheme
 */
    public static ColorTheme forMode(GameMode mode) {
        if (mode == GameMode.ZEN) {
            return zen();
        } else {
            return arcade();
        }
    }

    /**
     * Returns the Color associated with the given tetromino type index.
     *
     * @param colorIndex numeric identifier of the brick (0–7)
     * @return the corresponding Color
     */
    public Color getColor(int colorIndex) {
        if (colorIndex < 0 || colorIndex >= colors.length) {
            return Color.WHITE; // Fallback color
        }
        return colors[colorIndex];
    }

    /**
     * Checks whether the given color index represents an empty cell.
     *
     * @param colorIndex the index to check
     * @return true if the index is 0 (empty), false otherwise
     */
    public boolean isEmpty(int colorIndex) {
        return colorIndex == 0;
    }

    /**
     * Returns the human-readable name of this theme.
     *
     * @return "Arcade" or "Zen"
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the CSS stylesheet filename associated with this theme.
     *
     * @return {@code "arcade_style.css"} or {@code "zen_style.css"}
     */
    public String getCssFile() {
        if (name.equals("Zen")) {
            return "zen_style.css";
        } else {
            return "arcade_style.css";
        }
    }
}