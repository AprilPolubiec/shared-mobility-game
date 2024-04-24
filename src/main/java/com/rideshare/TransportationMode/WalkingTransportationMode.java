package com.rideshare.TransportationMode;

/*Source for CO2e/km estimate: https://www.nature.com/articles/s41598-020-66170-y
 * This source states that walking as a mode of transport has an average CO2 emissions (equivalent per km) rate of
 * 50-260 CO2e per KM. However, this metric is based on GHG emissions associated with food intake required to fuel a kilometre of walking range.
 * Thus, for clarity, it is being treated as CO2e neutral in this class.*/
public class WalkingTransportationMode extends TransportationMode {
    private static final int WALKING_SPEED = 5;
    private static final int WALKING_EMISSION_RATE = 0;

    public WalkingTransportationMode(String name) {
        super(name, WALKING_EMISSION_RATE, WALKING_SPEED, false);
    }
}
