package com.rideshare.TransportationMode;

/*Source for CO2e/km estimate: https://8billiontrees.com/carbon-offsets-credits/how-much-co2-does-a-car-emit-per-mile/
 * This source states that a car (medium petrol-powered) as a mode of transport has an average CO2 emissions (equivalent per km) rate of
 * 192 CO2e per KM, hence passing it as a fixed value to the constructor*/
public class CarTransportationMode extends TransportationMode {
    private final static int CAR_EMISSION_RATE = 192;
    private final static int CAR_SPEED = 48; // km/hr

    public CarTransportationMode(String name) {
        super(name, CAR_EMISSION_RATE, CAR_SPEED, true);
    }

}
