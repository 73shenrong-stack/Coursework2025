package com.comp2042.model.game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles saving and loading game records to/from disk
 * Provides fail-safe loading that returns default records if the file doesn't exist or is corrupted.
 */
public class RecordsPersistence {
    private static final String RECORDS_DIR = System.getProperty("user.home") + File.separator + ".tetrisjfx";
    private static final String RECORDS_FILE = RECORDS_DIR + File.separator + "records.dat";

    /**
     * Saves game records to disk using serialization.
     * Creates the storage directory if it doesn't exist.
     *
     * @param records the GameRecords object to save
     * @return true if save was successful, false if an error occurred
     */

    public static boolean saveRecords(GameRecords records) {
        try {
            // Create directory if it doesn't exist
            Path dirPath = Paths.get(RECORDS_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Save records using serialization
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(RECORDS_FILE))) {
                oos.writeObject(records);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Failed to save records: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads game records from disk.
     * If the file doesn't exist or cannot be read, returns a new
     * GameRecords object with default values.
     *
     * @return GameRecords object loaded from disk, or new GameRecords if load fails
     */
    public static GameRecords loadRecords() {
        File recordsFile = new File(RECORDS_FILE);

        if (!recordsFile.exists()) {
            return new GameRecords();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(RECORDS_FILE))) {
            return (GameRecords) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load records: " + e.getMessage());
            return new GameRecords();
        }
    }
}