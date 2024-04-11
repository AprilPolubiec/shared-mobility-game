package com.rideshare.Controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import com.rideshare.City;
import com.rideshare.Mailbox;
import com.rideshare.MailboxStatus;
import com.rideshare.Timer;
import com.rideshare.TimerState;
import com.rideshare.GameManager.Game;
import com.rideshare.GameManager.MapLoader;
import com.rideshare.GameManager.Sprite;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    @FXML
    public javafx.scene.control.Button startButton;

    private City _city;
    private AnchorPane _root;
    private Timer _timer;
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
            loadMap("level-1");
            // loadTimer();
            loadProgressModal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void loadTimer() {
    //     _timer = new Timer();
    //     _timer.render(_root);
    // }

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
        Sprite player = new Sprite("girl-1", _stage);
        Game game = new Game(_root, _city, player);
        game.start();
        // try {
        //     Sprite player = new Sprite("girl-1", _stage);
        //     player.render();
        //     Timer t = new Timer(_clockText);
        //     t.start();

        //     Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
        //         showRandomMailbox(player);
        //     }));
        //     timeline.setCycleCount(Animation.INDEFINITE);
        //     timeline.playFromStart();
        // } catch (Exception e) {
        //     // TODO: handle exception
        //     e.printStackTrace();
        // }

    }

    private void showRandomMailbox(Sprite player) {
        int numMailboxes = _city.getUninitializedMailboxes().size();
        int randomMailboxIndex = new Random().nextInt(numMailboxes);
        Mailbox currentMailbox = _city.getUninitializedMailboxes().get(randomMailboxIndex);

        currentMailbox.setDuration(5);
        currentMailbox.render(player);
        currentMailbox.markWaiting();
        currentMailbox.show();
    }

}
