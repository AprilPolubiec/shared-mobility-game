package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.App;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;

abstract public class Controller {
    // A static stage that will be used by all controllers
    protected static Stage _stage;
    private static MediaPlayer _mediaPlayer;
    private AnchorPane _root = new AnchorPane();
    protected static GameController gameController;
    protected static HomeController homeController;

    public void initialize(Stage stage) {
        if (Controller._stage == null) {
            Controller._stage = stage;
            renderStage();
        }
    }

    abstract void load();

    private void renderStage() {
        Scene scene = new Scene(_root);
        _stage.setTitle("Pedal & Post");
        _stage.setScene(scene);
        _stage.show();
    }

    public void playAudio(String audioName) {
        Media media = new Media(App.class.getResource(String.format("/images/audio/%s.mp3", audioName)).toString()); // replace
        _mediaPlayer = new MediaPlayer(media);
        _mediaPlayer.play();
    }

    public void pauseAudio() {
        if (_mediaPlayer != null) {
            _mediaPlayer.pause();
        }
    }

    public void muteAudio(boolean isMuted) {
        if (_mediaPlayer != null) {
            _mediaPlayer.setMute(isMuted);
        }
    }

    protected void loadScreen(String screenName, boolean isFullScreen) {
        switch (screenName) {
            case "game":
                gameController.load();
                break;
        
            default:
                break;
        }
    }

    protected AnchorPane setScene(String fxml, boolean isFullScreen) throws IOException {
        Scene scene = _stage.getScene();
        AnchorPane root = loadFXML(fxml);
        scene.setRoot(root);
        _stage.setFullScreen(isFullScreen);
        return root;
    }

    private AnchorPane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
