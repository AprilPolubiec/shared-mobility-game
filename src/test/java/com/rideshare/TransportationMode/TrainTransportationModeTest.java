package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TrainTransportationModeTest {
    @Test 
    public void Test_CanCreateTrainTransportationMode(){;
        String displayName = "Metro";
        TransportationMode tm = new TrainTransportationMode(displayName);
        
        assertEquals(35, tm.getEmissionRate());
        assertEquals(30, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }
}
