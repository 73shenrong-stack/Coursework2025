package com.comp2042.model.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    @DisplayName("Should start with zero score")
    void testInitialScore() {
        assertEquals(0, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Should add points correctly")
    void testAdd_singleValue() {
        score.add(100);
        assertEquals(100, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Should accumulate multiple additions")
    void testAdd_multipleValues() {
        score.add(10);
        score.add(20);
        score.add(30);

        assertEquals(60, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Should handle large scores")
    void testAdd_largeValues() {
        score.add(999999);
        score.add(1);

        assertEquals(1000000, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Should reset score to zero")
    void testReset() {
        score.add(500);
        score.reset();

        assertEquals(0, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Should allow adding after reset")
    void testAddAfterReset() {
        score.add(100);
        score.reset();
        score.add(50);

        assertEquals(50, score.scoreProperty().get());
    }

    @Test
    @DisplayName("Score property should be observable")
    void testScoreProperty_observable() {
        assertNotNull(score.scoreProperty());

        final int[] observedValue = {0};
        score.scoreProperty().addListener((obs, oldVal, newVal) -> {
            observedValue[0] = newVal.intValue();
        });

        score.add(42);
        assertEquals(42, observedValue[0]);
    }
}