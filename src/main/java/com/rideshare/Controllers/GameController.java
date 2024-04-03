package com.rideshare.Controllers;

import java.util.HashMap;

import com.rideshare.City;
import com.rideshare.Timer;
import com.rideshare.GameManager.MapLoader;
import com.rideshare.GameManager.Sprite;

import javafx.fxml.FXML;
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

public class GameController extends Controller {
    @FXML
    public javafx.scene.control.Button startButton;

    private City _city;
    private AnchorPane _root = new AnchorPane();
    private HashMap<String, Font> fonts = new HashMap<String, Font>();
    
    public GameController() {
        this(_stage);
    }

    public GameController(Stage stage) {
        super(stage);
        Controller.gameController = this;
    }

    @Override
    void load() {
        try {
            loadFonts();
            _root = setScene("game", true);
            loadMap("level-1");
            loadTimeModal();
            loadProgressModal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFonts() {
        Font clockFont = Font.loadFont(getClass().getResourceAsStream("/fonts/digital-7.ttf"), 48);
        fonts.put("clock", clockFont);
    }

    private void loadMap(String mapName) {
        // TODO: should be able to load different maps eventually
        MapLoader loader = new MapLoader(_stage.getScene());
        loader.load(mapName);
        _city = loader.getCity();
    }

    private void loadTimeModal() {
        StackPane timeModalRoot = new StackPane();
        ImageView panelImageView = new ImageView(
                new Image(getClass().getResourceAsStream("/images/ui/grey_panel.png")));
        timeModalRoot.getChildren().add(panelImageView);
        AnchorPane.setBottomAnchor(timeModalRoot, 0.0);
        AnchorPane.setRightAnchor(timeModalRoot, 0.0);
        panelImageView.setFitHeight(150);
        panelImageView.setFitWidth(300);

        Text clockText = new Text("00:00AM");
        clockText.setFont(fonts.get("clock"));
        clockText.setFill(javafx.scene.paint.Color.BLACK);
        timeModalRoot.getChildren().add(clockText);

        _root.getChildren().add(timeModalRoot);
    }

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

        // Sprite player = new Sprite("girl-1", this);
        // player.render();
        // Timer t = new Timer(_clockText);
        // t.start();
        // _city.showAllMailboxes();
    }

}
