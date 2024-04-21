package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import com.rideshare.ScoreKeeper;
import com.rideshare.SaveManager.SaveLoad;
import java.net.URL;

public class HomeController {

    private ScoreKeeper sk;
    private SaveLoad saveLoad;

    public void initialize() {
        sk = new ScoreKeeper();
        saveLoad = new SaveLoad(sk);
    }

    @FXML
    public javafx.scene.control.Button loadGameButton;
    @FXML
    public javafx.scene.control.Button newGameButton;
    @FXML
    public javafx.scene.control.Button startGameButton;
    @FXML
    public javafx.scene.control.Button instructionsButton;

    private AnchorPane _root;
    private Stage _stage;

    private void setScene(boolean isFullScreen) throws IOException {
        Scene scene = new Scene(_root);
        _stage.setScene(scene);
        _stage.setFullScreen(isFullScreen);
    }

    public void load(Stage stage, AnchorPane root) {
        try {
            _root = root;
            _stage = stage;
            Media media = new Media(App.class.getResource(String.format("/images/audio/%s.mp3",
                    "bg-slow")).toString()); // replace
            MediaPlayer _mediaPlayer = new MediaPlayer(media);
            // _mediaPlayer.play();

            _stage.setWidth(720);
            _stage.setHeight(439);
            _stage.centerOnScreen();
            setScene(false);
            _stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStartButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("newLoad.fxml"));
            AnchorPane root = loader.load();
            NewLoadController nlc = loader.getController();
            nlc.load(root, _stage); // Pass the stage to the controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleGameSelected() {
        // TODO: load saved game file
        saveLoad.loadSave();
        // TODO: Show saved game options

    }

    @FXML
    public void handleNewGameButtonPressed() {
        // TODO: prompt user for name, etc
        // TODO: create a player
    }

    @FXML
    public void handleInstructionsButtonPressed() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("instructions.fxml"));
            AnchorPane root = loader.load();

            Stage instructionsStage = new Stage();
            instructionsStage.setTitle("Instructions");
            // instructionsStage.initModality(Modality.APPLICATION_MODAL); // Block input to
            // other windows
            instructionsStage.setScene(new Scene(root));

            // Get the exit button from the loaded FXML
            // Button exitButton = (Button) root.lookup("#exitButton");

            // Set action for the exit button
            // exitButton.setOnAction(event -> instructionsStage.close());

            // Show the pop-up window
            // instructionsStage.show();
            instructionsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // this.loadInstructionsScreen();
    }

}
