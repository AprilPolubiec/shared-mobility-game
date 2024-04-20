package com.rideshare;

import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.rideshare.TileManager.TileUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Timer {
    private final LocalTime DAY_START = LocalTime.of(5, 0, 0);
    private final LocalTime DAY_END = LocalTime.of(0, 0, 0);
    boolean isPaused = false;
    private LocalTime currentInGameTime;
    private TimerState state = TimerState.UNINITIALIZED;
    private Timeline _timeline;
    Text _clockText;
    Font _font;

    public Timer() {
        this._font = Font.loadFont(getClass().getResourceAsStream("/fonts/digital-7.ttf"), 48);
        initialize();
    }

    public void initialize() {
        currentInGameTime = DAY_START;
        state = TimerState.INITIALIZED;
    }

    private void updateInGameTime() {
        if (state == TimerState.RUNNING) {
            currentInGameTime = currentInGameTime.plusMinutes(1);
            // Update clock text
            if (_clockText != null) {
                _clockText.setText(currentInGameTime.format(DateTimeFormatter.ofPattern("HH:mm a")));
            }
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

    // 1 second = 6 minutes
    public static double secondsToGameMinutes(double seconds) {
        return seconds * 6.0;
    }
    // 1 minute = 1/6 seconds
    public static double gameMinutesToSeconds(double minutes) {
        return minutes / 6.0;
    }

    public void render(AnchorPane root) {
        StackPane timeModalRoot = new StackPane();
        ImageView panelImageView = new ImageView(
                new Image(getClass().getResourceAsStream("/images/ui/grey_panel.png")));
        timeModalRoot.getChildren().add(panelImageView);
        AnchorPane.setBottomAnchor(timeModalRoot, 0.0);
        AnchorPane.setLeftAnchor(timeModalRoot, TileUtils.TILE_SIZE_IN_PIXELS * 30.0);
        panelImageView.setFitHeight(150);
        panelImageView.setFitWidth(300);

        _clockText = new Text("00:00AM");
        _clockText.setFont(_font);
        _clockText.setFill(javafx.scene.paint.Color.BLACK);
        timeModalRoot.getChildren().add(_clockText);

        root.getChildren().add(timeModalRoot);
    }

}