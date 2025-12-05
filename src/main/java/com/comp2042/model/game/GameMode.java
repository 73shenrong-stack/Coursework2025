package com.comp2042.model.game;

/**
 * Enumeration of available game modes.
 * Each mode has different objectives, time constraints, and gameplay characteristics.
 */
public enum GameMode {
    BLITZ("Blitz Mode", "Score as high as possible in 2 minutes!"), FORTY_LINES("40 Lines", "Clear 40 lines as fast as you can!"), ZEN("Zen Mode", "Relax and play without pressure");

    private final String displayName;
    private final String description;

    /**
     * Constructs a game mode with display information.
     *
     * @param displayName the user-facing name of the mode
     * @param description a brief description of the mode's objective
     */
    GameMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the display name for this game mode.
     *
     * @return the user-friendly name (e.g., "Blitz Mode")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description text for this game mode.
     *
     * @return a brief description of the mode's objective
     */
    public String getDescription() {
        return description;
    }
}
