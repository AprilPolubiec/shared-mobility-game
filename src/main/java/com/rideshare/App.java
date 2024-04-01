package com.rideshare;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
        gameController.loadHomeScreen();

        // Sprite s = new Sprite("girl-1", this.gameController);
        // s.render();
        // City city = createCity();
        // TripCalculator tc = new TripCalculator(city, this.gameController);
        // tc.calculateTrips(4, 0, 4, 7);
    }

    public static void main(String[] args) {
        launch(args);
    }

}