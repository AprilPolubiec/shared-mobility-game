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
    private static Scene _scene;
    private static AnchorPane _root = new AnchorPane();
    private static Boolean isPaused;

    @FXML
    private javafx.scene.control.Button loadGameButton; 
    @FXML
    private javafx.scene.control.Button newGameButton; 
    @FXML
    private javafx.scene.control.Button startGameButton; 
    @FXML
    private javafx.scene.control.Button instructionsButton; 

    public Scene get_scene() {
        return _scene;
    }

    public AnchorPane get_root() {
        return _root;
    }

    /** 
     * @param stage
     * Sets up the general configuration for the game window (heigh, width, etc) and renders
     */
    private void renderStage(Stage stage) {
        _scene = new Scene(_root);
        stage.setTitle("Shared Mobility App");
        stage.setWidth((900));
        stage.setHeight((600));
        stage.setResizable(false);
        stage.setScene(_scene);
        stage.show();
    }

    // Function which sets up the initial variables and elements for the game.
    public void initialize(Stage stage) {
        this.renderStage(stage);
    }
  
    //region Screen loading functions
    private static void setRoot(String fxml) throws IOException {
        _root = loadFXML(fxml);
        _scene.setRoot(_root);
    }

    private static AnchorPane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void loadHomeScreen(){
        try {
            setRoot("home");
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void loadGameScreen() {
        try {
            setRoot("game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadInstructionsScreen() {
        try {
            setRoot("instructions");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    public void start() {
        System.out.println("Starting");
        // 
        // Sprite player = new Sprite("April", this);
        // player.render();
        // Load city (using City class)
        // Generate mailboxes (using City class)
        // Create ScoreKeeper (using ScoreKeeper class)
        // Start timer (using Timer class)
        // Randomly show x mailboxes (eg: 1 if user is on level 1) - should loop through the mailboxes on our City instance that are not completed or unavailable
        // Calculate trips (using TripCalculator)
        // Show trips in popup (Using ChooseTripPopup)
        // on trip selection, "do" the trip - TBD?
    }

    //region button press handlers
    @FXML
    public void handleStartButtonPressed() {
        this.loadGameScreen();
        this.start();
    }

    @FXML
    public void handleLoadButtonPressed() {
        // TODO: load saved game file
        // TODO: Show saved game options
    }

    @FXML
    public void handleGameSelected() {
        // TODO: load saved game file
        // TODO: Show saved game options
    }

    @FXML
    public void handleNewGameButtonPressed() {
        // TODO: prompt user for name, etc
        // TODO: create a player
    }

    @FXML
    public void handleInstructionsButtonPressed() {
        this.loadInstructionsScreen();
    }

    //endregion

    public void loadGame() {
        // TODO: find file
        // TODO: if no file, create empty file and start
        // TODO: if file found, create a scorekeeper and load in
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