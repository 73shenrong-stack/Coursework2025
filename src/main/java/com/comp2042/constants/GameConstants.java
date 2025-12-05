package com.comp2042.constants;

/**
 * Central repository for all game-wide constants.
 * Defines board dimensions, rendering parameters, game speeds, scoring values and spawn configuration.
 * Using constants ensures consistency across the codebase and makes tuning game parameters easier.
 */

public final class GameConstants {

    /** Board dimensions
     *
     */
    public static final int BOARD_HEIGHT = 25;
    public static final int BOARD_WIDTH = 10;
    public static final int VISIBLE_ROW_START = 2; // Rows 0-1 are hidden spawn area

    /** Rendering constants
     *
     */
    public static final int BRICK_SIZE = 20;
    public static final int ARC_SIZE = 6;
    public static final int PREVIEW_PANEL_SIZE = 4;

    /** Game timing (milliseconds)
     *
     */
    public static final int BLITZ_MODE_SPEED = 300;
    public static final int NORMAL_MODE_SPEED = 400;
    public static final int ZEN_MODE_SPEED = 600;

    /** Scoring
     *
     */
    public static final int SOFT_DROP_POINTS = 1;
    public static final int LINE_CLEAR_BASE_SCORE = 50;

    public static final double SHADOW_OPACITY = 0.6;

    /** Spawn position
     *
     */
    public static final int SPAWN_X = 3;
    public static final int SPAWN_Y = 0;

    /**
     * Private constructor to prevent instantiation.
     * This class should only be used for its static constants.
     *
     * @throws AssertionError if instantiation is attempted
     */

    private GameConstants() {
        throw new AssertionError("Cannot instantiate this class");
    }
}