package com.rideshare.TransportationMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BusTransportationModeTest {
    @Test
    public void Test_CanCreateBusTransportationMode() {
        String displayName = "CitiBus";
        TransportationMode tm = new BusTransportationMode(displayName);

        assertEquals(105, tm.getEmissionRate());
        assertEquals(40, tm.getSpeed());
        assertEquals(displayName, tm.getName());
        assertEquals(true, tm.hasStops());
    }

    @Test
    public void Test_CanCalculateEmission() {
        String displayName = "CitiBus";
        TransportationMode tm = new BusTransportationMode(displayName);
        int distance = 10; // in miles
        double emission = tm.calculateEmission(distance);
        assertEquals(10.5, emission); // 10 miles * 105 emission rate / 100
    }
}
