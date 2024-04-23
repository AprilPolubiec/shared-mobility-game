package com.rideshare.City;

import com.rideshare.TransportationMode.TransportationType;

/**
 * Description: A route is a way to get from point A to point B via a mode of
 * transportation within a city. A route contains stops where a player can get
 * on and off (eg: a bus stop).
 * The route contains a data structure representing its path throughout the
 * city.
 */
public class Route {
    private RouteNodeMatrix nodeMatrix;
    private TransportationType transportationType;
    public String name;

    public Route(int[][] routeMatrix, TransportationType transportationType, String name) {
        nodeMatrix = new RouteNodeMatrix(routeMatrix, transportationType, name);
        this.transportationType = transportationType;
        this.name = name;
    }

    public RouteNodeMatrix getRouteNodeMatrix() {
        return nodeMatrix;
    }

    public TransportationType getTransportationType() {
        return this.transportationType;
    }
}