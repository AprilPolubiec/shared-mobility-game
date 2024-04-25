package com.rideshare.Trip;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.rideshare.City.City;
import com.rideshare.City.Route;
import com.rideshare.GameManager.MapLoader;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TileManager.MapJson;
import com.rideshare.TransportationMode.TransportationType;
public class TripCalculatorTest {
    private City createTestCity(String mapName) {
        try {
            MapJson mapJson = MapLoader.getMapDataFromFile(mapName);
            City c = MapLoader.createCityFromMapData(mapJson);
            return c;
        } catch (Exception e) {
            System.out.println("Failed to make test city");
            return null;
        }
    }
   
    @Test
    public void Test_CanCreateTripCalculator() {
        City c = createTestCity("level-1");
        assertDoesNotThrow(() -> new TripCalculator(c));
    }

    @Test
    public void Test_CanGetStartNode() {
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);
        TransportationNode startNode = calc.getStartNode(new GridPanePosition(0, 0));
        assertEquals(TransportationType.WALKING, startNode.transportationType);
    }

    @Test
    public void Test_CanGetGoalNode() {
        // Given a house position, should be able to get the goal node
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);

        // For the house at 12,16, the goal node is to the left
        TransportationNode goalNode = calc.getGoalNode(new GridPanePosition(12, 16));
        assertEquals(12, goalNode.getPosition().row);
        assertEquals(15, goalNode.getPosition().col);

        // For the house at 10,13, the goal node is below
        goalNode = calc.getGoalNode(new GridPanePosition(10, 13));
        assertEquals(11, goalNode.getPosition().row);
        assertEquals(13, goalNode.getPosition().col);
    }

    @Test
    public void Test_CanGetClosestStartStation() {
        // Can get the closest start station to [15,15]
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);
        Route busRoute = c.getRoutes().get(2);
        assertEquals(TransportationType.BUS, busRoute.getTransportationType());
        assertEquals("39A", busRoute.getName());
        TransportationNode startNode = calc.getStartNode(new GridPanePosition(15, 15));

        TransportationNode startStation = calc.getClosestStation(startNode, TransportationType.BUS, "39A");
        assertEquals(TransportationType.BUS, startStation.transportationType);
        assertEquals("39A", startStation.modeOfTransport.getName());
        assertEquals(16, startStation.getPosition().row);
        assertEquals(15, startStation.getPosition().col);
    }

    @Test
    public void Test_CanGetClosestEndStation() {
        // Can get the closest start station to [15,15]
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);
        Route busRoute = c.getRoutes().get(2);
        assertEquals(TransportationType.BUS, busRoute.getTransportationType());
        assertEquals("39A", busRoute.getName());
        TransportationNode goalNode = calc.getGoalNode(new GridPanePosition(19, 16));

        TransportationNode endStation = calc.getClosestStation(goalNode, TransportationType.BUS, "39A");
        assertEquals(TransportationType.BUS, endStation.transportationType);
        assertEquals("39A", endStation.modeOfTransport.getName());
        assertEquals(16, endStation.getPosition().row);
        assertEquals(15, endStation.getPosition().col);
    }

    // This test is based on a real coordinates from  a bug I've been hitting
    // which has been producing this node list:
    // Trip node list: 
    // [20, 19] WALKING
    // [20, 18] WALKING
    // [20, 17] WALKING
    // [20, 16] BUS
    // [18, 3] BUS // Not the big jump here!
    // [18, 3] BUS
    // [18, 2] WALKING
    // [17, 2] WALKING
    // [16, 2] WALKING
    // [15, 2] WALKING
    // [14, 2] WALKING
    // [13, 2] WALKING
    // [12, 2] WALKING
    // [12, 3] WALKING
    // And claiming [20,16] is a stop for 39A when it is not
    // Mailbox at [11, 4]
    // Starting search from [20, 19] to [12, 3]
    @Test
    public void Test_CanGetStartAndEndStationAcrossBusRoutes() {
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);

        // House is at 11, 3; target tile is 12, 3
        TransportationNode goalNode = calc.getGoalNode(new GridPanePosition(11, 3));
        assertEquals(12, goalNode.getPosition().row);
        assertEquals(3, goalNode.getPosition().col);
        assertEquals(TransportationType.WALKING, goalNode.transportationType);

        TransportationNode startNode = calc.getStartNode(new GridPanePosition(20, 19));
        assertEquals(20, startNode.getPosition().row);
        assertEquals(19, startNode.getPosition().col);
        assertEquals(TransportationType.WALKING, startNode.transportationType);
        
        TransportationNode startStation = calc.getClosestStation(startNode, TransportationType.BUS, "119");
        assertEquals(20, startStation.getPosition().row);
        assertEquals(16, startStation.getPosition().col);
        assertEquals("119", startStation.modeOfTransport.getName());
        
        
        TransportationNode endStation = calc.getClosestStation(goalNode, startStation.transportationType, startStation.modeOfTransport.getName());
        assertEquals(20, endStation.getPosition().row);
        assertEquals(16, endStation.getPosition().col);
        assertEquals("119", endStation.modeOfTransport.getName());
    }

    @Test
    public void Test_CanPassAStop() {
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);

        // House is at 21, 10; target tile is 22, 10
        TransportationNode goalNode = calc.getGoalNode(new GridPanePosition(21, 10));
        assertEquals(22, goalNode.getPosition().row);
        assertEquals(10, goalNode.getPosition().col);
        assertEquals(TransportationType.WALKING, goalNode.transportationType);

        TransportationNode startNode = calc.getStartNode(new GridPanePosition(10, 28));
        assertEquals(10, startNode.getPosition().row);
        assertEquals(28, startNode.getPosition().col);
        assertEquals(TransportationType.WALKING, startNode.transportationType);
        
        TransportationNode startStation = calc.getClosestStation(startNode, TransportationType.TRAIN, "Blue Line");
        assertEquals(8, startStation.getPosition().row);
        assertEquals(21, startStation.getPosition().col);
        assertEquals("Blue Line", startStation.modeOfTransport.getName());
        
        
        TransportationNode endStation = calc.getClosestStation(goalNode, startStation.transportationType, startStation.modeOfTransport.getName());
        assertEquals(23, endStation.getPosition().row);
        assertEquals(8, endStation.getPosition().col);
        assertEquals("Blue Line", endStation.modeOfTransport.getName());
        
        // TODO: this is no bueno
        // calc.currentRouteMatrix = startNode.routeMatrix;
        // Trip middleLeg = calc.runPathFinding(TripType.TRANSIT_ONLY, startStation, startStation, endStation);
        // assertEquals(31, middleLeg.getNodeList().size());
    }

    @Test
    public void Test_TransitStationIsAlsoGoalNode() {
        City c = createTestCity("level-1");
        TripCalculator calc = new TripCalculator(c);
        ArrayList<Trip> trips = calc.calculateTrips(new GridPanePosition(20, 17), new GridPanePosition(27, 18));
        assertEquals(14, trips.get(0).getNodeList().size());
    }
}


// TODO: just remove this train stop - too wonky
// Mailbox at [6, 5]
// Starting search from [13, 13] to [7, 4]
// Start station: 39A [14, 16]
// End station: 39A [12, 7]
// Creating EFFICIENT trip
// Creating EFFICIENT trip
// EFFICIENT Trip (1 legs)
//  Duration: 51.3125 Distance: 4.5 Emission: 48.0
// Trip node list: 
// [13, 13] WALKING
// [14, 13] CAR
// [14, 12] WALKING
// [14, 11] WALKING
// [14, 10] WALKING
// [14, 9] WALKING
// [14, 8] WALKING
// [14, 7] WALKING
// [13, 7] WALKING
// [12, 7] WALKING
// [11, 7] WALKING
// [10, 7] WALKING
// [9, 7] WALKING
// [8, 7] WALKING
// [7, 7] WALKING
// [7, 6] WALKING
// [7, 5] WALKING
// [7, 4] WALKING


// FAST Trip (1 legs)
//  Duration: 26.8125 Distance: 17.0 Emission: 3128.75
// Trip node list: 
// [3, 4] WALKING
// [3, 5] CAR
// [3, 6] CAR
// [3, 7] CAR
// [3, 8] CAR
// [3, 9] CAR
// [3, 10] CAR
// [3, 11] CAR
// [3, 12] CAR
// [3, 13] CAR
// [4, 13] CAR
// [5, 13] CAR
// [6, 13] CAR
// [7, 13] CAR
// [8, 13] CAR
// [9, 13] CAR
// [9, 14] CAR
// [9, 15] CAR
// [9, 16] CAR
// [9, 17] CAR
// [9, 18] CAR
// [9, 19] CAR
// [9, 20] CAR
// [9, 21] CAR
// [10, 21] CAR
// [11, 21] CAR
// [12, 21] CAR
// [13, 21] CAR
// [14, 21] CAR
// [15, 21] CAR
// [15, 20] CAR
// [15, 19] CAR
// [15, 18] CAR
// [15, 17] CAR
// [16, 17] TRAIN
// [17, 17] CAR
// [17, 16] CAR
// [17, 15] CAR
// [17, 14] CAR
// [17, 13] CAR
// [17, 12] CAR
// [17, 11] CAR
// [18, 11] CAR
// [19, 11] CAR
// [20, 11] CAR
// [21, 11] CAR
// [22, 11] CAR
// [23, 11] CAR
// [24, 11] CAR
// [25, 11] CAR
// [26, 11] CAR
// [27, 11] CAR
// [28, 11] CAR
// [28, 12] CAR
// [28, 13] CAR
// [28, 14] CAR
// [28, 15] CAR
// [28, 16] CAR
// [28, 17] CAR
// [28, 18] CAR
// [28, 19] CAR
// [28, 20] CAR
// [28, 21] CAR
// [28, 22] CAR
// [28, 23] CAR
// [28, 24] CAR
// [28, 25] CAR
// [24, 2] WALKING