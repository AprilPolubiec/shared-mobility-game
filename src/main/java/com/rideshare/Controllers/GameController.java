package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.City.City;
import com.rideshare.GameManager.Game;
import com.rideshare.GameManager.MapLoader;
import com.rideshare.GameManager.Player;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GameController {
    @FXML
    public javafx.scene.control.Button startButton;

    private City _city;
    private AnchorPane _root;
    private Stage _stage;

    private Player player;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setPlayer(Player player) {
        this.player = player;
        this.loadMap(player.getScoreKeeper().getMapName());
    }

    private void loadMap(String mapName) {
        MapLoader loader = new MapLoader(_stage.getScene());
        loader.load(mapName);
        _city = loader.getCity();
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
        // Player player = new Player("april", "girl-1");

        Game game = new Game(_root, _city, player);
        game.start();
    }


}
