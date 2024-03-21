package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TripCalculatorTest {
    @Test
    public void Test_CanCreateTripCalculator() {
        TripCalculator calc = new TripCalculator();
    }
    @Test
    public void Test_ReturnsEmptyListIfNoRoutesAvailable() {
        int[] positionA = { 0, 0 };
        int[] positionB = { 2, 2 };
        City city = new City();
        TripCalculator calc = new TripCalculator();
        Trip[] trips = calc.calculateTrips(positionA, positionB, city);
        assertEquals(0, trips.length);
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
