package com.comp2042.controller;

import com.comp2042.model.game.GameMode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuControllerTest {

    private MainMenuController controller;
    private Button testButton;
    private Stage testStage;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Replaced JFXPanel with Platform.startup to avoid dependency issues
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            // If Toolkit is already initialized, just continue
            latch.countDown();
        }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("JavaFX took too long to start");
        }
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller = new MainMenuController();
                testButton = new Button();
                testStage = new Stage();

                // This prevents NullPointerException when .getScene() is called
                VBox root = new VBox(testButton);
                Scene scene = new Scene(root, 800, 600);
                testStage.setScene(scene);

                // We use reflection to set the private fields in the controller
                // These names match exactly what is in your MainMenuController.java
                injectField(controller, "blitzModeButton", testButton);
                injectField(controller, "zenModeButton", testButton);
                injectField(controller, "fortyLinesModeButton", testButton);
                injectField(controller, "exitButton", testButton);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * Helper method to inject private FXML fields
     */
    private void injectField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            System.err.println("Test Warning: Field '" + fieldName + "' not found in controller.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not inject field " + fieldName, e);
        }
    }

    @Test
    void testInitialize() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> controller.initialize(null, null));
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testOnBlitzMode_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            ActionEvent event = new ActionEvent(testButton, null);
            // These methods will fail to load FXML in test environment, but should not throw exceptions
            // They will print error messages and return early due to null checks
            controller.onBlitzMode(event);
            // Test passes if no exception is thrown
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testOnFortyLinesMode_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            ActionEvent event = new ActionEvent(testButton, null);
            // These methods will fail to load FXML in test environment, but should not throw exceptions
            // They will print error messages and return early due to null checks
            controller.onFortyLinesMode(event);
            // Test passes if no exception is thrown
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testOnZenMode_NoException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            ActionEvent event = new ActionEvent(testButton, null);
            // These methods will fail to load FXML in test environment, but should not throw exceptions
            // They will print error messages and return early due to null checks
            controller.onZenMode(event);
            // Test passes if no exception is thrown
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testOnExit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            ActionEvent event = new ActionEvent(testButton, null);
            // This will work now because 'exitButton' is injected and attached to a Stage
            assertDoesNotThrow(() -> controller.onExit(event));
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testReturnToMainMenu_WithValidNode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // This will fail to load FXML but should not throw exception
            // It will print error message and return early
            MainMenuController.returnToMainMenu(testButton);
            // Test passes if no exception is thrown
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testGetModeTitle_Blitz() {
        try {
            java.lang.reflect.Method method = MainMenuController.class.getDeclaredMethod("getModeTitle", GameMode.class);
            method.setAccessible(true);
            String title = (String) method.invoke(controller, GameMode.BLITZ);
            assertEquals("Blitz Mode", title);
        } catch (Exception e) {
            fail("Could not test getModeTitle method: " + e.getMessage());
        }
    }

    @Test
    void testGetModeTitle_FortyLines() {
        try {
            java.lang.reflect.Method method = MainMenuController.class.getDeclaredMethod("getModeTitle", GameMode.class);
            method.setAccessible(true);
            String title = (String) method.invoke(controller, GameMode.FORTY_LINES);
            assertEquals("40 Lines Mode", title);
        } catch (Exception e) {
            fail("Could not test getModeTitle method");
        }
    }

    @Test
    void testGetModeTitle_Zen() {
        try {
            java.lang.reflect.Method method = MainMenuController.class.getDeclaredMethod("getModeTitle", GameMode.class);
            method.setAccessible(true);
            String title = (String) method.invoke(controller, GameMode.ZEN);
            assertEquals("Zen Mode", title);
        } catch (Exception e) {
            fail("Could not test getModeTitle method");
        }
    }

    @Test
    void testControllerImplementsInitializable() {
        assertTrue(controller instanceof javafx.fxml.Initializable);
    }
}