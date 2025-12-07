package com.comp2042.audio;

import com.comp2042.model.game.GameMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager for handling all game audio including sound effects and background music.
 * Provides centralized control over audio playback with safe loading that gracefully handles missing files.
 * Background music is loaded on-demand when switching game modes.
 *
 * @see GameMode
 */
public class AudioManager {

    private static AudioManager instance;
    private final Map<String, MediaPlayer> players = new HashMap<>();
    private MediaPlayer bgPlayer;

    private AudioManager() {
        // Safe loading – if file missing, just skip silently
        loadSound("clear", "/audio/clear.mp3");
        loadSound("rotate", "/audio/rotate.mp3");
        loadSound("drop", "/audio/drop.mp3");
        loadSound("gameover", "/audio/gameover.mp3");
        loadSound("victory", "/audio/victory.mp3");
    }

    /**
     * Returns the singleton instance of AudioManager.
     * Creates the instance on first access (lazy initialization).
     *
     * @return the single AudioManager instance
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Loads a sound effect from the specified resource path and stores it in the players map.
     * If the file cannot be found or loaded, a message is printed to console and the sound is skipped.
     * This allows the game to continue functioning even when audio files are missing during development.
     *
     * @param key the unique identifier for this sound effect (e.g., "clear", "rotate")
     * @param path the classpath resource path to the audio file (e.g., "/audio/clear.mp3")
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
     * Plays the sound effect associated with the given key.
     * The sound is reset to the beginning before playing, allowing it to be triggered multiple times rapidly.
     * If the sound doesn't exist (wasn't loaded successfully), this method does nothing.
     *
     * @param key the identifier of the sound effect to play (e.g., "clear", "rotate", "drop")
     */
    public void playSound(String key) {
        MediaPlayer p = players.get(key);
        if (p != null) {
            p.seek(Duration.ZERO);
            p.play();
        }
    }

    /**
     * Plays background music appropriate for the specified game mode.
     * Stops any currently playing background music before starting the new track.
     * The music loops indefinitely until stopped or changed.
     *
     * @param mode the current game mode, used to select appropriate background music
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

    public void stopBackground() { if (bgPlayer != null) bgPlayer.stop(); }
    public void pauseBackground() { if (bgPlayer != null) bgPlayer.pause(); }
    public void resumeBackground() { if (bgPlayer != null) bgPlayer.play(); }
}