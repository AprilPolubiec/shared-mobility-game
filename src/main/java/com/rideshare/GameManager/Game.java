package com.rideshare.GameManager;

import java.util.ArrayList;
import java.util.Random;

import com.rideshare.ChooseTripComponent;
import com.rideshare.City;
import com.rideshare.GridPanePosition;
import com.rideshare.Mailbox;
import com.rideshare.MailboxStatus;
import com.rideshare.Player;
import com.rideshare.ScoreKeeper;
import com.rideshare.Timer;
import com.rideshare.TimerState;
import com.rideshare.Trip;
import com.rideshare.TripCalculator;
import com.rideshare.Utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Game {
    private Player _player;
    private Timer _timer;
    private int _level;
    private City _city;
    private TripCalculator _tripCalculator;
    private AnchorPane _root;
    private Trip _currentTrip;
    private Timeline _timeline;
    private ChooseTripComponent _tripChooser;

    public Game(AnchorPane root, City city, Player player) {
        Utils.print(String.format("Building a new city"));
        this._player = player;
        this._city = city;
        this._root = root;
        this._timer = new Timer();
        this._timer.render(root);
        this._tripCalculator = new TripCalculator(this._city);
        this._level = 0;

        this._player.getScoreKeeper().setLevel(0);
        this._player.getScoreKeeper().setTotalMailboxes(_city.getMailboxes().size());

        _tripChooser = new ChooseTripComponent(_root);
        _tripChooser.onSelectedTripChanged(new ChangeListener<Trip>() {
            @Override
            public void changed(ObservableValue<? extends Trip> observable, Trip oldValue, Trip newValue) {
                _currentTrip = newValue;
                _timer.resume();
                _player.moveOnRoute(newValue.getNodeList());
                // TODO: how do I wait for the above to finish?
                _tripChooser.clear();
            }
        });
    
        initializeGameLoop();
    }

    // TODO: this will take in whatever is loaded by the loader and initialize from
    // there
    public void loadExisting() {
        this._player.loadExisting();
        return;
    }

    private void initializeGameLoop() {
        Utils.print(String.format("Starting game loop"));
        _timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            // Get the mailboxes that have not been rendered yet
            int numMailboxes = _city.getMailboxes().size();
            int mailboxesLeft = numMailboxes - _city.getFailedOrCompletedMailboxes().size();
            Utils.print(String.format("%s mailboxes left", mailboxesLeft));
            Utils.print(String.format("Timer state: %s", _timer.getState().name()));
            // _player.getScoreKeeper().print();
            // If the timer is running and no mailboxes are left, we've completed the level
            if (mailboxesLeft == 0) {
                handleLevelCompleted();
            } else if ((_timer.getState() == TimerState.STOPPED && mailboxesLeft > 0)
                    || _player.getScoreKeeper().hasExceededBudget()) {
                handleLevelFailed();
            } else {
                int numUninitializedMailboxes = _city.getUninitializedMailboxes().size();
                int randomMailboxIndex = new Random().nextInt(numUninitializedMailboxes);
                Mailbox mailboxToShow = _city.getUninitializedMailboxes().get(randomMailboxIndex);
                showMailbox(mailboxToShow);
            }
        }));
        _timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void pause() {
        _timer.pause();
    }

    public void resume() {
        _timer.resume();
    }

    public void setLevel(int levelNumber) {
        this._level = levelNumber;
    }

    public int getLevel() {
        return this._level;
    }

    public void start() {
        _player.render(_root, new GridPanePosition(13, 13)); // TODO: how do we detrmine start pos?
        _timer.start();
        _timeline.playFromStart();
    }

    private void handleLevelCompleted() {
        Utils.print(String.format("Level completed"));
        // TODO
        _timeline.stop();
        this._level += 1;
    }

    private void handleLevelFailed() {
        Utils.print(String.format("Level failed"));
        // TODO
        _timeline.stop();
        // Render game over!
    }

    private void showMailbox(Mailbox mailbox) {
        Utils.print(String.format("Showing mailbox at [%s, %s]", mailbox.getGridPanePosition().row, mailbox.getGridPanePosition().col));
        // mailbox.setDuration(Timer.gameMinutesToSeconds(60)); // 1 in-game hour
        mailbox.render();
        mailbox.show();
        mailbox.addStatusListener(new ChangeListener<MailboxStatus>() {
            @Override
            public void changed(ObservableValue<? extends MailboxStatus> observable, MailboxStatus oldStatus,
                    MailboxStatus newStatus) {
                System.out.println("Status changed: " + newStatus);
                // Call a method or do something else based on the new value
                if (newStatus == MailboxStatus.IN_PROGRESS) {
                    handleMailboxSelected(mailbox);
                }
                if (newStatus == MailboxStatus.COMPLETED) {
                    handleMailboxCompleted();
                }
                if (newStatus == MailboxStatus.FAILED) {
                    handleMailboxFailed();
                }
            }
        });
    }

    private void handleMailboxSelected(Mailbox mailbox) {
        Utils.print(String.format("Mailbox selected"));
        // TODO: we should be checking that NO other mailboxes are in progress here
        if (mailbox.getStatus() != MailboxStatus.IN_PROGRESS) {
            return;
        }
        // Calculate trips from player to mailbox
        ArrayList<Trip> trips = _tripCalculator.calculateTrips(_player.getGridPanePosition().row,
                _player.getGridPanePosition().col, mailbox.getGridPanePosition().row,
                mailbox.getGridPanePosition().col);
        Utils.print(String.format("Found trips!"));
        _currentTrip = trips.get(0);
        // TODO: Filter out trips that are too slow to reach mailbox?
        _tripChooser.setTrips(trips);
        _tripChooser.render();
        mailbox.markInProgress();
    }

    private void handleMailboxCompleted() {
        ScoreKeeper scoreKeeper = _player.getScoreKeeper();
        scoreKeeper.setMailboxesCompleted(scoreKeeper.getMailboxesCompleted() + 1);
        scoreKeeper.incrementCO2Used((int) _currentTrip.getEmission());
        scoreKeeper.print();
    }

    private void handleMailboxFailed() {
        // TODO?
    }
}
