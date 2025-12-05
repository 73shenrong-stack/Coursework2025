package com.comp2042.model.game;

import java.io.Serializable;

/** Manages game records (high scores and best times) for different game modes
 * Tracks records for Blitz, 40 Lines, and Zen modes separately.
 * Records are serializable for persistent storage across game sessions.
 */

public class GameRecords implements Serializable {
    private static final long serialVersionUID = 1L;

    /** High scores for each mode
     *
     */
    private int blitzHighScore = 0;
    private int fortyLinesHighScore = 0;
    private int zenHighScore = 0;

    /** Best times (in seconds) for each mode
     *
     */
    private int blitzBestTime = 0; // Time when high score was achieved (always 120 for Blitz)
    private int fortyLinesBestTime = Integer.MAX_VALUE; // Shortest completion time
    private int zenBestTime = 0; // Time when high score was achieved

    public GameRecords() {
        // Default constructor
    }


    /**
     * Updates the Blitz mode record if the new score exceeds the current high score.
     * Time is always set to 120 seconds (2 minutes) for Blitz completions.
     *
     * @param score the score achieved
     * @param timeInSeconds the time in seconds (always 120 for Blitz)
     * @return true if a new record was set, false otherwise
     */

    public boolean updateBlitzRecord(int score, int timeInSeconds) {
        if (score > blitzHighScore) {
            blitzHighScore = score;
            blitzBestTime = 120; // Always 2 minutes for Blitz
            return true;
        }
        return false;
    }


    /**
     * Updates the 40 Lines mode record based on completion time and score.
     * A new record is set if:
     *
     * @param score the score achieved
     * @param timeInSeconds the completion time in seconds
     * @return true if a new record was set, false otherwise
     */

    public boolean updateFortyLinesRecord(int score, int timeInSeconds) {
        boolean isNewRecord = false;

        // New record if: faster time OR same time but higher score
        if (timeInSeconds < fortyLinesBestTime) {
            // Faster time is always a new record
            fortyLinesBestTime = timeInSeconds;
            fortyLinesHighScore = score;
            isNewRecord = true;
        } else if (timeInSeconds == fortyLinesBestTime && score > fortyLinesHighScore) {
            // Same time but higher score
            fortyLinesHighScore = score;
            isNewRecord = true;
        } else if (fortyLinesBestTime == Integer.MAX_VALUE) {
            // First completion ever
            fortyLinesBestTime = timeInSeconds;
            fortyLinesHighScore = score;
            isNewRecord = true;
        }

        return isNewRecord;
    }


    /**
     * Updates the Zen mode record if the new score exceeds the current high score.
     *
     * @param score the score achieved
     * @param timeInSeconds the session time when the score was achieved
     * @return true if a new record was set, false otherwise
     */

    public boolean updateZenRecord(int score, int timeInSeconds) {
        if (score > zenHighScore) {
            zenHighScore = score;
            zenBestTime = timeInSeconds;
            return true;
        }
        return false;
    }

    // Getters
    public int getBlitzHighScore() {
        return blitzHighScore;
    }

    public int getFortyLinesHighScore() {
        return fortyLinesHighScore;
    }

    public int getZenHighScore() {
        return zenHighScore;
    }

    public int getBlitzBestTime() {
        return blitzBestTime;
    }

    public int getFortyLinesBestTime() {
        return fortyLinesBestTime;
    }

    public int getZenBestTime() {
        return zenBestTime;
    }


    /**
     * Gets the high score for the specified game mode.
     *
     * @param mode the game mode
     * @return the high score for that mode, or 0 if invalid mode
     */

    public int getHighScore(GameMode mode) {
        switch (mode) {
            case BLITZ:
                return blitzHighScore;
            case FORTY_LINES:
                return fortyLinesHighScore;
            case ZEN:
                return zenHighScore;
            default:
                return 0;
        }
    }


    /**
     * Gets the best time for the specified game mode.
     *
     * @param mode the game mode
     * @return the best time in seconds, or 0 if invalid mode
     */

    public int getBestTime(GameMode mode) {
        switch (mode) {
            case BLITZ:
                return blitzBestTime;
            case FORTY_LINES:
                return fortyLinesBestTime;
            case ZEN:
                return zenBestTime;
            default:
                return 0;
        }
    }


    /**
     * Formats a time value in seconds to MM:SS format.
     *
     * @param seconds the time in seconds
     * @return formatted string "M : SS" or "--:--" if time is MAX_VALUE
     */

    public static String formatTime(int seconds) {
        if (seconds == Integer.MAX_VALUE) {
            return "--:--";
        }
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d : %02d", minutes, secs);
    }
}