package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
/**
 * Starts the JavaFX application by initializing the primary stage
 * The application starts in the main menu scene, from which players can select game modes (Normal, 40 Lines, Blitz, Zen), view high scores, adjust settings or exit the game.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by initializing the primary stage and loading the main menu scene.
     *
     * @param primaryStage the primary stage for this application onto which the application scene is set
     * @throws Exception if the FXML file or CSS resource cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("MainMenu.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TetrisJFX - Main Menu");
        Scene scene = new Scene(root, 950, 700);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
                event.consume();
            }
        });

        // Load the CSS file
        String css = getClass().getClassLoader().getResource("mainMenu.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(700);

        primaryStage.show();
    }

    /**
     * Main method – launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
