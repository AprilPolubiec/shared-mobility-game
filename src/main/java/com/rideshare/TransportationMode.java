package com.rideshare;

/**
 * Description: A mode of transportation is a medium used to get around a city 
 * (vehicle, walking, etc). It has an associated CO2 emission rate and speed
    Attributes:
    CO2EmissionRate (int): Indicates the amount of carbon dioxide emissions produced per distance unit
    Speed (int): how many distance units the mode can travel per minute
    DisplayName
    hasStops (boolean): true/false value, indicates whether the mode has stops
 */
public class TransportationMode {
    // TODO:
    // - decide how to represent emission rate (per km? per sec?)
    // - decide how to represent speed (km/hr? pixels/ns?)
    int co2EmissionRate;
    int speed;

    public TransportationMode(int co2EmissionRate, int speed) {
        transportSpeed = speed;
        co2Emissions = co2EmissionRate;
    }

    public static void main(String[] args) {}
}
