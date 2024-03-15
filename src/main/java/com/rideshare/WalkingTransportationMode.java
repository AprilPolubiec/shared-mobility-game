package com.rideshare;

/**
 * 
 */
public class WalkingTransportationMode extends TransportationMode {
    /*Source for CO2e/km estimate: https://www.nature.com/articles/s41598-020-66170-y
     * This source states that walking as a mode of transport has an average CO2 emissions (equivalent per km) rate of
     * 50-260 CO2e per KM. However, this metric is based on GHG emissions associated with food intake required to fuel a kilometre of walking range.
     * Thus, for clarity, it is being treated as CO2e neutral in this class.*/
    // TODO: override the appropriate properties from TransportationMode
    public WalkingTransportationMode(String name, int speed, boolean hasStops) {
        super(name,0, speed, hasStops);
    }

    public static void main(String[] args) {}
}
