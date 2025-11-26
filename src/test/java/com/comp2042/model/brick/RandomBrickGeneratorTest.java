package com.comp2042.model.brick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    @DisplayName("Should return non-null brick")
    void testGetBrick_notNull() {
        Brick brick = generator.getBrick();
        assertNotNull(brick);
        assertNotNull(brick.getShapeMatrix());
    }

    @Test
    @DisplayName("Should return valid next brick without consuming it")
    void testGetNextBrick_peek() {
        Brick next = generator.getNextBrick();
        Brick nextAgain = generator.getNextBrick();

        assertNotNull(next);
        assertSame(next, nextAgain);
    }

    @Test
    @DisplayName("Should generate all 7 brick types using bag system")
    void testBagSystem_allTypes() {
        Set<String> brickTypes = new HashSet<>();

        // Generate 14 bricks (2 bags) to ensure all types appear
        for (int i = 0; i < 14; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass().getSimpleName());
        }

        assertEquals(7, brickTypes.size(), "Should have all 7 types: I, J, L, O, S, T, Z");
        assertTrue(brickTypes.contains("IBrick"));
        assertTrue(brickTypes.contains("JBrick"));
        assertTrue(brickTypes.contains("LBrick"));
        assertTrue(brickTypes.contains("OBrick"));
        assertTrue(brickTypes.contains("SBrick"));
        assertTrue(brickTypes.contains("TBrick"));
        assertTrue(brickTypes.contains("ZBrick"));
    }

    @Test
    @DisplayName("Should generate variety within first few pieces")
    void testBrickVariety_earlyGame() {
        Set<String> brickTypes = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass().getSimpleName());
        }

        assertTrue(brickTypes.size() >= 5, "Expected variety in first 10 pieces");
    }

    @Test
    @DisplayName("Should maintain next brick queue properly")
    void testNextBrickQueue_consistency() {
        Brick peeked = generator.getNextBrick();
        Brick got = generator.getBrick();

        // After getting a brick, should have a new next brick
        Brick newNext = generator.getNextBrick();
        assertNotNull(newNext);
        assertNotSame(peeked, newNext);
    }

    @Test
    @DisplayName("Should create new brick instances")
    void testBrickInstances_unique() {
        Brick brick1 = generator.getBrick();
        Brick brick2 = generator.getBrick();

        assertNotSame(brick1, brick2);
    }

    @Test
    @DisplayName("Each brick should have valid 4x4 shape matrix")
    void testBrickShape_validity() {
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            int[][] shape = brick.getShapeMatrix().get(0);

            assertEquals(4, shape.length, "Should be 4 rows");
            assertEquals(4, shape[0].length, "Should be 4 columns");
        }
    }

    @Test
    @DisplayName("Should refill bag when exhausted")
    void testBagRefill() {
        // Exhaust first bag (7 pieces)
        for (int i = 0; i < 7; i++) {
            generator.getBrick();
        }

        // Next pieces should still be generated
        Brick brick = generator.getBrick();
        assertNotNull(brick);

        // Should have variety in second bag
        Set<String> secondBagTypes = new HashSet<>();
        secondBagTypes.add(brick.getClass().getSimpleName());
        for (int i = 0; i < 6; i++) {
            secondBagTypes.add(generator.getBrick().getClass().getSimpleName());
        }

        assertTrue(secondBagTypes.size() >= 5);
    }
}