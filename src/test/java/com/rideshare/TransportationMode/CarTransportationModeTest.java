package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CarTransportationModeTest {
    @Test 
    public void Test_CanCreateBusTransportationMode(){;
        String displayName = "Honda Civic";
        TransportationMode tm = new CarTransportationMode(displayName);
        
        assertEquals(192, tm.getEmissionRate());
        assertEquals(48, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }
}
