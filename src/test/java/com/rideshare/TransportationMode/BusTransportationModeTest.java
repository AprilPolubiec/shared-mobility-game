package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BusTransportationModeTest {
    @Test 
    public void Test_CanCreateBusTransportationMode(){;
        String displayName = "CitiBus";
        TransportationMode tm = new BusTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate());
        assertEquals(5, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }
}
