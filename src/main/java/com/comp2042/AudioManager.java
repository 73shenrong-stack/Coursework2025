package com.comp2042;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

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

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

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

    public void playSound(String key) {
        MediaPlayer p = players.get(key);
        if (p != null) {
            p.seek(Duration.ZERO);
            p.play();
        }
    }

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