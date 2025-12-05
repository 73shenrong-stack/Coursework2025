package com.comp2042.controller;

import com.comp2042.audio.AudioManager;
import com.comp2042.view.GameViewController;
import com.comp2042.model.game.GameMode;
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

/**
 * Controller for the main menu screen.
 * Handles navigation between the menu and game modes and provides an exit option.
 * Applies appropriate CSS styling for the menu view and manages scene transitions to the game view.
 */
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

    /**
     * Starts a game with the specified mode.
     * Loads the game view FXML, transitions the scene, applies game styling,
     * and initializes the game controller with the selected mode.
     *
     * @param mode the game mode to start
     */
    private void startGame(GameMode mode) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GameViewController c = fxmlLoader.getController();

            // Use blitzModeButton instead of newGameButton
            Scene currentScene = blitzModeButton.getScene();
            Stage stage = (Stage) currentScene.getWindow();

            // clear mainmenu.css
            currentScene.getStylesheets().clear();

            currentScene.setRoot(root);

            stage.setTitle("TetrisJFX - " + getModeTitle(mode));
            stage.setResizable(true);
            stage.setMinWidth(950);
            stage.setMinHeight(700);

            new GameController(c, mode);  // ADD mode parameter here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the display title for a game mode.
     * Converts the GameMode enum to a user-friendly display string.
     *
     * @param mode the game mode
     * @return the display title string for the window title bar
     */
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

    /**
     * Handles the exit button action.
     * Closes the application window, terminating the program.
     *
     * @param event the button click event
     */
    @FXML
    public void onExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns to the main menu from the game view.
     * Loads the main menu FXML, applies menu styling, and stops background music.
     * This is a static utility method that can be called from anywhere with access to a scene node.
     *
     * @param currentNode any node in the current scene (used to access Stage)
     */
    public static void returnToMainMenu(Node currentNode) {
        try {
            URL location = MainMenuController.class.getClassLoader().getResource("MainMenu.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();

            Scene currentScene = currentNode.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("TetrisJFX - Main Menu");

            currentScene.getStylesheets().clear();
            String css = MainMenuController.class.getClassLoader().getResource("mainMenu.css").toExternalForm();
            currentScene.getStylesheets().add(css);

            currentScene.setRoot(root);
            AudioManager.getInstance().stopBackground();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}