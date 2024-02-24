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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class which manages running the game.
 */
public class GameController {
    @FXML
    private static Scene scene;
    private static AnchorPane root = new AnchorPane();

    public Scene getScene() {
        return scene;
    }
    public AnchorPane getRoot() {
        return root;
    }

    // Creates the basic window of the game
    private void renderStage(Stage stage) {
        scene = new Scene(root);
        stage.setTitle("Shared Mobility App");
        stage.setWidth((900));
        stage.setHeight((600));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void loadHomeScreen(Stage stage) throws IOException {
        try {
            setRoot("home");
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private static void setRoot(String fxml) throws IOException {
        root = loadFXML(fxml);
        scene.setRoot(root);
    }

    private static AnchorPane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Function which sets up the initial variables and elements for the game.
    public void initialize(Stage stage) {
        this.renderStage(stage);
    }

    public void loadGameScreen() throws IOException {
        setRoot("game");;
        // Add avatar
    }

    @FXML
    public void start() {
       System.out.println("Starting");
    }

    @FXML
    public void load() {
       
    }

    @FXML
    public void end() {
       
    }

    @FXML
    public void save() {
       
    }

    @FXML
    public void pause() {
        System.out.println("Pausing");
    }
}
