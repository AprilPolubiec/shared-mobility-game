package com.rideshare;

/**
 * Description: A mode of transportation is a medium used to get around a city 
 * (vehicle, walking, etc.). It has an associated CO2 emission rate and speed
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
    private final String name;
    protected final int co2EmissionRate;
    protected final int speed;
    protected final boolean hasStops;
    public TransportationMode(String name, int co2eEmissionRate, int speed, boolean hasStops) {
        this.name = name;
        this.co2EmissionRate = co2eEmissionRate;
        this.speed = speed;
        this.hasStops = hasStops;

    }
    public String getName() {
        return this.name;
    }

    public int getEmissionRate() {
        return this.co2EmissionRate;
    }


    public int getSpeed() {
        return this.speed;
    }

    public boolean hasStops() {
        return this.hasStops;
    }

}
