package com.rideshare;

import java.util.ArrayList;

import javafx.geometry.Point2D;

/**
 * Description: A route is a way to get from point A to point B via a mode of transportation within a city. A route contains stops where a player can get on and off (eg: a bus stop). The route contains a data structure representing its path throughout the city.
    Attributes:
    City (City): The city where this route exists
    Name (String): a name to identify the route by (eg: the Green Line)
    TransportationMode (TransportationMode): the type of transport that runs on this route (eg: bus, train)
    Path: a matrix defining where the route exists within the city
        Example:
        [0, 1, 0]
        [0, 1, 0]
        [0, 1, 0]
    Stops: matrix for the coordinates of eligible places to get on/off the route
        Example:
        [0, 1, 0]
        [0, 0, 0]
        [0, 1, 0]
    Methods:
    getPathCoordinates(): returns list of coordinates that the path is on
    getStopCoordinates(): returns list of coordinates where the stops are
    isStop(posx, posy): returns true if posx, posy exists as a stop of the path
    isRoute(posx, posy): returns true if posx, posy exists on the route’s path

 */
public class Route {
    TransportationNodeMatrix nodeMatrix;
    
    public Route(int[][] routeMatrix, int[][] stopMatrix, TransportationType transportationType) {
        nodeMatrix = new TransportationNodeMatrix(routeMatrix, stopMatrix, transportationType);
    }

    public TransportationNodeMatrix getRouteNodes() {
        return nodeMatrix;
    }
    public boolean isStop(Point2D pos) {
        return true;
    }
    public boolean isRoute(Point2D pos) {
        return true;
    }
    public TransportationMode getTransportationMode() {
        return new TransportationMode();
    }
}