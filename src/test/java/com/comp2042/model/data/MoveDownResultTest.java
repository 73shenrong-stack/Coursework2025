package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MoveDownResultTest {

    @Test
    @DisplayName("Should store move down result with line clear")
    void testWithLineClear() {
        LineClearResult clearResult = new LineClearResult(2, new int[2][2], 200);
        ViewData viewData = new ViewData(new int[2][2], 5, 10, null, 15, null);

        MoveDownResult result = new MoveDownResult(clearResult, viewData);

        assertNotNull(result.getClearRow());
        assertEquals(2, result.getClearRow().getLinesRemoved());
        assertNotNull(result.getViewData());
    }

    @Test
    @DisplayName("Should handle null line clear result")
    void testWithoutLineClear() {
        ViewData viewData = new ViewData(new int[2][2], 5, 10, null, 15, null);

        MoveDownResult result = new MoveDownResult(null, viewData);

        assertNull(result.getClearRow());
        assertNotNull(result.getViewData());
    }

    @Test
    @DisplayName("Should provide access to view data")
    void testViewDataAccess() {
        ViewData viewData = new ViewData(new int[2][2], 7, 13, null, 20, null);
        MoveDownResult result = new MoveDownResult(null, viewData);

        ViewData retrieved = result.getViewData();
        assertEquals(7, retrieved.getXPosition());
        assertEquals(13, retrieved.getYPosition());
    }
}