package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BusTransportationModeTest {
    @Test 
    public void Test_CanCreateBusTransportationMode(){;
        String displayName = "CitiBus";
        TransportationMode tm = new BusTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate()); // TODO: decide emission rate
        assertEquals(5, tm.getSpeed()); // TODO: decide how to measure speed
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }
}
