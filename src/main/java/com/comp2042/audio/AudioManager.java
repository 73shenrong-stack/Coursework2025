package com.comp2042.audio;

import com.comp2042.model.game.GameMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager for all game audio including sound effects and background music.
 * Handles loading, playing, pausing, and stopping audio files for the game.
 * Provides mode-specific background music and sound effects for game events such as line clears, rotations, and piece drops.
 *
 * Audio files are loaded from the classpath resources and managed through JavaFX MediaPlayer instances. If audio files are missing, operations fail silently to prevent crashes during development.</p>
 *
 */
public class AudioManager {

    private static AudioManager instance;
    private final Map<String, MediaPlayer> players = new HashMap<>();
    private MediaPlayer bgPlayer;

    /**
     * Private constructor to enforce singleton pattern.
     * Loads all sound effects into memory during initialization.
     */
    private AudioManager() {
        // Safe loading – if file missing, just skip silently
        loadSound("clear", "/audio/clear.mp3");
        loadSound("rotate", "/audio/rotate.mp3");
        loadSound("drop", "/audio/drop.mp3");
        loadSound("gameover", "/audio/gameover.mp3");
        loadSound("victory", "/audio/victory.mp3");
    }

    /**
     * Gets the singleton instance of AudioManager.
     * Creates the instance on first call (lazy initialization).
     *
     * @return the singleton AudioManager instance
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Loads a sound effect from the classpath and stores it in the players map.
     * If the file is not found or cannot be loaded, the operation fails silently with a console message.
     *
     * @param key the identifier for this sound effect
     * @param path the classpath resource path to the audio file
     */
    private void loadSound(String key, String path) {
        try {
            String url = getClass().getResource(path) != null ?
                    getClass().getResource(path).toExternalForm() : null;
            if (url != null) {
                Media sound = new Media(url);
                MediaPlayer player = new MediaPlayer(sound);
                player.setVolume(0.6);
                players.put(key, player);
            }
        } catch (Exception e) {
            System.out.println("Audio file not found (OK in dev): " + path);
        }
    }

    /**
     * Plays a sound effect by its key identifier.
     * The sound restarts from the beginning if already playing.
     * If the sound key is not found, the method does nothing.
     *
     * @param key the sound effect identifier (e.g., "clear", "rotate", "drop")
     */
    public void playSound(String key) {
        MediaPlayer p = players.get(key);
        if (p != null) {
            p.seek(Duration.ZERO);
            p.play();
        }
    }

    /**
     * Plays background music appropriate for the given game mode.
     * Stops any currently playing background music before starting the new track.
     * The music loops indefinitely until stopped or changed.
     *
     * @param mode the game mode to play music for
     */
    public void playBackground(GameMode mode) {
        if (bgPlayer != null) bgPlayer.stop();
        String path = mode == GameMode.ZEN ? "/audio/zen-bg.mp3" : "/audio/blitz-bg.mp3";
        try {
            String url = getClass().getResource(path) != null ?
                    getClass().getResource(path).toExternalForm() : null;
            if (url != null) {
                Media bg = new Media(url);
                bgPlayer = new MediaPlayer(bg);
                bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgPlayer.setVolume(0.35);
                bgPlayer.play();
            }
        } catch (Exception e) {
            System.out.println("Background music not found (OK): " + path);
        }
    }

    /**
     * Stops the currently playing background music.
     * Safe to call even if no music is playing.
     */
    public void stopBackground() { if (bgPlayer != null) bgPlayer.stop(); }
    /**
     * Pauses the currently playing background music.
     * Music can be resumed from the paused position using resumeBackground().
     * Safe to call even if no music is playing.
     */
    public void pauseBackground() { if (bgPlayer != null) bgPlayer.pause(); }
    /**
     * Resumes the background music from where it was paused.
     * Safe to call even if no music is paused.
     */
    public void resumeBackground() { if (bgPlayer != null) bgPlayer.play(); }
}