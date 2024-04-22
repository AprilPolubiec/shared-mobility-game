package com.rideshare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.rideshare.AudioManager.AudioManager;
import com.rideshare.Controllers.HomeController;

/**
 * JavaFX App
 */
public class App extends Application {
    AudioManager backgroundAudio;
    @Override
    public void start(Stage stage) throws IOException {
        this.backgroundAudio = new AudioManager();
        this.backgroundAudio.playBackgroundMusic("bg-fast");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home.fxml"));
        AnchorPane root = fxmlLoader.load();
        HomeController hc = fxmlLoader.getController();
        hc.load(stage, root, backgroundAudio);
    }

    public static void main(String[] args) {
        launch(args);
    }

}