package com.rideshare.TransportationMode;

/*Source for CO2e/km estimate: https://8billiontrees.com/carbon-offsets-credits/how-much-co2-does-a-car-emit-per-mile/
* This source states that a bus as a mode of transport has an average CO2 emissions (equivalent per km) rate of
* 105 CO2e per KM, hence passing it as a fixed value to the constructor*/ 
public class BusTransportationMode extends TransportationMode {
    private final static int busEmissionRate = 105;
    private final static int busSpeed = 40; // km/hr

    public BusTransportationMode(String name) {
        super(name, busEmissionRate, busSpeed, true);
    }
}