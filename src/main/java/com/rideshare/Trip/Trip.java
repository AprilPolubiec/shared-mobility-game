package com.rideshare.Trip;

import java.util.ArrayList;
import java.util.Collections;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.rideshare.TileManager.TileUtils;

/**
 * TODO
 */

public class Trip {
    private static final Logger LOGGER = Logger.getLogger(Trip.class.getName());

    private float tripDuration = 0; // In game time minutes
    private float tripDistance = 0; // In km
    private float tripEmission = 0; // In /km (gallons?)
    private ArrayList<TransportationNode> _nodeList = new ArrayList<>();
    private TripType tripType;

    public Trip(TransportationNode endNode, TransportationNode startNode, TripType tripType) {
        this.tripType = tripType;
        if (startNode == endNode) {
            // Log warning that the user attempted to create a trip with the same start and end node
            LOGGER.log(Level.WARNING, "Attempted to create a trip with the same start and end node");
            return;
        }
        System.out.println(String.format("Creating %s trip", tripType.name()));
        TransportationNode current = endNode;
        float currentLegDistance = 0;
        float currentLegDuration = 0;
        float currentLegEmission = 0;

        // TODO: iterations are here to prevent infinite loop but this is a hacky
        // solution, especially as maps get bigger - temporary
        int iterations = 0;
        while (current != null & iterations <= 200) {
            this._nodeList.add(current);

            currentLegDistance += TileUtils.TILE_DISTANCE_IN_KM; // Each node = .5km
            currentLegDuration += (TileUtils.TILE_DISTANCE_IN_KM / current.modeOfTransport.getSpeed()) * 60.0; // Number
                                                                                                               // of
                                                                                                               // minutes
                                                                                                               // to go
                                                                                                               // 0.25
                                                                                                               // km
            currentLegEmission += (float) current.modeOfTransport.getEmissionRate() * TileUtils.TILE_DISTANCE_IN_KM; // Emission
                                                                                                                     // rate
                                                                                                                     // is
                                                                                                                     // in
                                                                                                                     // km/hr

            if (current.parent == null || current.transportationType != current.parent.transportationType) {
                this.tripDuration += currentLegDuration;
                this.tripDistance += currentLegDistance;
                this.tripEmission += currentLegEmission;
                currentLegDistance = 0;
                currentLegEmission = 0;
                currentLegDuration = 0;
            }

            // Check that the parent is a valid coordinate
            if (current.parent != null && !current.parent.position.isAdjacent(current.position)) {
                // @Sadhbh - great place for a logger warning that a route was invalid -
                // printing out the coordinates would be helpful for debugging too
                // Wipe everything
                tripDuration = 0;
                tripDistance = 0;
                tripEmission = 0;
                _nodeList = new ArrayList<>();
                return;
            }

            current = current.parent;
            iterations++;
        }
        Collections.reverse(_nodeList); // Reverse list so its from start to end
    }

    public ArrayList<TransportationNode> getNodeList() {
        return _nodeList;
    }

    public TripType getTripType() {
        return this.tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    /**
     * Get duration of trip in minutes (game-time)
     * 
     * @return tripDuration
     */
    public double getDuration() {
        return Math.round(this.tripDuration);
    }

    /**
     * Get trip distance in km
     * 
     * @return tripDistance
     */
    public double getDistance() {
        return Math.round(this.tripDistance);
    }

    /**
     * Get trip emission in CO2e/km
     * 
     * @return tripEmission
     */
    public double getEmission() {
        return Math.round(this.tripEmission);
    }

    /**
     * Appends an existing trip to this trip, updating all stats as well. Returns
     * the newly merged trips.
     * 
     * @param trip
     * @return
     */
    public Trip appendTrip(Trip trip) {
        if (trip == null) {
            return this; // Do nothing
        }
        this._nodeList.addAll(trip.getNodeList());
        this.tripDistance += trip.tripDistance;
        this.tripDuration += trip.tripDuration;
        this.tripEmission += trip.tripEmission;
        return this;
    }

    public void print() {
        System.out.println(String.format("%s Trip\n Duration: %s Distance: %s Emission: %s", tripType.name(),
                tripDuration, tripDistance, tripEmission));
        System.out.println("Trip node list: ");
        for (TransportationNode transportationNode : _nodeList) {
            System.out.println(
                    String.format("[%s, %s] %s", transportationNode.position.row, transportationNode.position.col,
                            transportationNode.transportationType.name()));
        }
    }
}
