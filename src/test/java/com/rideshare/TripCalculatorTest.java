package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.rideshare.GameManager.MapLoader;
import com.rideshare.TileManager.MapJson;
import com.rideshare.TileManager.TiledMapLayer;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class TripCalculatorTest {
    private City createTestCity() {
        try {
            MapJson mapJson = MapLoader.getMapDataFromFile("level-1");
            City c = MapLoader.createCityFromMapData(mapJson);
            return c;
        } catch (Exception e) {
            System.out.println("Failed to make test city");
            return null;
        }
    }
   
    @Test
    public void Test_CanCreateTripCalculator() {
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);
        assertEquals(30, calc.cityHeight);
        assertEquals(30, calc.cityWidth);
        assertEquals(c, calc.city);
    }

    @Test
    public void Test_CanGetStartNode() {
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);
        TransportationNode startNode = calc.getStartNode(0, 0);
        assertEquals(TransportationType.WALKING, startNode.transportationType);
    }

    @Test
    public void Test_CanGetGoalNode() {
        // Given a house position, should be able to get the goal node
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);

        // For the house at 12,16, the goal node is to the left
        TransportationNode goalNode = calc.getGoalNode(12, 16);
        assertEquals(12, goalNode.row);
        assertEquals(15, goalNode.col);

        // For the house at 10,13, the goal node is below
        goalNode = calc.getGoalNode(10, 13);
        assertEquals(11, goalNode.row);
        assertEquals(13, goalNode.col);
    }

    @Test
    public void Test_CanGetClosestStartStation() {
        // Can get the closest start station to [15,15]
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);
        Route busRoute = c.routes.get(2);
        assertEquals(TransportationType.BUS, busRoute.getTransportationType());
        assertEquals("39A", busRoute.name);
        TransportationNode startNode = calc.getStartNode(15, 15);

        TransportationNode startStation = calc.getClosestStation(startNode, null, null);
        assertEquals(TransportationType.BUS, startStation.transportationType);
        assertEquals("39A", startStation.modeOfTransport.getName());
        assertEquals(16, startStation.row);
        assertEquals(15, startStation.col);
    }

    @Test
    public void Test_CanGetClosestEndStation() {
        // Can get the closest start station to [15,15]
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);
        Route busRoute = c.routes.get(2);
        assertEquals(TransportationType.BUS, busRoute.getTransportationType());
        assertEquals("39A", busRoute.name);
        TransportationNode goalNode = calc.getGoalNode(19, 16);

        TransportationNode endStation = calc.getClosestStation(goalNode, TransportationType.BUS, "39A");
        assertEquals(TransportationType.BUS, endStation.transportationType);
        assertEquals("39A", endStation.modeOfTransport.getName());
        assertEquals(16, endStation.row);
        assertEquals(15, endStation.col);
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
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);

        // House is at 11, 3; target tile is 12, 3
        TransportationNode goalNode = calc.getGoalNode(11, 3);
        assertEquals(12, goalNode.row);
        assertEquals(3, goalNode.col);
        assertEquals(TransportationType.WALKING, goalNode.transportationType);

        TransportationNode startNode = calc.getStartNode(20, 19);
        assertEquals(20, startNode.row);
        assertEquals(19, startNode.col);
        assertEquals(TransportationType.WALKING, startNode.transportationType);
        
        TransportationNode startStation = calc.getClosestStation(startNode, null, null);
        assertEquals(20, startStation.row);
        assertEquals(16, startStation.col);
        assertEquals("119", startStation.modeOfTransport.getName());
        
        
        TransportationNode endStation = calc.getClosestStation(goalNode, startStation.transportationType, startStation.modeOfTransport.getName());
        assertEquals(20, endStation.row);
        assertEquals(16, endStation.col);
        assertEquals("119", endStation.modeOfTransport.getName());
    }

    @Test
    public void Test_CanPassAStop() {
        City c = createTestCity();
        TripCalculator calc = new TripCalculator(c);

        // House is at 21, 10; target tile is 22, 10
        TransportationNode goalNode = calc.getGoalNode(21, 10);
        assertEquals(22, goalNode.row);
        assertEquals(10, goalNode.col);
        assertEquals(TransportationType.WALKING, goalNode.transportationType);

        TransportationNode startNode = calc.getStartNode(10, 28);
        assertEquals(10, startNode.row);
        assertEquals(28, startNode.col);
        assertEquals(TransportationType.WALKING, startNode.transportationType);
        
        TransportationNode startStation = calc.getClosestStation(startNode, null, null);
        assertEquals(8, startStation.row);
        assertEquals(21, startStation.col);
        assertEquals("Blue Line", startStation.modeOfTransport.getName());
        
        
        TransportationNode endStation = calc.getClosestStation(goalNode, startStation.transportationType, startStation.modeOfTransport.getName());
        assertEquals(23, endStation.row);
        assertEquals(8, endStation.col);
        assertEquals("Blue Line", endStation.modeOfTransport.getName());
        
        calc.currentRouteMatrix = startNode.routeMatrix;
        Trip middleLeg = calc.runPathFinding(TripType.TRANSIT_ONLY, startStation, startStation, endStation);
        assertEquals(31, middleLeg.getNodeList().size());
    }
}
