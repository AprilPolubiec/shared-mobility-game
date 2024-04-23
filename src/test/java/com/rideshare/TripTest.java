package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rideshare.TransportationMode.TransportationType;
import com.rideshare.Trip.TransportationNode;
import com.rideshare.Trip.Trip;
import com.rideshare.Trip.TripType;

public class TripTest {
    @Test
    public void Test_CanCreateTrip() {
       // Create simple node list of walking
       TransportationNode node1 = new TransportationNode(3, 0, TransportationType.WALKING, null);
       TransportationNode node2 = new TransportationNode(2, 0, TransportationType.WALKING, null);
       TransportationNode node3 = new TransportationNode(1, 0, TransportationType.WALKING, null);
       TransportationNode node4 = new TransportationNode(0, 0, TransportationType.WALKING, null);
       node4.parent = node3;
       node3.parent = node2;
       node2.parent = node1;

        Trip t = new Trip(node4, node1, TripType.EFFICIENT);

        assertEquals((3 / 5) / 60, t.getDuration());
        assertEquals(4, t.getDistance());
        assertEquals(0, t.getEmission());
    }

    @Test
    public void Test_CanCalculateMultipleTransportationTypes() {
       // Create simple node list of walking
       TransportationNode node1 = new TransportationNode(3, 0, TransportationType.WALKING, null);
       TransportationNode node11 = new TransportationNode(3, 0, TransportationType.WALKING, null);
       node11.parent = node1;
       TransportationNode node2 = new TransportationNode(2, 0, TransportationType.TRAIN, null);
       node2.parent = node11;
       TransportationNode node21 = new TransportationNode(2, 0, TransportationType.TRAIN, null);
       node21.parent = node2;
       TransportationNode node3 = new TransportationNode(1, 0, TransportationType.BUS, null);
       node3.parent = node21;
       TransportationNode node31 = new TransportationNode(1, 0, TransportationType.BUS, null);
       node31.parent = node3;
       TransportationNode node4 = new TransportationNode(0, 0, TransportationType.CAR, null);
       node4.parent = node31;
       TransportationNode node41 = new TransportationNode(0, 0, TransportationType.CAR, null);
       node41.parent = node4;


        Trip t = new Trip(node41, node1, TripType.EFFICIENT);
        // Walking = 5km/hr = 2 / 5 * 60 = 24
        // Train = 30 km/hr = 2 / 30 * 60 =  4
        // Bus = 40 km/hr = 2/40 * 60 = 3
        // Car = 48 km/hr = 2/48 * 60 = 2.5

        // 4km = 24 + 4 + 3 + 2.5 = 33.5
        assertEquals(Math.floor(33.5), t.getDuration());
        assertEquals(8.0, t.getDistance());

        // Walking = 0/km
        // Train = 35/km
        // Bus = 105/km
        // Car = 192/km
        // 0 + 35 + 105 + 192
        assertEquals(332.0 * 2, t.getEmission());
    }
}