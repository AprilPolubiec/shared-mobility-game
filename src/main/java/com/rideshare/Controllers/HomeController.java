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
            Media media = new
            Media(App.class.getResource(String.format("/images/audio/%s.mp3",
            "bg-slow")).toString()); // replace
            MediaPlayer _mediaPlayer = new MediaPlayer(media);
            _mediaPlayer.play();

            _stage.setWidth(720);
            _stage.setHeight(439);
            _stage.centerOnScreen();
            setScene( false);
            _stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStartButtonPressed() {
        try {
            AnchorPane anchorPane = (AnchorPane) _stage.getScene().lookup("#newLoadGameModal");
        if (anchorPane != null) {
            // Hide the AnchorPane
            anchorPane.setVisible(false);
        } else {
            System.out.println("AnchorPane not found!");
        }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLoadButtonPressed() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Game");
    
       //creating a path to game_data directory where game status' will be saved
        File initialDirectory = new File("/game_data");
        fileChooser.setInitialDirectory(initialDirectory);
    
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Data Files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extensionFilter);
    
       
        File selectedFile = fileChooser.showOpenDialog(null);
    
        if (selectedFile != null) {
            String fileName = selectedFile.getAbsolutePath();
            saveLoad.loadSave(fileName); 
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
        // this.loadInstructionsScreen();
    }

}
