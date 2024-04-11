package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TrainTransportationModeTest {
    @Test 
    public void Test_CanCreateTrainTransportationMode(){;
        String displayName = "Metro";
        TransportationMode tm = new TrainTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate()); // TODO: decide emission rate
        assertEquals(5, tm.getSpeed()); // TODO: decide how to measure speed
        assertEquals(displayName, tm.getName());
        assertTrue(tm.hasStops());
    }
}
