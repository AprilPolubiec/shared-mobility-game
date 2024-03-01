package com.rideshare.GameManager;

import java.io.IOException;

import com.rideshare.App;
import com.rideshare.Trip;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Class which manages the game GUI
 */
public class GameController {
    @FXML
    private static Scene scene;
    private static AnchorPane root = new AnchorPane();
    private static boolean isPaused;

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
       Sprite player = new Sprite("April", this);
       player.render();
       // Load city (using City class)
       // Generate mailboxes (using City class)
       // Create ScoreKeeper (using ScoreKeeper class)
       // Start timer (using Timer class)
       // Randomly show x mailboxes (eg: 1 if user is on level 1) - should loop through the mailboxes on our City instance that are not completed or unavailable
       // Calculate trips (using TripCalculator)
       // Show trips in popup (Using ChooseTripPopup)
       // on trip selection, "do" the trip - TBD?
    }

    public void loadCity(int difficulty) {
        // this.addMailboxes(...);
       return;
    }

    public void addMailboxes(int amount) {
       return;
    }

    public void createScoreKeeper(int budget) {
       return;
    }

    public void startTimer() {
       return;
    }

    public void showMailboxes(int count) {
       return;
    }

    public void calculateTrips(int count) {
       return;
    }

    public void renderEducationalPopup(Trip[] trips) {
       return;
    }

    @FXML
    public void takeTrip(Trip trip) {
       // Character should visually move along the trip route
        // On arrival, educational popup should show
    }

    public void showEducationalPopup(Trip[] trips) {
       // EducationalPopup.show(trips)
    }

    @FXML
    public void load() {
       // Import file
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
        isPaused = true;
    }

    @FXML
    public void resume() {
        System.out.println("Resuming");
        isPaused = false;
    }
}
