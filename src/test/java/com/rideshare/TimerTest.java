package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static java.time.temporal.ChronoUnit.SECONDS;;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class TimerTest {
    // Runs a loop that forces the code to wait x seconds
    private void waitTime(int seconds) {
        LocalTime start = LocalTime.now();
        LocalTime now = LocalTime.now();
        while (SECONDS.between(now, start) <= seconds) {
            // do nothing
            now = LocalTime.now();
        }
    }
    @Test
    void testGetTime() {

    }

    @Test
    void testInitialize() {
        Timer t = new Timer();
        assertEquals(TimerState.INITIALIZED, t.getState());
        assertEquals(0, LocalTime.of(0,0,0).compareTo(t.getTime()));
    }

    @Test
    void testStart() {
        Timer t = new Timer();
        LocalTime start = LocalTime.now();
        LocalTime now = LocalTime.now();
        t.initialize();
        t.start();
        while (SECONDS.between(now, start) <= 3) {
            // do nothing
            now = LocalTime.now();
        }
        assertEquals(3, LocalTime.of(0,0,0).compareTo(t.getTime()));
        assertEquals(TimerState.RUNNING, t.getState());
    }

    @Test
    void testPause() {
        Timer t = new Timer();
        t.initialize();
        t.start();
        waitTime(3);
        t.pause();
    
        assertEquals(TimerState.PAUSED, t.getState());
        assertEquals(3, LocalTime.of(0,0,0).compareTo(t.getTime()));
        waitTime(3);
        assertEquals(3, LocalTime.of(0,0,0).compareTo(t.getTime()));

        // Should pick up from the last time it was at
        t.start();
        assertEquals(3, LocalTime.of(0,0,0).compareTo(t.getTime()));
    }


    @Test
    void testStop() {
        Timer t = new Timer();
        t.initialize();
        t.start();
        waitTime(3);
        t.stop();
    
        assertEquals(TimerState.STOPPED, t.getState());
        // Time should get reset to 0
        assertEquals(0, LocalTime.of(0,0,0).compareTo(t.getTime()));
    }
}
