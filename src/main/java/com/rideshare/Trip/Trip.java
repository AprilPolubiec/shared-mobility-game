package com.rideshare.Trip;

import java.util.ArrayList;
import java.util.Collections;

import com.rideshare.TileManager.TileUtils;

/**
 * Description: A trip is a series of Routes that can be taken to get from point
 * A
 * to point B and the statistics associated with taking the trip (carbon, time,
 * length, etc.). The main purpose of this class is to store information about
 * the
 * Trip - it does not handle any visualizations (ie: moving player across the
 * screen, that occurs elsewhere)
 * 
 * Attributes:
 * StartPosition ([int, int]): coordinates of where the trip started
 * EndPosition ([int, int]): coordinates of where the trip ended
 * TripPath (Route[]): the chronologically ordered series of Routes which can be
 * taken in order to get from StartPosition to EndPosition
 * TripLength (int): the time needed overall to complete the route
 * TripDistance (int): the distance between start position and end position
 * TripEmission (int): how much emission will be created upon completion of this
 * route
 * Status (Status): Ready, InProgress, Paused, Complete; current status of the
 * trip
 * Player (Player): the player which is interacting with the trip
 * Methods:
 * StartTrip(): sets the Trip Status to “InProgress”
 * EndTrip(): sets the Trip Status to “Complete”
 * PauseTrip(): sets the Trip Status to Paused
 * GetScore(): Calculates final score of a trip taking into account the time,
 * CO2 emission, etc. - should utilize the TripCalculator
 * CalculateCO2Emission(): Calculates CO2 taken for a trip.
 * GetStatus: Check the status of the trip
 * 
 */

 public class Trip {
    private float tripDuration; // In game time minutes
    private float tripDistance; // In km
    private float tripEmission; // In /km (gallons?)
    private ArrayList<TransportationNode> _nodeList = new ArrayList<>();
    private TripType tripType;

    // private int _score;
    private ArrayList<Trip> legs = new ArrayList<>();

    public Trip(TransportationNode endNode, TransportationNode startNode, TripType tripType) {
        this.tripType = tripType;
        System.out.println(String.format("Creating %s trip", tripType.name()));
        TransportationNode current = endNode;
        float currentLegDistance = 0;
        float currentLegDuration = 0;
        float currentLegEmission = 0;
        int iterations = 0;
        while (current != null & iterations <= 200) {
            this._nodeList.add(current);

            currentLegDistance += TileUtils.TILE_DISTANCE_IN_KM; // Each node = .5km
            currentLegDuration += (TileUtils.TILE_DISTANCE_IN_KM / current.modeOfTransport.getSpeed()) * 60.0; // Number of minutes to go 0.5 km
            currentLegEmission += (float) current.modeOfTransport.getEmissionRate() * TileUtils.TILE_DISTANCE_IN_KM; // Emission rate is in km/hr

            if (current.parent == null || current.transportationType != current.parent.transportationType) {
                this.tripDuration += currentLegDuration;
                this.tripDistance += currentLegDistance;
                this.tripEmission += currentLegEmission;
                currentLegDistance = 0;
                currentLegEmission = 0;
                currentLegDuration = 0;
            }

            current = current.parent;
            iterations++;
            
        }
        Collections.reverse(_nodeList); // Reverse list so its from start to end
        legs.add(this);
    }

    public ArrayList<TransportationNode> getNodeList() {
        return _nodeList;
    }

    // public int getScore() {
    //     return this._score;
    // }
    public TripType getTripType() {
        return this.tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public double getDuration() {
        return Math.floor(this.tripDuration);
    }

    public double getDistance() {
        return Math.floor(this.tripDistance);
    }

    public double getEmission() {
        return Math.floor(this.tripEmission);
    }

    public Trip appendTrip(Trip trip) {
        if (trip == null) {
            return this; // Do nothing
        }
        legs.add(trip);
        this._nodeList.addAll(trip.getNodeList());
        this.tripDistance += trip.tripDistance;
        this.tripDuration += trip.tripDuration;
        this.tripEmission += trip.tripEmission;
        return this;
    }

    public void print() {
        System.out.println(String.format("%s Trip (%s legs)\n Duration: %s Distance: %s Emission: %s", tripType.name(),
                legs.size(),
                tripDuration, tripDistance, tripEmission));
        System.out.println("Trip node list: ");
        for (TransportationNode transportationNode : _nodeList) {
            System.out.println(String.format("[%s, %s] %s", transportationNode.position.row, transportationNode.position.col,
                    transportationNode.transportationType.name()));
        }
    }
}
