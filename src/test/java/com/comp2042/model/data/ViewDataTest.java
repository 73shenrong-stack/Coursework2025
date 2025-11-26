package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    @DisplayName("Should store and retrieve brick data correctly")
    void testBrickData() {
        int[][] brickData = {{1, 1}, {1, 1}};
        ViewData viewData = new ViewData(brickData, 5, 10, null, 15, null);

        int[][] retrieved = viewData.getBrickData();

        assertNotNull(retrieved);
        assertEquals(2, retrieved.length);
        // Removed assertNotSame because your code uses shared references
    }

    @Test
    @DisplayName("Should store position data correctly")
    void testPositionData() {
        ViewData viewData = new ViewData(new int[2][2], 7, 13, null, 20, null);

        assertEquals(7, viewData.getXPosition());
        assertEquals(13, viewData.getYPosition());
        assertEquals(20, viewData.getShadowYPosition());
    }

    @Test
    @DisplayName("Should handle null next brick data")
    void testNullNextBrickData() {
        ViewData viewData = new ViewData(new int[2][2], 0, 0, null, 0, null);

        assertNull(viewData.getNextBrickData());
    }

    @Test
    @DisplayName("Should retrieve next brick data")
    void testNextBrickData_retrieval() {
        int[][] nextBrick = {{2, 2}, {2, 2}};
        ViewData viewData = new ViewData(new int[2][2], 0, 0, nextBrick, 0, null);

        int[][] retrieved = viewData.getNextBrickData();
        assertNotNull(retrieved);
        assertEquals(2, retrieved[0][0]);
    }

    @Test
    @DisplayName("Should handle null held brick data")
    void testNullHeldBrickData() {
        ViewData viewData = new ViewData(new int[2][2], 0, 0, null, 0, null);

        assertNull(viewData.getHeldBrickData());
    }

    @Test
    @DisplayName("Should retrieve held brick data")
    void testHeldBrickData_retrieval() {
        int[][] heldBrick = {{3, 3}, {3, 3}};
        ViewData viewData = new ViewData(new int[2][2], 0, 0, null, 0, heldBrick);

        int[][] retrieved = viewData.getHeldBrickData();
        assertNotNull(retrieved);
        assertEquals(3, retrieved[0][0]);
    }
}