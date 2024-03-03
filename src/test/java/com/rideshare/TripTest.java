package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TripTest {
    @Test
    public void Test_CanCreateTrip() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking", // TODO: literal string is a red flag! we need a constant or an enum somewhere
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        assertArrayEquals(start, trip.getStartCoordinates());
        assertArrayEquals(end, trip.getEndCoordinates());
        assertEquals(TripStatus.READY, trip.getStatus());
    }

    @Test
    public void Test_CanStoreTripStatistics() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking",
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        assertEquals(0, trip.getTime()); // TODO: depends on what we decide for our TransportationMode values
        assertEquals(0, trip.getDistance()); // TODO: depends on what we decide for our TransportationMode values
        assertEquals(0, trip.getEmission()); // TODO: depends on what we decide for our TransportationMode values
    }

    @Test
    public void Test_CanStartTrip() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking",
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        trip.start();
        assertEquals(TripStatus.IN_PROGRESS, trip.getStatus());
    }

    @Test
    public void Test_CanPauseTrip() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking",
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        trip.start();
        trip.pause();
        assertEquals(TripStatus.PAUSED, trip.getStatus());
    }

    @Test
    public void Test_CanEndTrip() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking",
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        trip.start();
        trip.end();
        assertEquals(TripStatus.COMPLETED, trip.getStatus());
    }

    @Test
    public void Test_CanGetTripScore() {
        // TODO: this is ONE idea of how to represent the routes
        var path = {
            {   "Walking",
                {   
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                }
            },
            {   "Bus",
                {
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                }
            }
        };
        int[] start = { 2, 0 };
        int[] end = { 2, 4 };

        Trip trip = new Trip(start, end, path);
        trip.start();
        trip.end();
        assertEquals(0, trip.getScore()); // TODO: depends on our scoring system
    }
}