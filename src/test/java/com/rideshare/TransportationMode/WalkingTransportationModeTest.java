package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WalkingTransportationModeTest {
    @Test 
    public void Test_CanCreateWalkingTransportationMode(){;
        String displayName = "Main Street";
        TransportationMode tm = new WalkingTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate());
        assertEquals(5, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(false, tm.hasStops());
    }
}
