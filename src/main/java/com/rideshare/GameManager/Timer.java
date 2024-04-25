package com.rideshare.GameManager;

import javafx.util.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.rideshare.Utils;
import com.rideshare.TileManager.TileUtils;
import com.rideshare.GameManager.TimerState;
import com.rideshare.City.Mailbox;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;


import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.Background;

public class Timer {
    private static final LocalTime DAY_START = LocalTime.of(5, 0, 0);
    private static final LocalTime DAY_END = LocalTime.of(0, 0, 0);
    boolean isPaused = false;
    private LocalTime currentInGameTime;
    private TimerState state = TimerState.UNINITIALIZED;
    private Timeline timeline;
    Text clockText;
    Font font;
    private Button pauseButton;

    public Timer() {
        this.font = Font.loadFont(getClass().getResourceAsStream("/fonts/digital-7.ttf"), 48);
        initialize();
    }

    public void initialize() {
        this.currentInGameTime = DAY_START;
        this.state = TimerState.INITIALIZED;
    }

    private void updateInGameTime() {
        if (this.state == TimerState.RUNNING) {
            this.currentInGameTime = this.currentInGameTime.plusMinutes(1);
            // Update clock text
            if (this.clockText != null) {
                this.clockText.setText(this.currentInGameTime.format(DateTimeFormatter.ofPattern("HH:mm a")));
            }
            if (isEndOfDay()) {
                System.out.println("End of day reached.");
                stop();
            }
        }
    }

    public void start() {
        this.currentInGameTime = DAY_START;
        this.state = TimerState.RUNNING;
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 6), event -> updateInGameTime()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void pause() {
        this.state = TimerState.PAUSED;
        this.timeline.pause();
        this.isPaused = true;
    }

    public void resume() {
        this.timeline.play();
        this.state = TimerState.RUNNING;
        this.isPaused = false;
    }

    private boolean isEndOfDay() {
        return this.currentInGameTime.equals(DAY_END);
    }

    public void resetTimer() {
        this.currentInGameTime = DAY_START;
        this.isPaused = false;
        this.timeline.stop();
        System.out.println("Timer reset.");
    }

    public void stop() {
        this.state = TimerState.STOPPED;
        this.isPaused = false;
        this.timeline.stop();
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
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui/grey_panel.png"))));
        timeModalRoot.getChildren().add(panelImageView);
        AnchorPane.setBottomAnchor(timeModalRoot, 0.0);
        AnchorPane.setLeftAnchor(timeModalRoot, TileUtils.TILE_SIZE_IN_PIXELS * 30.0);
        panelImageView.setFitHeight(150);
        panelImageView.setFitWidth(300);

        this.clockText = new Text("00:00AM");
        this.clockText.setFont(this.font);
        this.clockText.setFill(javafx.scene.paint.Color.BLACK);
        timeModalRoot.getChildren().add(this.clockText);

        root.getChildren().add(timeModalRoot);
    }

    public String getTimeElapsedString() {
        long minutesElapsed = MINUTES.between(DAY_START, currentInGameTime);
        int hours   = (int)minutesElapsed / 60;
        int minutes = (int)minutesElapsed % 60;

        // Left pad minutes with 0
        String minutesString = Integer.toString(minutes);
        while (minutesString.length() < 2) {
            minutesString = String.format("0%s", minutesString);
        }
        
        return String.format("%s:%s", hours, minutesString);
    }

    public long getMinutesLeft() {
        long minutesLeft = MINUTES.between(DAY_END, currentInGameTime);
        Utils.print(String.format("%s minutes left on timer", minutesLeft));
        return minutesLeft;
    }


    public void renderPauseButton(AnchorPane root) {
        Image pauseImage = new Image(getClass().getResourceAsStream("/images/ui/pauseButton.png"));
        Image playImage = new Image(getClass().getResourceAsStream("/images/ui/playButton.png"));

        pauseButton = new Button();
        AnchorPane.setTopAnchor(pauseButton, 418.0); 
        AnchorPane.setRightAnchor(pauseButton, 298.0);

        BackgroundImage backgroundImage = new BackgroundImage(
                        pauseImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
                );

                
        Background background = new Background(backgroundImage);
        pauseButton.setBackground(background);


        double desiredWidth = 305; 
        double desiredHeight = 200; 
        pauseButton.setPrefWidth(desiredWidth);
        pauseButton.setPrefHeight(desiredHeight); 


        pauseButton.setOnAction(e -> {
            if (getState() == TimerState.RUNNING) {
                pause();
                Mailbox.disableMailboxes();
                pauseButton.setBackground(new Background(new BackgroundImage(
                        playImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
                )));
            } else {
                resume();
                Mailbox.enableMailboxes();
                pauseButton.setBackground(new Background(new BackgroundImage(
                        pauseImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
                )));
            }
        });
        root.getChildren().add(pauseButton);
    }

    public Button getPauseButton() {
        return pauseButton;
    }

     
    

}