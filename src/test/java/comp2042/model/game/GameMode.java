package comp2042.model.game;

public enum GameMode {
    BLITZ("Blitz Mode", "Score as high as possible in 2 minutes!"), FORTY_LINES("40 Lines", "Clear 40 lines as fast as you can!"), ZEN("Zen Mode", "Relax and play without pressure");

    private final String displayName;
    private final String description;

    GameMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
