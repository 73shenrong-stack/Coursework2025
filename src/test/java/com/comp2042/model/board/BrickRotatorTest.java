package com.comp2042.model.board;

import com.comp2042.model.brick.Brick;
import com.comp2042.model.brick.RandomBrickGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;
    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        generator = new RandomBrickGenerator();
    }

    @Test
    @DisplayName("Should set brick and reset rotation to zero")
    void testSetBrick_resetsRotation() {
        Brick brick = generator.getBrick();

        rotator.setBrick(brick);

        assertNotNull(rotator.getBrick());
        assertEquals(0, rotator.getRotationIndex());
    }

    @Test
    @DisplayName("Should return current shape at rotation index")
    void testGetCurrentShape() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        int[][] shape = rotator.getCurrentShape();

        assertNotNull(shape);
        assertEquals(4, shape.length);
        assertEquals(4, shape[0].length);
    }

    @Test
    @DisplayName("Should cycle through rotation states")
    void testRotationCycle() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        int maxRotations = brick.getShapeMatrix().size();

        for (int i = 0; i < maxRotations; i++) {
            rotator.setRotationIndex(i);
            assertEquals(i, rotator.getRotationIndex());
            assertNotNull(rotator.getCurrentShape());
        }
    }

    @Test
    @DisplayName("Different rotations should yield different shapes")
    void testRotationChangesShape() {
        // Find a brick with multiple rotations
        Brick brick = null;
        for (int i = 0; i < 10; i++) {
            Brick candidate = generator.getBrick();
            if (candidate.getShapeMatrix().size() > 1) {
                brick = candidate;
                break;
            }
        }

        assertNotNull(brick, "Should find a brick with multiple rotations");
        rotator.setBrick(brick);

        if (brick.getShapeMatrix().size() > 1) {
            rotator.setRotationIndex(0);
            int[][] shape0 = rotator.getCurrentShape();

            rotator.setRotationIndex(1);
            int[][] shape1 = rotator.getCurrentShape();

            boolean different = false;
            for (int i = 0; i < shape0.length && !different; i++) {
                for (int j = 0; j < shape0[i].length && !different; j++) {
                    if (shape0[i][j] != shape1[i][j]) {
                        different = true;
                    }
                }
            }

            assertTrue(different, "Different rotations should produce different shapes");
        }
    }

    @Test
    @DisplayName("Should maintain brick reference")
    void testGetBrick_sameReference() {
        Brick original = generator.getBrick();
        rotator.setBrick(original);

        Brick retrieved = rotator.getBrick();

        assertSame(original, retrieved);
    }

    @Test
    @DisplayName("Should handle O-brick single rotation state")
    void testOBrick_singleRotation() {
        // O-brick has only one rotation state
        Brick oBrick = null;
        for (int i = 0; i < 20; i++) {
            Brick candidate = generator.getBrick();
            if (candidate.getClass().getSimpleName().equals("OBrick")) {
                oBrick = candidate;
                break;
            }
        }

        if (oBrick != null) {
            rotator.setBrick(oBrick);
            assertEquals(1, oBrick.getShapeMatrix().size());

            int[][] shape = rotator.getCurrentShape();
            assertNotNull(shape);
        }
    }

    @Test
    @DisplayName("Should return valid shape for any rotation index")
    void testGetCurrentShape_alwaysValid() {
        Brick brick = generator.getBrick();
        rotator.setBrick(brick);

        int maxRotations = brick.getShapeMatrix().size();
        for (int i = 0; i < maxRotations; i++) {
            rotator.setCurrentShape(i);

            int[][] shape = rotator.getCurrentShape();
            assertNotNull(shape);
            assertEquals(4, shape.length);
        }
    }
}