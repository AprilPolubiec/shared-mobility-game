package com.rideshare.GameManager;

import java.io.IOException;

import com.rideshare.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class which manages running the game.
 */
public class GameController {
    // Attributes
    // Methods
    // startGame
    // initializeGame
    // pauseGame
    @FXML
    private static Scene scene;

    private void AddSprite(Stage stage) {
        Image icon = new Image(App.class.getResource("/images/girl-1.png").toString());
        stage.getIcons().add(icon);
    }

    private void DrawScene(Stage stage) {
        Group root = new Group();
        scene = new Scene(root, Color.BLACK);
        
        stage.setTitle("Shared Mobility App");
        stage.setWidth((900));
        stage.setHeight((600));
        stage.setResizable(false);

        // stage.setFullScreen(true);

        stage.setScene(scene);
    }

    @FXML
    public void LoadHomeScreen(Stage stage) throws IOException {
        try {
            App.setRoot(scene, "home");
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void Show(Stage stage) {
        stage.show();
    }

    // Function which sets up the initial variables and elements for the game.
    public void InitializeGame(Stage stage) {
        // Create a scene
        this.DrawScene(stage);
        // Load the home screen
        this.Show(stage);
    }

    public void RunGame() {
       
    }
}
