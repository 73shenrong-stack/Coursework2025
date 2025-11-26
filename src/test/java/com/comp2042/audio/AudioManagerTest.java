package com.comp2042.audio;

import com.comp2042.model.game.GameMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioManagerTest {

    private AudioManager audioManager;

    @BeforeEach
    void setUp() {
        audioManager = AudioManager.getInstance();
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        AudioManager instance1 = AudioManager.getInstance();
        AudioManager instance2 = AudioManager.getInstance();

        assertSame(instance1, instance2, "AudioManager should be singleton");
    }

    @Test
    void testGetInstance_NotNull() {
        assertNotNull(AudioManager.getInstance());
    }

    @Test
    void testPlaySound_NoException() {
        assertDoesNotThrow(() -> audioManager.playSound("clear"));
        assertDoesNotThrow(() -> audioManager.playSound("rotate"));
        assertDoesNotThrow(() -> audioManager.playSound("drop"));
        assertDoesNotThrow(() -> audioManager.playSound("gameover"));
        assertDoesNotThrow(() -> audioManager.playSound("victory"));
    }

    @Test
    void testPlaySound_InvalidKey_NoException() {
        assertDoesNotThrow(() -> audioManager.playSound("nonexistent"));
    }

    @Test
    void testPlaySound_NullKey_NoException() {
        assertDoesNotThrow(() -> audioManager.playSound(null));
    }

    @Test
    void testPlayBackground_BlitzMode_NoException() {
        assertDoesNotThrow(() -> audioManager.playBackground(GameMode.BLITZ));
    }

    @Test
    void testPlayBackground_ZenMode_NoException() {
        assertDoesNotThrow(() -> audioManager.playBackground(GameMode.ZEN));
    }

    @Test
    void testPlayBackground_FortyLinesMode_NoException() {
        assertDoesNotThrow(() -> audioManager.playBackground(GameMode.FORTY_LINES));
    }

    @Test
    void testStopBackground_NoException() {
        audioManager.playBackground(GameMode.BLITZ);
        assertDoesNotThrow(() -> audioManager.stopBackground());
    }

    @Test
    void testPauseBackground_NoException() {
        audioManager.playBackground(GameMode.BLITZ);
        assertDoesNotThrow(() -> audioManager.pauseBackground());
    }

    @Test
    void testResumeBackground_NoException() {
        audioManager.playBackground(GameMode.BLITZ);
        audioManager.pauseBackground();
        assertDoesNotThrow(() -> audioManager.resumeBackground());
    }

    @Test
    void testMultipleSoundCalls_NoException() {
        assertDoesNotThrow(() -> {
            audioManager.playSound("clear");
            audioManager.playSound("rotate");
            audioManager.playSound("drop");
        });
    }

    @Test
    void testBackgroundMusicSwitch_NoException() {
        assertDoesNotThrow(() -> {
            audioManager.playBackground(GameMode.BLITZ);
            audioManager.playBackground(GameMode.ZEN);
        });
    }

    @Test
    void testStopWithoutPlay_NoException() {
        assertDoesNotThrow(() -> audioManager.stopBackground());
    }

    @Test
    void testPauseWithoutPlay_NoException() {
        assertDoesNotThrow(() -> audioManager.pauseBackground());
    }

    @Test
    void testResumeWithoutPlay_NoException() {
        assertDoesNotThrow(() -> audioManager.resumeBackground());
    }
}