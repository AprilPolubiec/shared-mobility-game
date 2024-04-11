package com.rideshare.GameManager;

import java.util.Random;

import com.rideshare.City;
import com.rideshare.Mailbox;
import com.rideshare.MailboxStatus;
import com.rideshare.Player;
import com.rideshare.Timer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Game {
    private Sprite _player;
    private Timer _timer;
    private int _level;
    private City _city;
    public Game(AnchorPane root, City city, Sprite player) { // TODO: this should be a Player
        this._player = player;
        this._city = city;
        this._timer = new Timer();
        this._timer.render(root);
    }

    public void setLevel(int levelNumber) {
        this._level = levelNumber;
    }

    public int getLevel() {
        return this._level;
    }

    // TODO: this will take in whatever is loaded by the loader and initialize from there
    public void loadExisting() {
        return;
    }

    public void start() {
        _player.render();
        _timer.start();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            showRandomMailbox();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }


    private void showRandomMailbox() {
        int numMailboxes = _city.getUninitializedMailboxes().size();
        int randomMailboxIndex = new Random().nextInt(numMailboxes);
        Mailbox currentMailbox = _city.getUninitializedMailboxes().get(randomMailboxIndex);
        currentMailbox.setDuration(5);
        currentMailbox.render();
        currentMailbox.show();
        currentMailbox.addStatusListener(new ChangeListener<MailboxStatus>() {
            @Override
            public void changed(ObservableValue<? extends MailboxStatus> observable, MailboxStatus oldStatus, MailboxStatus newStatus) {
                System.out.println("Status changed: " + newStatus);
                // Call a method or do something else based on the new value
                if (newStatus == MailboxStatus.COMPLETED) {
                    // Do something
                }
                if (newStatus == MailboxStatus.FAILED) {
                    // Do something
                }
                if (newStatus == MailboxStatus.IN_PROGRESS) {
                    // Do something
                    // CALCULATE TRIPS
                }
            }
        });
    }
}
