package com.rideshare.Trip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rideshare.City.RouteNodeMatrix;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TransportationMode.TransportationType;

public class TransportationNodeTest {
    @Test
    public void Test_CanCalculateUnweightedCosts() {
        TransportationNode node1 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Whatever"));
        TransportationNode node2 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Whatever"));
        TransportationNode node3 = new TransportationNode(new GridPanePosition(2, 2), TransportationType.WALKING,
                new RouteNodeMatrix(new int[5][5], TransportationType.WALKING, "Whatever"));
        node2.getCost(node1, node3, TripType.EFFICIENT);
        assertEquals(1, node2.gCost); // Distance from node1 to node2
        assertEquals(3, node2.hCost); // Distance from node 2 to node3
        assertEquals(4, node2.fCost); // Sum of g and h
    }

    @Test
    public void Test_CanCalculateCostsWeightedByEmissions() {
        TransportationNode node1 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        TransportationNode node2 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        TransportationNode node3 = new TransportationNode(new GridPanePosition(2, 2), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        node2.getCost(node1, node3, TripType.EFFICIENT);

        assertEquals(1, node2.gCost); // Distance from node1 to node2
        assertEquals(195, node2.hCost); // Distance from node 2 to node3 plus emissions (192)
        assertEquals(196, node2.fCost); // Sum of g and h
    }
    @Test
    public void Test_CanCalculateCostsWeightedBySpeed() {
        TransportationNode node1 = new TransportationNode(new GridPanePosition(0, 0), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        TransportationNode node2 = new TransportationNode(new GridPanePosition(1, 0), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        TransportationNode node3 = new TransportationNode(new GridPanePosition(2, 2), TransportationType.CAR,
                new RouteNodeMatrix(new int[5][5], TransportationType.CAR, "Whatever"));
        node2.getCost(node1, node3, TripType.FAST);

        assertEquals(1, node2.gCost); // Distance from node1 to node2
        assertEquals(-45, node2.hCost); // Distance from node 2 to node3 minus speed (48)
        assertEquals(-44, node2.fCost); // Sum of g and h
    }
}
