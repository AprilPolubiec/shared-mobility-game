package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TrainTransportationModeTest {
    @Test 
    public void Test_CanCreateTrainTransportationMode(){;
        String displayName = "Metro";
        TransportationMode tm = new TrainTransportationMode(displayName);
        
        assertEquals(0, tm.getEmissionRate());
        assertEquals(5, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }
}
