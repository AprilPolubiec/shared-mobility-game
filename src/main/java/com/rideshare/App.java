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
        gameController.loadGameScreen();

        // Sprite s = new Sprite("girl-1", this.gameController);
        // s.render();
        // City city = createCity();
        // TripCalculator tc = new TripCalculator(city, this.gameController);
        // tc.calculateTrips(4, 0, 4, 7);
    }

    private City createCity() {
        ArrayList<RouteNodeMatrix> routes = new ArrayList<RouteNodeMatrix>();
        int[][] busMatrix = {
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
          };
        int[][] busStopMatrix = {
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
          };
        RouteNodeMatrix busRoute = new RouteNodeMatrix(busMatrix, busStopMatrix, TransportationType.BUS);
        routes.add(busRoute);
        int[][] trainMatrix = {
            {0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
          };
          int[][] trainStopMatrix = {
            {0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
          };
          RouteNodeMatrix trainRoute = new RouteNodeMatrix(trainMatrix, trainStopMatrix, TransportationType.TRAIN);
          routes.add(trainRoute);
        int[][] carMatrix = {
            {1, 0, 1, 0, 1, 0, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 1, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
            {1, 1, 1, 0, 1, 0, 1, 0},
            {1, 0, 1, 0, 1, 0, 1, 0},
          };
          RouteNodeMatrix carRoute = new RouteNodeMatrix(carMatrix, carMatrix, TransportationType.CAR);
          routes.add(carRoute);

        int[][] walkingMatrix = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
        };
        RouteNodeMatrix walkingRoute = new RouteNodeMatrix(walkingMatrix, walkingMatrix, TransportationType.WALKING);
        routes.add(walkingRoute);
        City city = new City(8, routes);
        return city;
    }

    public static void main(String[] args) {
        launch(args);
    }

}