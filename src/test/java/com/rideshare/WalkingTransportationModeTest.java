package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class WalkingTransportationModeTest {
    @Test 
    public void Test_CanCreateWalkingTransportationMode(){;
        String displayName = "Main Street";
        TransportationMode tm = new WalkingTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate());
        assertEquals(5, tm.getSpeed()); // TODO: decide how to measure speed
        assertEquals(displayName, tm.getName());
        assertFalse(tm.hasStops());
    }
}
