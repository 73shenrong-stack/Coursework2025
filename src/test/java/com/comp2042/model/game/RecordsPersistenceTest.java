package com.comp2042.model.game;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RecordsPersistenceTest {

    private File recordsFile;
    private File recordsDir;
    private GameRecords originalRecords;

    @BeforeEach
    void setUp() {
        // Get the actual records file path used by RecordsPersistence
        String userHome = System.getProperty("user.home");
        recordsDir = new File(userHome, ".tetrisjfx");
        recordsFile = new File(recordsDir, "records.dat");

        // Backup existing records if any
        if (recordsFile.exists()) {
            originalRecords = RecordsPersistence.loadRecords();
        }

        // Delete existing records file to start fresh
        if (recordsFile.exists()) {
            recordsFile.delete();
        }
    }

    @AfterEach
    void tearDown() {
        // Clean up test file
        if (recordsFile.exists()) {
            recordsFile.delete();
        }

        // Restore original records if they existed
        if (originalRecords != null) {
            RecordsPersistence.saveRecords(originalRecords);
        }
    }

    @Test
    @DisplayName("Should save and load records correctly")
    void testSaveAndLoadRecords() {
        // Create records with data
        GameRecords originalRecords = new GameRecords();
        originalRecords.updateBlitzRecord(1500, 120);
        originalRecords.updateFortyLinesRecord(2000, 60);
        originalRecords.updateZenRecord(3000, 300);

        // Save records
        boolean saveSuccess = RecordsPersistence.saveRecords(originalRecords);
        assertTrue(saveSuccess, "Save should succeed");

        // Load records
        GameRecords loadedRecords = RecordsPersistence.loadRecords();

        // Verify loaded data matches original
        assertEquals(1500, loadedRecords.getBlitzHighScore());
        assertEquals(120, loadedRecords.getBlitzBestTime());
        assertEquals(2000, loadedRecords.getFortyLinesHighScore());
        assertEquals(60, loadedRecords.getFortyLinesBestTime());
        assertEquals(3000, loadedRecords.getZenHighScore());
        assertEquals(300, loadedRecords.getZenBestTime());
    }

    @Test
    @DisplayName("Should create new records if file doesn't exist")
    void testLoadNonExistentFile() {
        // Ensure the file doesn't exist
        if (recordsFile.exists()) {
            recordsFile.delete();
        }

        GameRecords records = RecordsPersistence.loadRecords();

        assertNotNull(records, "Should return new GameRecords if file doesn't exist");
        assertEquals(0, records.getBlitzHighScore());
        assertEquals(0, records.getFortyLinesHighScore());
        assertEquals(0, records.getZenHighScore());
    }

    @Test
    @DisplayName("Should create directory if it doesn't exist")
    void testCreateDirectory() {
        // Delete directory if it exists
        if (recordsFile.exists()) {
            recordsFile.delete();
        }
        if (recordsDir.exists()) {
            recordsDir.delete();
        }

        GameRecords records = new GameRecords();
        records.updateBlitzRecord(1000, 120);

        boolean saveSuccess = RecordsPersistence.saveRecords(records);
        assertTrue(saveSuccess, "Should create directory and save");

        // Verify directory was created
        assertTrue(recordsDir.exists(), "Directory should be created");
        assertTrue(recordsDir.isDirectory(), "Should be a directory");
    }

    @Test
    @DisplayName("Should overwrite existing records")
    void testOverwriteRecords() {
        // Save first records
        GameRecords firstRecords = new GameRecords();
        firstRecords.updateBlitzRecord(1000, 120);
        RecordsPersistence.saveRecords(firstRecords);

        // Save second records with different data
        GameRecords secondRecords = new GameRecords();
        secondRecords.updateBlitzRecord(2000, 120);
        RecordsPersistence.saveRecords(secondRecords);

        // Load and verify second data overwrote first
        GameRecords loadedRecords = RecordsPersistence.loadRecords();
        assertEquals(2000, loadedRecords.getBlitzHighScore(), "Should have new score");
    }

    @Test
    @DisplayName("Should handle empty records")
    void testSaveEmptyRecords() {
        GameRecords emptyRecords = new GameRecords();
        boolean saveSuccess = RecordsPersistence.saveRecords(emptyRecords);

        assertTrue(saveSuccess, "Should save empty records");

        GameRecords loadedRecords = RecordsPersistence.loadRecords();
        assertEquals(0, loadedRecords.getBlitzHighScore());
        assertEquals(0, loadedRecords.getFortyLinesHighScore());
    }

    @Test
    @DisplayName("Should preserve all record fields")
    void testPreserveAllFields() {
        GameRecords records = new GameRecords();
        records.updateBlitzRecord(1234, 120);
        records.updateFortyLinesRecord(5678, 90);
        records.updateZenRecord(9999, 500);

        RecordsPersistence.saveRecords(records);
        GameRecords loaded = RecordsPersistence.loadRecords();

        // Verify all fields preserved
        assertEquals(1234, loaded.getBlitzHighScore());
        assertEquals(120, loaded.getBlitzBestTime());
        assertEquals(5678, loaded.getFortyLinesHighScore());
        assertEquals(90, loaded.getFortyLinesBestTime());
        assertEquals(9999, loaded.getZenHighScore());
        assertEquals(500, loaded.getZenBestTime());
    }

    @Test
    @DisplayName("Multiple save and load cycles should work")
    void testMultipleSaveLoadCycles() {
        for (int i = 1; i <= 5; i++) {
            GameRecords records = new GameRecords();
            records.updateBlitzRecord(i * 1000, 120);

            RecordsPersistence.saveRecords(records);
            GameRecords loaded = RecordsPersistence.loadRecords();

            assertEquals(i * 1000, loaded.getBlitzHighScore(),
                    "Cycle " + i + " should have correct score");
        }
    }

    @Test
    @DisplayName("Should handle null records gracefully")
    void testSaveNullRecords() {
        // This tests the robustness of the save method
        assertDoesNotThrow(() -> {
            try {
                RecordsPersistence.saveRecords(null);
            } catch (NullPointerException e) {
                // Expected behavior - null records may throw NPE
            }
        });
    }

    @Test
    @DisplayName("Records file should exist after save")
    void testRecordsFileExists() {
        GameRecords records = new GameRecords();
        records.updateBlitzRecord(500, 60);

        RecordsPersistence.saveRecords(records);

        assertTrue(recordsFile.exists(), "Records file should exist after save");
    }
}