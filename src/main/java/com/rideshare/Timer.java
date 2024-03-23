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
    private LocalTime currentInGameTime;
    private LocalTime currentIRLTime;
    private boolean isRunning = true;
    private boolean isPaused = false;

 
    public Timer() {
        currentInGameTime = LocalTime.MIDNIGHT;
        currentIRLTime = LocalTime.MIDNIGHT;
    }

    public void initialize() {
        state = TimerState.INITIALIZED;
        currentInGameTime = LocalTime.of(0, 0, 0);
    }
   
    public void start() {
        isRunning = true;
        currentIRLTime = LocalTime.now();
        // TODO:
        // Start a while loop, that runs until it is end of the day
        // The loop should check if one second has passed since it last ran
        // If one second has passed, it should increment the currentTime by one second
        // If the currentTime reaches the EOD, call stop()
        
        // Finer-grained implementation, Time Ratio: 1/288 seconds IRL, 1 minute in GameTime
        //  5 minutes in IRL = 24 hours in GameTime

        while ((!isEndOfDay()) && isRunning) {
            // this initialises the 'counter' for right now! i.e. 12:30:01 
            LocalTime counterTime = LocalTime.now();

            // this checks if 1/288 second has passed 12:30:(1/288). 
            // & if it is the case, increment the current game time by the amount we want i.e. 1 minute
            // Then it sets currentIRL as equivalent to counterTime
            // functionally updating the counterTime by 1/288
            if (LocalTime.now().isAfter(currentIRLTime.plusNanos(3472222))) {
                currentInGameTime = currentInGameTime.plusMinutes(1);
                currentIRLTime = counterTime;
                System.out.println("Current in-game time: " + currentInGameTime);
                System.out.println("CurrentIRLTime:" + currentIRLTime);
            }
            try {
                    Thread.sleep(100); // Sleep for 100 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (isEndOfDay()) {
                stop();
                }        
        } 
        
    }
        
        
        // Loose grained implementation, Time Ratio: 1 second IRL, 30 minutes in GameTime
        // while (!isEndOfDay() && isRunning) {
        //     LocalTime newTime = LocalTime.now();

        //     if (LocalTime.now().isAfter(currentIRLTime.plusSeconds(1))){
        //         // currentInGameTime = currentInGameTime.plusHours(1);
        //         currentInGameTime = currentInGameTime.plusMinutes(30);
        //         currentIRLTime = newTime;
        //         System.out.println("Current in-game time: " + currentInGameTime);
        //         System.out.println("CurrentIRLTime:" + currentIRLTime);
        //     }
        //     try {
        //         Thread.sleep(100); // Sleep for 100 milliseconds
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
            
        //     if (isEndOfDay()) {
        //         stop();
        //     }
        // }
    // }

    public void stop() {
        state = TimerState.STOPPED;
        isRunning = false;
        
    }

    public void pause() {
        state = TimerState.PAUSED;
        isRunning = false;
        // throw new UnsupportedOperationException("pause() not implemented yet");
    }

    private boolean isEndOfDay() {
        
        // loose-grained implementation of EndOfDay
        // Checks if currentInGameTime is before midnight (i.e. checks if it's 23:00) &
        // then checks if we add an hour equivalent to 00:00.
        // boolean result = currentInGameTime.isBefore(LocalTime.MIDNIGHT) && currentInGameTime.plusHours(1).equals(checkForNextMidgnight);
        
        // return currentInGameTime.compareTo(LocalTime.MAX) == 0;
        return currentInGameTime.equals(LocalTime.of(23, 59));

    }

    public LocalTime getTime() {
        return currentInGameTime;

    }

    public TimerState getState() {
        return state;

    }

    // Tester function since the TimerTest.java isn't working for me D:
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.initialize();
        timer.start();
}
}
