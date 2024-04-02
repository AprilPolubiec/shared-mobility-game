package com.rideshare.GameManager;

import java.io.IOException;

import com.rideshare.App;
import com.rideshare.City;
import com.rideshare.Trip;
import com.rideshare.TripCalculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class which manages the game GUI
 */
public class GameController {
    @FXML
    private static Scene _scene;
    private static Stage _stage;
    private static AnchorPane _root = new AnchorPane();
    private static Boolean isPaused;
    MediaPlayer _bgMusic;

    @FXML
    public javafx.scene.control.Button loadGameButton;
    @FXML
    public javafx.scene.control.Button newGameButton;
    @FXML
    public javafx.scene.control.Button startGameButton;
    @FXML
    public javafx.scene.control.Button instructionsButton;

    public Scene get_scene() {
        return _scene;
    }

    public AnchorPane get_root() {
        return _root;
    }

    /**
     * @param stage
     *              Sets up the general configuration for the game window (heigh,
     *              width, etc) and renders
     */
    private void renderStage(Stage stage) {
        _stage = stage;
        _scene = new Scene(_root);
        stage.setTitle("Shared Mobility App");
        stage.setScene(_scene);
        stage.show();
    }

    // Function which sets up the initial variables and elements for the game.
    public void initialize(Stage stage) {
        this.renderStage(stage);
    }

    // region Screen loading functions
    private static void setScene(String fxml, boolean isFullScreen) throws IOException {
        _root = loadFXML(fxml);
        _scene.setRoot(_root);
        _stage.setScene(_scene);
        _stage.setFullScreen(isFullScreen);
    }

    private static AnchorPane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void loadHomeScreen() {
        try {
            Media media = new Media(App.class.getResource("/images/audio/bg-slow.mp3").toString()); // replace
                                                                                                    // /Movies/test.mp3
                                                                                                    // with your file
            _bgMusic = new MediaPlayer(media);
            // TODO: add mute button
            // _bgMusic.play();

            _stage.setWidth(720);
            _stage.setHeight(439);
            _stage.centerOnScreen();
            // _stage.setFullScreen(false);
            setScene("home", false);
            // _scene.getStylesheets().add(App.class.getResource("/styles/homeScreen.css").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGameScreen() {
        try {
            setScene("game", true);
            // Load player's game
            // Load image
            MapLoader loader = new MapLoader(_scene);
            loader.load("test-map-large");
            City city = loader.getCity();
            city.showAllMailboxes();
            
            // TripCalculator tc = new TripCalculator(city);
            // tc.calculateTrips(0, 15, 3, 15);
            // System.out.println("Got the city!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInstructionsScreen() {
        try {
            setScene("instructions", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion

    public void start() {
        System.out.println("Starting");
        //
        // Sprite player = new Sprite("April", this);
        // player.render();
        // Load city (using City class)
        // Generate mailboxes (using City class)
        // Create ScoreKeeper (using ScoreKeeper class)
        // Start timer (using Timer class)
        // Randomly show x mailboxes (eg: 1 if user is on level 1) - should loop through
        // the mailboxes on our City instance that are not completed or unavailable
        // Calculate trips (using TripCalculator)
        // Show trips in popup (Using ChooseTripPopup)
        // on trip selection, "do" the trip - TBD?
    }

    // region button press handlers
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

    // endregion

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
