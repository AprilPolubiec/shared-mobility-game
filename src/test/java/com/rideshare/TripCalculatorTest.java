package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TripCalculatorTest {
    private City createTestCity() {
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
            {0, 0, 0, 0, 0, 0, 0, 0},
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
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
        };
        RouteNodeMatrix walkingRoute = new RouteNodeMatrix(walkingMatrix, walkingMatrix, TransportationType.WALKING);
        routes.add(walkingRoute);
        City city = new City(8, routes);
        return city;
    }
   
    @Test
    public void Test_CanCreateTripCalculator() {
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);
    }

    @Test
    public void Test_ReturnsEmptyListIfNoRoutesAvailable() {
        City city = createTestCity();
        TripCalculator calc = new TripCalculator(city);
        ArrayList<Trip> trips = calc.calculateTrips(0, 0, 0, 0);
        assertEquals(0, trips.size());
    }

    @Test
    public void Test_Foo() {
        City city = createTestCity();
        TripCalculator calc = new TripCalculator(city);
        ArrayList<Trip> trips = calc.calculateTrips(0, 0, 0, 0);
        assertEquals(0, trips.size());
    }

    @Test
    public void Test_CanGetShortestWalkingOnlyTrip() {
        int[] positionA = { 0, 0 };
        int[] positionB = { 2, 2 };
        City city = new City();
        /**
         * TODO: city should look like
         * [
         *  [0,0,0],
         *  [0,0,0],
         *  [0,0,0]
         * ]
         */
        TripCalculator calc = new TripCalculator();
        Trip[] trips = calc.calculateTrips(positionA, positionB, city);
        assertEquals(1, trips.length);
        // TODO: not sure how we represent trips yet, but it should be (0,0), (1,2), (2,2)
        throw new UnsupportedOperationException("Need to implement routes/trips");
    }

    @Test
    public void Test_CanGetSingleBusTrip() {
        int[] positionA = { 0, 0 };
        int[] positionB = { 2, 2 };
        City city = new City();
        /**
         * TODO:
         * Route should be a single bus route that looks like
         * [
         *  [1, 1, 1],
         *  [1, 0, 0],
         *  [1, 0, 0],
         * ]
         * And has stops at 0,0 and 2,2
         */
        Route[] routes = { new Route() };
        city.addRoutes(routes);
        TripCalculator calc = new TripCalculator();
        Trip[] trips = calc.calculateTrips(positionA, positionB, city);
        assertEquals(1, trips.length);
    }

    @Test
    public void Test_ExcludesRoutesWithNoValidStops() {
        int[] positionA = { 0, 0 };
        int[] positionB = { 2, 2 };
        City city = new City();
        /**
         * TODO:
         * busRoute should be a bus route that looks like
         * [
         *  [1, 1, 1],
         *  [1, 0, 0],
         *  [1, 0, 0],
         * ]
         * And has stops at 0,0 and 1,2 (player can get on but not off)
         */
        Route busRoute = new Route();

        /**
         * TODO:
         * trainRoute should be a train route that looks like
         * [
         *  [1, 1, 1],
         *  [1, 0, 0],
         *  [1, 0, 0],
         * ]
         * And has stops at 0,2 and 2,2 (player can get off but not on)
         */
        Route trainRoute = new Route();
        // Player can always go on walking route
        Route walkingRoute = new Route(); // Do we even create a Route for these?
        Route[] routes = { busRoute, trainRoute, walkingRoute };
        city.addRoutes(routes);
        TripCalculator calc = new TripCalculator();
        Trip[] trips = calc.calculateTrips(positionA, positionB, city);
        assertEquals(1, trips.length); // Only the walking route
    }

    @Test
    public void Test_MergesRoutesBasedOnValidStops() {
        throw;
        int[] positionA = { 0, 0 };
        int[] positionB = { 2, 2 };
        City city = new City();
        /**
         * TODO:
         * busRoute should be a bus route that looks like
         * [
         *  [1, 1, 1],
         *  [1, 0, 0],
         *  [1, 0, 0],
         * ]
         * And has stops at 0,0 and 1,2 (player can get on but not off)
         */
        Route busRoute = new Route();

        /**
         * TODO:
         * trainRoute should be a train route that looks like
         * [
         *  [1, 1, 1],
         *  [1, 0, 0],
         *  [1, 0, 0],
         * ]
         * And has stops at 0,2 and 2,2 (player can get off but not on)
         */
        Route trainRoute = new Route();
        // Player can always go on walking route
        Route walkingRoute = new Route();
        Route[] routes = { busRoute, trainRoute, walkingRoute };
        city.addRoutes(routes);
        TripCalculator calc = new TripCalculator();
        Trip[] trips = calc.calculateTrips(positionA, positionB, city);
        assertEquals(1, trips.length); // Only the walking route
    }
}
