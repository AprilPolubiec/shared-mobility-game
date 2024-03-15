package com.rideshare;

public class TrainTransportationMode extends TransportationMode {
    /*Source for CO2e/km estimate: https://ourworldindata.org/travel-carbon-footprint
     * This source states that a train (national rail) as a mode of transport has an average CO2 emissions (equivalent per km) rate of
     * 35 CO2e per KM, hence passing it as a fixed value to the constructor*/
    public TrainTransportationMode(String modeName, int speedKm, boolean stops) {
        super(modeName,35, speedKm, stops);
    }

    public static void main(String[] args) {}
}
