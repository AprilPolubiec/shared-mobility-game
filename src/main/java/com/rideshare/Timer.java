package com.rideshare;

import java.time.LocalTime;

public class Timer {
    private LocalTime currentInGameTime;
    private LocalTime currentIRLTime;
    private volatile boolean isPaused = false;
    private LocalTime pauseTime;
    private TimerState state = TimerState.UNINITIALIZED;

    public Timer() {
        currentInGameTime = LocalTime.MIDNIGHT;
        currentIRLTime = LocalTime.MIDNIGHT;
        initialize();
    }

    public void initialize() {
        state = TimerState.INITIALIZED;
    }

    public void start() {
        currentIRLTime = LocalTime.now();

        while (true) { // Infinite loop to keep the timer running until the conditions isEndOfDay() or Pause()
            if (!isPaused) {
                LocalTime counterTime = LocalTime.now();

                if (LocalTime.now().isAfter(currentIRLTime.plusNanos(3472200))) {
                    currentInGameTime = currentInGameTime.plusSeconds(1);
                    currentIRLTime = counterTime;
                    System.out.println("Current in-game time: " + currentInGameTime);
                    System.out.println("Current IRL Time: " + currentIRLTime);
                }

                // Check if it's the end of the day
                if (isEndOfDay()) {
                    System.out.println("End of day reached.");
                    stop(); 
                    break;
                }
            } else {
                System.out.println("Timer paused.");
                break; 
            }
        }
    }

    public void pause() {
        state = TimerState.PAUSED;
        isPaused = true;
        pauseTime = LocalTime.now();
    }

    public void resume() {
        state = TimerState.RUNNING;
        isPaused = false;
        
        long elapsedTimeSeconds = LocalTime.now().toSecondOfDay() - pauseTime.toSecondOfDay();
    
        // Add the elapsed time to the current in-game time
        currentInGameTime = currentInGameTime.plusSeconds(elapsedTimeSeconds);
        currentIRLTime = LocalTime.now();
        System.out.println("Timer resumed.");

        while (!isEndOfDay()) { // Infinite loop to keep the timer running until the conditions isEndOfDay() or Pause()
            if (!isPaused) {
                LocalTime counterTime = LocalTime.now();

                if (isEndOfDay()) {
                    System.out.println("End of day reached.");
                    stop(); 
                    break;
                }

                if (LocalTime.now().isAfter(currentIRLTime.plusNanos(3472200))) {
                    currentInGameTime = currentInGameTime.plusSeconds(1);
                    currentIRLTime = counterTime;
                    System.out.println("Current in-game time: " + currentInGameTime);
                    System.out.println("Current IRL Time: " + currentIRLTime);
                }

                // Check if it's the end of the day
            } else {
                System.out.println("Timer paused.");
                break; // Exit the loop once paused
            }
        }


    }
    
    private boolean isEndOfDay() {
        return currentInGameTime.equals(LocalTime.of(23, 59, 59));
    }

    private void resetTimer() {
        currentInGameTime = LocalTime.MIDNIGHT;
        currentIRLTime = LocalTime.now();
        isPaused = false;
        System.out.println("Timer reset.");
    }

    
    private void stop() {
        currentInGameTime = LocalTime.MIDNIGHT;
        state = TimerState.STOPPED;
        isPaused = false;
        System.out.println("Timer has been stopped.");
    }

    public LocalTime getTime() {
        return this.currentInGameTime;
    }
        
    public TimerState getState() {
        return this.state;
    }
   
}
    // TO TEST TIMER uncomment below
    // For consistent behaviour, the timer must run within a seperate thread i.e. the "main" code block
    // seen below. I've tried many times to start it simply by doing timer.start but it seems that the
    // LocalTime object is thread dependent, and attempting to just "run" it within the current/ press play so to speak
    // results in unexpected behaviour (just doesn't really work so to speak). If linking with mailboxes


//     public static void main(String[] args) {
//         Timer timer = new Timer();
//         Thread timerThread = new Thread(timer::start);
//         timerThread.start();

//         // testing pause function
//         try {
//             Thread.sleep(1000); // Sleep for 10 seconds
//             timer.pause(); // Pause the timer after 10 seconds
//             timerThread.join(); // Wait for the timer thread to finish
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         // testing pause and resume functions
//         try {
//             Thread.sleep(1000); // Sleep for 10 seconds
//             timer.pause(); // Pause the timer after 10 seconds
//             timer.getState();
//             timer.getTime();

//             Thread.sleep(1000); // Sleep for 5 seconds to simulate some time passing
//             timer.resume(); // Resume the timer after 5 seconds
//             timerThread.join(); // Wait for the timer thread to finish
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         // testing resetTimer function + checking getState getTime
//         try {
//                 Thread.sleep(1000); // Sleep for 10 seconds
//                 timer.pause(); // Pause the timer after 10 seconds
//                 Thread.sleep(1000); // Sleep for 5 seconds to simulate some time passing
//                 timer.resetTimer(); // Resume the timer after 5 seconds
//                 timer.getState();
//                 timer.getTime();
//                 timerThread.join(); // Wait for the timer thread to finish
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }

//     }
// }