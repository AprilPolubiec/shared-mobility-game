package com.rideshare;
/**
    Description: A trip is a series of Routes that can be taken to get from point A 
    to point B and the statistics associated with taking the trip (carbon, time, 
    length, etc). The main purpose of this class is to store information about the 
    Trip - it does not handle any visualizations (ie: moving player across the 
    screen, that occurs elsewhere)

    Attributes:
        StartPosition ([int, int]): coordinates of where the trip started
        EndPosition ([int, int]): coordinates of where the trip ended
        TripPath (Route[]): the chronologically ordered series of Routes which can be taken in order to get from StartPosition to EndPosition
        TripLength (int): the time needed overall to complete the route
        TripDistance (int): the distance between start position and end position
        TripEmission (int): how much emission will be created upon completion of this route
        Status (Status): Ready, InProgress, Paused, Complete; current status of the trip
        Player (Player): the player which is interacting with the trip
    Methods:
        StartTrip(): sets the Trip Status to “InProgress”
        EndTrip(): sets the Trip Status to “Complete”
        PauseTrip(): sets the Trip Status to Paused
        GetScore(): Calculates final score of a trip taking into account the time, CO2 emission, etc - should utilize the TripCalculator
        CalculateCO2Emission(): Calculates CO2 taken for a trip.
        GetStatus: Check the status of the trip

 */
public class Trip {
    // TODO: whoever implements this class has the task of deciding how to represent the routes so its clear which portion of 
    // each route was taken!
}
