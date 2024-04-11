package com.rideshare;

import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;

public class Timer {
    private final LocalTime DAY_START = LocalTime.of(5,0,0);
    private final LocalTime DAY_END = LocalTime.of(0, 0, 0);
    boolean isPaused = false;
    private LocalTime currentInGameTime;
    private TimerStatus status = TimerStatus.UNINITIALISED;
    private Timeline timeline;
    Text clockText;

    public Timer(Text clockText) {
        this.clockText = clockText;
        initialise();
    }

    public void initialise() {
        currentInGameTime = DAY_START;
        status = TimerStatus.INITIALISED;
    }

    private void updateInGameTime() {
        if(status == TimerStatus.RUNNING) {
            currentInGameTime = currentInGameTime.plusMinutes(1);
            // Update clock text
            if(clockText != null) {
                clockText.setText(currentInGameTime.format(DateTimeFormatter.ofPattern("HH:mm a")));
            }
            if (isEndOfDay()) {
                System.out.println("End of day reached.");
                stop(); 
            }
        }
    }

    public void start() {
        currentInGameTime = DAY_START;
        status = TimerStatus.RUNNING;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 6), event -> updateInGameTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pause() {
        status = TimerStatus.PAUSED;
        timeline.pause();
        isPaused = true;
    }

    public void resume() {
        timeline.play();
        status = TimerStatus.RUNNING;
        isPaused = false;
    }
    
    private boolean isEndOfDay() {
        return currentInGameTime.equals(DAY_END);
    }

    private void resetTimer() {
        currentInGameTime = DAY_START;
        isPaused = false;
        timeline.stop();
        System.out.println("Timer reset.");
    }

    
    void stop() {
        status = TimerStatus.STOPPED;
        isPaused = false;
        timeline.stop();
        System.out.println("Timer has been stopped.");
    }

    public LocalTime getTime() {
        return this.currentInGameTime;
    }
        
    public TimerStatus getState() {
        return this.status;
    }
   
}