package com.rideshare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import com.rideshare.GameManager.GameController;
import com.rideshare.GameManager.Sprite;

/**
 * JavaFX App
 */
public class App extends Application {
    private GameController gameController;

    @Override
    public void start(Stage stage) throws IOException {
        this.gameController = new GameController();
        gameController.initialize(stage);
        gameController.loadHomeScreen(stage);

        Sprite s = new Sprite("girl-1", this.gameController);
        s.render();
    }

    public static void main(String[] args) {
        launch(args);
    }

}