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

public class Main extends Application {

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
    public static void main(String[] args) {
        launch(args);
    }
}
