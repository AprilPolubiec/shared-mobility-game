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
    //Instance Field Declarations
    private final RouteNodeMatrix nodeMatrix;
    private final TransportationType transportationType;
    private final String name;

    //Class Constructor
    public Route(int[][] routeMatrix, TransportationType transportationType, String name) {
        nodeMatrix = new RouteNodeMatrix(routeMatrix, transportationType, name);
        this.transportationType = transportationType;
        this.name = name;
    }

    //Class Getter Methods
    public String getName() {
        return this.name;
    }

    /**
     * Get the transportation node matrix for this route
     * @return RouteNodeMatrix
     */
    public RouteNodeMatrix getRouteNodeMatrix() {
        return nodeMatrix;
    }

    /**
     * Get transportation type of this route
     * @return TransportationType
     */
    public TransportationType getTransportationType() {
        return this.transportationType;
    }
}