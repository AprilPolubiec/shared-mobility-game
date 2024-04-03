package com.rideshare.Controllers;

import com.rideshare.App;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class HomeController extends Controller {
    @FXML
    public javafx.scene.control.Button loadGameButton;
    @FXML
    public javafx.scene.control.Button newGameButton;
    @FXML
    public javafx.scene.control.Button startGameButton;
    @FXML
    public javafx.scene.control.Button instructionsButton;

    private AnchorPane _root;

    public HomeController() {
        this(_stage);
    }

    public HomeController(Stage stage) {
        super(stage);
        Controller.homeController = this;
    }

    @Override
    public void load() {
        try {
            playAudio("bg-slow");
            _stage.setWidth(720);
            _stage.setHeight(439);
            _stage.centerOnScreen();
            _root = setScene("home", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStartButtonPressed() {
        try {
            loadScreen("game", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLoadButtonPressed() {
        // TODO: load saved game file
        // TODO: Show saved game options
    }

    @FXML
    public void handleGameSelected() {
        // TODO: load saved game file
        // TODO: Show saved game options
    }

    @FXML
    public void handleNewGameButtonPressed() {
        // TODO: prompt user for name, etc
        // TODO: create a player
    }

    @FXML
    public void handleInstructionsButtonPressed() {
        // this.loadInstructionsScreen();
    }

}
