package com.rideshare;

import java.util.ArrayList;

import javafx.geometry.Point2D;

/**
 * Description: A route is a way to get from point A to point B via a mode of transportation within a city. A route contains stops where a player can get on and off (eg: a bus stop). 
 * The route contains a data structure representing its path throughout the city.
 */
public class Route {
    private RouteNodeMatrix nodeMatrix;
    private TransportationType transportationType;
    private City city;
    public String name;
    
    public Route(int[][] routeMatrix, int[][] stopMatrix, TransportationType transportationType, City city, String name) {
        nodeMatrix = new RouteNodeMatrix(routeMatrix, stopMatrix, transportationType);
        this.transportationType = transportationType;
        this.city = city;
        this.name = name;
    }

    public RouteNodeMatrix getRouteNodes() {
        return nodeMatrix;
    }
    public TransportationType getTransportationMode() {
        return this.transportationType;
    }
}