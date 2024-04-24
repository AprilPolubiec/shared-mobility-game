package com.rideshare.Trip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rideshare.City.RouteNodeMatrix;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TransportationMode.TransportationType;
public class TripTest {
    // TODO: calculations have changed
    @Test
    public void Test_CanCreateTrip() {
        // Create simple node list of walking
        TransportationNode node1 = new TransportationNode(new GridPanePosition(3, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Walking"));
        TransportationNode node2 = new TransportationNode(new GridPanePosition(2, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Walking"));
        TransportationNode node3 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Walking"));
        TransportationNode node4 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Walking"));
        node4.parent = node3;
        node3.parent = node2;
        node2.parent = node1;

        Trip t = new Trip(node4, node1, TripType.EFFICIENT);
        // 4 walking nodes = 0.25 (tile = .25km) * 4 km at 5km/hr
        assertEquals((1.0 / 5.0) * 60.0, t.getDuration());
        assertEquals(1, t.getDistance());
        assertEquals(0, t.getEmission());
    }

    @Test
    public void Test_CanCalculateMultipleTransportationTypes() {
        // Create simple node list of walking
        TransportationNode node1 = new TransportationNode(new GridPanePosition(3, 0), TransportationType.WALKING, new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Test"));
        TransportationNode node11 = new TransportationNode(new GridPanePosition(3, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Test"));
        node11.parent = node1;
        TransportationNode node2 = new TransportationNode(new GridPanePosition(2, 0), TransportationType.TRAIN, new RouteNodeMatrix(new int[5][5], TransportationType.TRAIN, "Test"));
        node2.parent = node11;
        TransportationNode node21 = new TransportationNode(new GridPanePosition(2, 0), TransportationType.TRAIN, new RouteNodeMatrix(new int[5][5], TransportationType.TRAIN, "Test"));
        node21.parent = node2;
        TransportationNode node3 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.BUS, new RouteNodeMatrix(new int[5][5], TransportationType.BUS, "Test"));
        node3.parent = node21;
        TransportationNode node31 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.BUS, new RouteNodeMatrix(new int[5][5], TransportationType.BUS, "Test"));
        node31.parent = node3;
        TransportationNode node4 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.CAR, new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Test"));
        node4.parent = node31;
        TransportationNode node41 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.CAR, new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Test"));
        node41.parent = node4;

        Trip t = new Trip(node41, node1, TripType.EFFICIENT);
        
        // Walking = 5km/hr = 0.5 / 5 * 60 = 6
        // Train = 30 km/hr = 0.5 / 30 * 60 = 1
        // Bus = 40 km/hr = 0.5 / 40 * 60 = 0.5 // maybe?
        // Car = 48 km/hr = 0.5 / 48 * 60 = 0.625

        // 2km = 6 + 1 + 0.5 + 0.25 = 7.75
        assertEquals(Math.round(7.75), t.getDuration());
        assertEquals(2.0, t.getDistance());

        // Walking = 0/km
        // Train = 35/km
        // Bus = 105/km
        // Car = 192/km
        // 0 + 17.5 + 52.5 + 96
        assertEquals(166.0, t.getEmission());
    }
}