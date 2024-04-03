package com.rideshare;

/*Source for CO2e/km estimate: https://ourworldindata.org/travel-carbon-footprint
 * This source states that a train (national rail) as a mode of transport has an average CO2 emissions (equivalent per km) rate of
 * 35 CO2e per KM, hence passing it as a fixed value to the constructor*/
public class TrainTransportationMode extends TransportationMode {
    private final static int trainEmissionRate = 35;
    private final static int trainSpeed = 30; // km/hr

    public TrainTransportationMode(String name) {
        super(name, trainEmissionRate, trainSpeed, true);
    }
}
