package com.rideshare.City;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.rideshare.TransportationMode.TransportationType;

public class RouteTest {
    @Test
    void Test_CanCreateRoute() {
        Route r = new Route(new int[5][5], TransportationType.WALKING, "test route");

        assertEquals("test route", r.getName());
        assertEquals(TransportationType.WALKING, r.getTransportationType());
    }

}
