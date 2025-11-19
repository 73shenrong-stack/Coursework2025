package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button blitzModeButton;

    @FXML
    private Button fortyLinesModeButton;

    @FXML
    private Button zenModeButton;

    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Any initialization if needed
    }

    @FXML
    public void onBlitzMode(ActionEvent event) {
        startGame(GameMode.BLITZ);
    }

    @FXML
    public void onFortyLinesMode(ActionEvent event) {
        startGame(GameMode.FORTY_LINES);
    }

    @FXML
    public void onZenMode(ActionEvent event) {
        startGame(GameMode.ZEN);
    }

    private void startGame(GameMode mode) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            // Use blitzModeButton instead of newGameButton
            Stage stage = (Stage) blitzModeButton.getScene().getWindow();
            stage.setTitle("TetrisJFX - " + getModeTitle(mode));
            Scene scene = new Scene(root, 900, 700);
            stage.setScene(scene);
            stage.setResizable(false);

            new GameController(c, mode);  // ADD mode parameter here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getModeTitle(GameMode mode) {
        switch (mode) {
            case BLITZ:
                return "Blitz Mode";
            case FORTY_LINES:
                return "40 Lines Mode";
            case ZEN:
                return "Zen Mode";
            default:
                return "TetrisJFX";
        }
    }

    @FXML
    public void onExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public static void returnToMainMenu(Node currentNode) {
        try {
            URL location = MainMenuController.class.getClassLoader().getResource("MainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) currentNode.getScene().getWindow();
            stage.setTitle("TetrisJFX - Main Menu");
            Scene scene = new Scene(root, 900, 700);

            String css = MainMenuController.class.getClassLoader().getResource("mainMenu.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}