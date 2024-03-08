package com.rideshare;

import java.time.LocalTime;

/**
 * Description: a timer is an object which keeps track of time within the game. 
 * It is not a reflection of the "real-world" time. This is important because, for example,
 * we want a mailbox to last 15 minutes in the fictional world, but not 15 minutes in real life.
 * This class should abstract out the logic of mapping the game's time of day to whatever is using this.
 * TODO: should be generalizable across the code
 */
public class Timer {
    private TimerState state = TimerState.UNINITIALIZED;
    private LocalTime currentTime;

    public Timer() {
        // Initialize timer
        throw new UnsupportedOperationException("Timer() not implemented yet");
    }

    public void initialize() {
        // Should set the state to initialized
        // Should set current time to 00:00
        throw new UnsupportedOperationException("initialize() not implemented yet");
    }
   
    public void start() {
        // TODO:
        // Start a while loop, that runs until it is end of the day
        // The loop should check if one second has passed since it last ran
        // If one second has passed, it should increment the currentTime by one second
        // If the currentTime reaches the EOD, call stop()
        while (!isEndOfDay()) {
            // Do things!
            if (isEndOfDay()) {
                stop();
            }
        }
        throw new UnsupportedOperationException("start() not implemented yet");
    }

    public void pause() {
        throw new UnsupportedOperationException("pause() not implemented yet");
    }

    public void stop() {
        throw new UnsupportedOperationException("stop() not implemented yet");
    }

    private boolean isEndOfDay() {
        // Note: this is looking for an EXACT timestamp, in the loop above make sure we set the clock to MAX appropriately
        return currentTime.compareTo(LocalTime.MAX) == 0;
    }

    public LocalTime getTime() {
        throw new UnsupportedOperationException("getTime() not implemented yet");
    }

    public TimerState getState() {
        throw new UnsupportedOperationException("getState() not implemented yet");
    }
}
