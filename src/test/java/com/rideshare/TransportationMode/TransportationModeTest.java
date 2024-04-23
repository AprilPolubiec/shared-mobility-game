package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TransportationModeTest {
    @Test 
    public void Test_CanCreateTransportationMode(){
        int emissionRate = 5;
        int speed = 5;
        String displayName = "Luas";
        boolean hasStops = true;
        TransportationMode tm = new TransportationMode(displayName, emissionRate, speed, hasStops);
        assertEquals(emissionRate, tm.getEmissionRate());
        assertEquals(speed, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(hasStops, tm.hasStops());
    }
}