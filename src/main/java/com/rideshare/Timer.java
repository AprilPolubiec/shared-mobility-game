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
    private TimerState state = TimerState.UNINITIALIZED;
    private Timeline _timeline;
    Text _clockText;

    public Timer(Text clockText) {
        this._clockText = clockText;
        initialize();
    }

    public void initialize() {
        currentInGameTime = DAY_START;
        state = TimerState.INITIALIZED;
    }

    private void updateInGameTime() {
        if(state == TimerState.RUNNING) {
            currentInGameTime = currentInGameTime.plusMinutes(1);
            // Update clock text
            _clockText.setText(currentInGameTime.format(DateTimeFormatter.ofPattern("HH:mm a")));
            if (isEndOfDay()) {
                System.out.println("End of day reached.");
                stop(); 
            }
        }
    }

    public void start() {
        currentInGameTime = DAY_START;
        state = TimerState.RUNNING;
        _timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 6), event -> updateInGameTime()));
        _timeline.setCycleCount(Timeline.INDEFINITE);
        _timeline.play();
    }

    public void pause() {
        state = TimerState.PAUSED;
        _timeline.pause();
        isPaused = true;
    }

    public void resume() {
        _timeline.play();
        state = TimerState.RUNNING;
        isPaused = false;
    }
    
    private boolean isEndOfDay() {
        return currentInGameTime.equals(DAY_END);
    }

    private void resetTimer() {
        currentInGameTime = DAY_START;
        isPaused = false;
        _timeline.stop();
        System.out.println("Timer reset.");
    }

    
    private void stop() {
        state = TimerState.STOPPED;
        isPaused = false;
        _timeline.stop();
        System.out.println("Timer has been stopped.");
    }

    public LocalTime getTime() {
        return this.currentInGameTime;
    }
        
    public TimerState getState() {
        return this.state;
    }
   
}