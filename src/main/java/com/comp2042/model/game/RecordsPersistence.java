package com.comp2042.model.game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles saving and loading game records to/from disk
 */
public class RecordsPersistence {
    private static final String RECORDS_DIR = System.getProperty("user.home") + File.separator + ".tetrisjfx";
    private static final String RECORDS_FILE = RECORDS_DIR + File.separator + "records.dat";

    // Save game records to disk

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

    // Load game records from disk
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