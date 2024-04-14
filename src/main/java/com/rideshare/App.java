package com.rideshare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.rideshare.Controllers.HomeController;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("home.fxml"));
        AnchorPane root = fxmlLoader.load();
        HomeController hc = fxmlLoader.getController();
        hc.load(stage, root);
    }

    public static void main(String[] args) {
        launch(args);
    }

}