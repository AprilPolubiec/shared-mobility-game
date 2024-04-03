package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RouteTest {
    @Test
    void Test_CanCreateRoute() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City();
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };
        
        Route r = new Route(city, transportationMode, path, stops, name);

        // TODO: these asserts are most likely checking that the address is the same.
        // If it is failing unexpectedly, that could be why.
        assertEquals(city, r.getCity());
        assertEquals(transportationMode, r.getTransportationType());
        assertEquals(name, r.getName());
        // TODO: check paths
    }

    @Test
    void Test_CanCheckIfStop() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City();
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };
        
        Route r = new Route(city, transportationMode, path, stops, name);
        boolean isStop1 = r.isStop(2, 0);
        boolean isStop2 = r.isStop(0, 0);
        assertTrue(isStop1);
        assertFalse(isStop2);
    }
    
    @Test
    void Test_CanCheckIfIsRoute() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City();
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };
        
        Route r = new Route(city, transportationMode, path, stops, name);
        boolean isRoute1 = r.isRoute(2, 0);
        boolean isRoute2 = r.isRoute(0, 0);
        assertTrue(isRoute1);
        assertFalse(isRoute2);
    }

    @Test
    void Test_CanGetPathCoordinates() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City();
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };
        
        Route r = new Route(city, transportationMode, path, stops, name);
        int[][] path = r.getPathCoordinates();
        assertEquals(5, path.length);
        assertEquals(2, path[0][0]);
        assertEquals(0, path[0][1]);
    }

    @Test
    void Test_CanGetStopCoordinates() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City();
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };
        
        Route r = new Route(city, transportationMode, path, stops, name);
        int[][] stops = r.getStopCoordinates();
        assertEquals(5, stops.length);
        assertEquals(2, stops[0][0]);
        assertEquals(0, stops[0][1]);
    }


    @Test
    void Test_CannotMakeRouteBiggerThanCity() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City(); // TODO: set city dimensions to 5x5
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
        };

        assertThrows(Exception.class, () -> new Route(city, transportationMode, path, stops, name));
    }

    @Test
    void Test_CannotMakeStopsDifferentSizeThanPath() {
        TransportationMode transportationMode = new TransportationMode();
        City city = new City(); // TODO: set city dimensions to 5x5
        String name = "Green Line";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
        };

        assertThrows(Exception.class, () -> new Route(city, transportationMode, path, stops, name));
        
        int[][] path2 = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops2 = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
        };

        assertThrows(Exception.class, () -> new Route(city, transportationMode, path2, stops2, name));
    }

    @Test
    void Test_CannotHaveStopsOnAModeThatDoesNotSupportStops() {
        TransportationMode transportationMode = new WalkingTransportationMode();
        City city = new City(); // TODO: set city dimensions to 5x5
        String name = "Main Street";
        int[][] path = {
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
        };
        int[][] stops = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
        };

        assertThrows(Exception.class, () -> new Route(city, transportationMode, path, stops, name));

    }

}
