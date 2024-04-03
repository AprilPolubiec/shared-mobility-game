package com.rideshare;

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
    private int tripDuration; // In game time minutes
    private int tripDistance; // In km
    private int tripEmission; // In /km (gallons?)
    private TransportationNode startNode;
    private TransportationNode endNode;

    private int _score;

    public Trip(TransportationNode endNode, TransportationNode startNode, TripType tripType) {
        System.out.println(String.format("Creating %s trip", tripType.name()));
        this.endNode = endNode;
        this.startNode = startNode;
        TransportationNode current = endNode;
        int currentLegDistance = 0;
        int currentLegDuration = 0;
        int currentLegEmission = 0;
        while (current != startNode) {
            if (current.parent != null) {
                System.out.println(String.format("[%s, %s] %s", current.row, current.col, current.transportationType.name()));
                if (current.transportationType != current.parent.transportationType) {
                    this.tripDuration += currentLegDuration;
                    this.tripDistance += currentLegDistance;
                    this.tripEmission += currentLegEmission;
                    currentLegDistance = 0;
                    currentLegEmission = 0;
                }
                current = current.parent;
                currentLegDistance += 1; // Each node = 1km
                currentLegDuration += current.modeOfTransport.getSpeed() / 60;
                currentLegEmission += current.modeOfTransport.getEmissionRate() / 60; // Emission rate is in km/hr
            }
        }

        System.out.println(String.format("%s Trip\n Duration: %s Distance: %s Emission: %s", tripType.name(),
                tripDuration, tripDistance, tripEmission));
    }

    public int getScore() {
        return this._score;
    }

    public int getDuration() {
        return this.tripDuration;
    }

    public int getDistance() {
        return this.tripDistance;
    }

    public int getEmission() {
        return this.tripEmission;
    }

    // Merges two trips into one
    // TODO - there is no validation that these trips are mergable - PROCEED W
    // CAUTION!
    public void append(Trip trip) {
        trip.startNode.parent = endNode;
        endNode = trip.endNode;

        tripDistance += trip.tripDistance;
        tripDuration += trip.tripDuration;
        tripEmission += trip.tripEmission;
    }

}
