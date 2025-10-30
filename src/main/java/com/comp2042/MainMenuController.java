package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private javafx.scene.control.Button newGameButton;

    @FXML
    private javafx.scene.control.Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Any initialization if needed
    }

    @FXML
    public void onNewGame(ActionEvent event) {
        try {
            URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();

            Stage stage = (Stage) newGameButton.getScene().getWindow();
            stage.setTitle("TetrisJFX");
            Scene scene = new Scene(root, 450, 550);
            stage.setScene(scene);

            new GameController(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}