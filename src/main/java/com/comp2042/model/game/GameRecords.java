package com.comp2042.model.game;

import java.io.Serializable;

// Manages game records (high scores and best times) for different game modes

public class GameRecords implements Serializable {
    private static final long serialVersionUID = 1L;

    // High scores for each mode
    private int blitzHighScore = 0;
    private int fortyLinesHighScore = 0;
    private int zenHighScore = 0;

    // Best times (in seconds) for each mode
    private int blitzBestTime = 0; // Time when high score was achieved (always 120 for Blitz)
    private int fortyLinesBestTime = Integer.MAX_VALUE; // Shortest completion time
    private int zenBestTime = 0; // Time when high score was achieved

    public GameRecords() {
        // Default constructor
    }


    // Update Blitz mode record if new score is higher

    public boolean updateBlitzRecord(int score, int timeInSeconds) {
        if (score > blitzHighScore) {
            blitzHighScore = score;
            blitzBestTime = 120; // Always 2 minutes for Blitz
            return true;
        }
        return false;
    }


    // Update 40 Lines mode record if new time is faster

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


    // Update Zen mode record if new score is higher

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


    // Get high score for a specific game mode

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


    // Get best time for a specific game mode

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


    // Format time in MM:SS format

    public static String formatTime(int seconds) {
        if (seconds == Integer.MAX_VALUE) {
            return "--:--";
        }
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d : %02d", minutes, secs);
    }
}