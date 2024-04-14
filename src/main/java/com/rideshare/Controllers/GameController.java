package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.City;
import com.rideshare.Player;
import com.rideshare.GameManager.Game;
import com.rideshare.GameManager.MapLoader;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameController {
    @FXML
    public javafx.scene.control.Button startButton;

    private City _city;
    private AnchorPane _root;
    private Stage _stage;

    public GameController() {
        return;
    }

    protected AnchorPane setScene(boolean isFullScreen) throws IOException {
        Scene scene = _stage.getScene();
        scene.setRoot(_root);
        _stage.setFullScreen(isFullScreen);
        return _root;
    }

    void load(AnchorPane root, Stage stage) {
        try {
            _stage = stage;
            _root = root;
            setScene(true);
            loadMap("level-2");
            // loadTimer();
            loadProgressModal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadMap(String mapName) {
        // TODO: should be able to load different maps eventually
        MapLoader loader = new MapLoader(_stage.getScene());
        loader.load(mapName);
        _city = loader.getCity();
    }

    // TODO: create a ProgressBar class!
    private void loadProgressModal() {
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setProgress(0.5);

        VBox vBox = new VBox(progressBar);
        progressBar.setPrefWidth(960);
        progressBar.setPrefHeight(50);

        progressBar.setStyle("-fx-accent: #fa8132;");
        AnchorPane.setBottomAnchor(vBox, 0.0);
        _root.getChildren().add(vBox);
    }

    @FXML
    public void start() {
        System.out.println("Starting");
        AnchorPane anchorPane = (AnchorPane) _stage.getScene().lookup("#introModal");
        if (anchorPane != null) {
            // Hide the AnchorPane
            anchorPane.setVisible(false);
        } else {
            System.out.println("AnchorPane not found!");
        }
        Player player = new Player("april", "girl-1");

        Game game = new Game(_root, _city, player);
        game.start();
    }


}
