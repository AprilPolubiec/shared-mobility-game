package com.rideshare;

public class CarTransportationMode extends TransportationMode {
    /*Source for CO2e/km estimate: https://8billiontrees.com/carbon-offsets-credits/how-much-co2-does-a-car-emit-per-mile/
     * This source states that a car (medium petrol-powered) as a mode of transport has an average CO2 emissions (equivalent per km) rate of
     * 192 CO2e per KM, hence passing it as a fixed value to the constructor*/
    public CarTransportationMode(int co2eRate, int speedKm, boolean stops) {
        super(192, speedKm, stops);
    }

    public static void main(String[] args) {}
}
