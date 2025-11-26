package com.comp2042.model.brick;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IBrickTest {

    @Test
    void testGetShapeMatrix_NotNull() {
        Brick brick = new IBrick();
        assertNotNull(brick.getShapeMatrix());
    }

    @Test
    void testGetShapeMatrix_HasTwoRotations() {
        Brick brick = new IBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectSize() {
        Brick brick = new IBrick();
        int[][] shape = brick.getShapeMatrix().get(0);
        assertEquals(4, shape.length);
        assertEquals(4, shape[0].length);
    }

    @Test
    void testGetShapeMatrix_DeepCopy() {
        Brick brick = new IBrick();
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2);
        assertNotSame(shapes1.get(0), shapes2.get(0));
    }

    @Test
    void testGetShapeMatrix_ContainsCorrectValue() {
        Brick brick = new IBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasOne = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 1) hasOne = true;
            }
        }
        assertTrue(hasOne);
    }
}

class JBrickTest {

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        Brick brick = new JBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new JBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasTwo = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 2) hasTwo = true;
            }
        }
        assertTrue(hasTwo);
    }
}

class LBrickTest {

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        Brick brick = new LBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new LBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasThree = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 3) hasThree = true;
            }
        }
        assertTrue(hasThree);
    }
}

class OBrickTest {

    @Test
    void testGetShapeMatrix_HasOneRotation() {
        Brick brick = new OBrick();
        assertEquals(1, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new OBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasFour = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 4) hasFour = true;
            }
        }
        assertTrue(hasFour);
    }

    @Test
    void testGetShapeMatrix_Square() {
        Brick brick = new OBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        // Count the 4s - should be 4 (2x2 square)
        int count = 0;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 4) count++;
            }
        }
        assertEquals(4, count);
    }
}

class SBrickTest {

    @Test
    void testGetShapeMatrix_HasTwoRotations() {
        Brick brick = new SBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new SBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasFive = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 5) hasFive = true;
            }
        }
        assertTrue(hasFive);
    }
}

class TBrickTest {

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        Brick brick = new TBrick();
        assertEquals(4, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new TBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasSix = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 6) hasSix = true;
            }
        }
        assertTrue(hasSix);
    }
}

class ZBrickTest {

    @Test
    void testGetShapeMatrix_HasTwoRotations() {
        Brick brick = new ZBrick();
        assertEquals(2, brick.getShapeMatrix().size());
    }

    @Test
    void testGetShapeMatrix_CorrectValue() {
        Brick brick = new ZBrick();
        int[][] shape = brick.getShapeMatrix().get(0);

        boolean hasSeven = false;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 7) hasSeven = true;
            }
        }
        assertTrue(hasSeven);
    }
}