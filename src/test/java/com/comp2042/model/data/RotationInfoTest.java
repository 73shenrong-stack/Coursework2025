package com.comp2042.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RotationInfoTest {

    @Test
    void testConstruction_WithValidData() {
        int[][] shape = {{1, 0}, {0, 1}};
        RotationInfo info = new RotationInfo(shape, 2);

        assertNotNull(info);
        assertEquals(2, info.getPosition());
    }

    @Test
    void testGetShape_ReturnsDefensiveCopy() {
        int[][] shape = {{1, 0}, {0, 1}};
        RotationInfo info = new RotationInfo(shape, 0);

        int[][] retrieved1 = info.getShape();
        int[][] retrieved2 = info.getShape();

        assertNotSame(retrieved1, retrieved2, "Should return new copy each time");
        assertNotSame(shape, retrieved1, "Should not return original reference");
    }

    @Test
    void testGetShape_ModificationDoesNotAffectOriginal() {
        int[][] shape = {{1, 0}, {0, 1}};
        RotationInfo info = new RotationInfo(shape, 0);

        int[][] retrieved = info.getShape();
        retrieved[0][0] = 999;

        assertEquals(1, info.getShape()[0][0], "Original should be unchanged");
    }

    @Test
    void testGetPosition_ReturnsCorrectValue() {
        int[][] shape = {{1}};
        RotationInfo info = new RotationInfo(shape, 3);

        assertEquals(3, info.getPosition());
    }

    @Test
    void testConstruction_WithZeroPosition() {
        int[][] shape = {{1, 1}, {1, 1}};
        RotationInfo info = new RotationInfo(shape, 0);

        assertEquals(0, info.getPosition());
    }

    @Test
    void testConstruction_WithLargePosition() {
        int[][] shape = {{1}};
        RotationInfo info = new RotationInfo(shape, 100);

        assertEquals(100, info.getPosition());
    }

    @Test
    void testConstruction_WithEmptyShape() {
        int[][] shape = new int[0][0];
        RotationInfo info = new RotationInfo(shape, 1);

        assertNotNull(info);
        assertEquals(0, info.getShape().length);
    }

    @Test
    void testConstruction_WithLargeShape() {
        int[][] shape = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                shape[i][j] = (i + j) % 7;
            }
        }

        RotationInfo info = new RotationInfo(shape, 0);

        assertNotNull(info);
        assertEquals(10, info.getShape().length);
    }

    @Test
    void testMultipleGetShapeCalls_IndependentCopies() {
        int[][] shape = {{1, 2}, {3, 4}};
        RotationInfo info = new RotationInfo(shape, 0);

        int[][] copy1 = info.getShape();
        int[][] copy2 = info.getShape();

        copy1[0][0] = 999;

        assertEquals(1, copy2[0][0], "Other copies should not be affected");
    }

    @Test
    void testGetShape_PreservesStructure() {
        int[][] shape = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        RotationInfo info = new RotationInfo(shape, 0);
        int[][] retrieved = info.getShape();

        assertEquals(3, retrieved.length);
        assertEquals(3, retrieved[0].length);
        assertEquals(5, retrieved[1][1]);
    }

    @Test
    void testConstruction_WithNegativePosition() {
        int[][] shape = {{1}};
        RotationInfo info = new RotationInfo(shape, -1);

        assertEquals(-1, info.getPosition());
    }

    @Test
    void testGetPosition_Immutable() {
        int[][] shape = {{1}};
        RotationInfo info = new RotationInfo(shape, 5);

        int pos1 = info.getPosition();
        int pos2 = info.getPosition();

        assertEquals(pos1, pos2);
    }
}