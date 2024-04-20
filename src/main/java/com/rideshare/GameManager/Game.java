package com.rideshare.GameManager;

import java.util.ArrayList;
import java.util.Random;

import com.rideshare.ChooseTripComponent;
import com.rideshare.City;
import com.rideshare.GridPanePosition;
import com.rideshare.Mailbox;
import com.rideshare.MailboxStatus;
import com.rideshare.Player;
import com.rideshare.PlayerStatus;
import com.rideshare.ScoreKeeper;
import com.rideshare.Timer;
import com.rideshare.TimerState;
import com.rideshare.Trip;
import com.rideshare.TripCalculator;
import com.rideshare.Utils;
import com.rideshare.SaveManager.SaveLoad;
import com.rideshare.SaveManager.DataStorage;

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
    private SaveLoad _saveLoad;
    private ScoreKeeper scoreKeeper;
    private int mailboxesLeft;

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
                _tripChooser.clear();
            }
        });

        initializeGameLoop();

        this.scoreKeeper = new ScoreKeeper();
        this._saveLoad = new SaveLoad(this.scoreKeeper);
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
            } else if (_timer.getState() == TimerState.RUNNING) {
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

    public int getMailboxesLeft() {
        return mailboxesLeft;
    }

    public Timer getTimer() {
        return this._timer;
    }

    public void start() {
        _player.render(_root, new GridPanePosition(13, 13)); // TODO: how do we detrmine start pos?
        _timer.start();
        _timeline.playFromStart();
    }

    private void handleLevelCompleted() {
        Utils.print(String.format("Level completed"));
        // TODO
        // making a new datastorage object to pass as argument when calling save()
        DataStorage ds = new DataStorage();

        ds.score = scoreKeeper.calculateScore();
        ds.mailboxesCompleted = scoreKeeper.getMailboxesCompleted();
        ds.level = scoreKeeper.getLevel();
        ds.CO2Saved = scoreKeeper.getCO2Saved();
        ds.CO2Used = scoreKeeper.getCO2Used();
        ds.totalMailboxes = scoreKeeper.getTotalMailboxes();

        if (isLevelOver()) {
            _saveLoad.save("game_state.dat", ds);
            _timeline.stop();
            this._level += 1;
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
    }

    private void handleLevelFailed() {
        Utils.print(String.format("Level failed"));
        // TODO
        DataStorage ds = new DataStorage();
        ds.score = scoreKeeper.calculateScore();
        ds.mailboxesCompleted = scoreKeeper.getMailboxesCompleted();
        ds.level = scoreKeeper.getLevel();
        ds.CO2Saved = scoreKeeper.getCO2Saved();
        ds.CO2Used = scoreKeeper.getCO2Used();
        ds.totalMailboxes = scoreKeeper.getTotalMailboxes();

        if (isLevelOver()) {
            _saveLoad.save("game_state.dat", ds);
            _timeline.stop();
            this._level += 1;
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
        // Render game over!
    }

    private void showMailbox(Mailbox mailbox) {
        Utils.print(String.format("Showing mailbox at [%s, %s]", mailbox.getGridPanePosition().row,
                mailbox.getGridPanePosition().col));
        // mailbox.setDuration(Timer.gameMinutesToSeconds(60)); // 1 in-game hour
        mailbox.render();
        mailbox.show();
        mailbox.addStatusListener(new ChangeListener<MailboxStatus>() {
            @Override
            public void changed(ObservableValue<? extends MailboxStatus> observable, MailboxStatus oldStatus,
                    MailboxStatus newStatus) {
                System.out.println("Status changed: " + newStatus);
                // Call a method or do something else based on the new value
                if (newStatus == MailboxStatus.SELECTED) {
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

    // TODO: handle switching between selecting different mailboxes
    private void handleMailboxSelected(Mailbox mailbox) {
        Utils.print(String.format("Mailbox selected"));

        if (_player.getStatus() == PlayerStatus.ON_TRIP) {
            return;
        }
        pause();
        // Calculate trips from player to mailbox
        ArrayList<Trip> trips = _tripCalculator.calculateTrips(_player.getGridPanePosition().row,
                _player.getGridPanePosition().col, mailbox.getGridPanePosition().row,
                mailbox.getGridPanePosition().col);
        Utils.print(String.format("Found trips!"));
        _currentTrip = trips.get(0);
        _tripChooser.setTrips(trips);
        _tripChooser.render();

        _player.addStatusListener(new ChangeListener<PlayerStatus>() {
            @Override
            public void changed(ObservableValue<? extends PlayerStatus> observable, PlayerStatus oldValue,
                    PlayerStatus newValue) {
                if (newValue == PlayerStatus.ON_TRIP) {
                    mailbox.markInProgress();
                }
                if (newValue == PlayerStatus.IDLE) {
                    // Mailbox is compeleted - this could be so much better :')
                    mailbox.markComplete();
                }
            }

        });
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

    private boolean isLevelOver() {
        int mailboxesLeft = getMailboxesLeft();
        if (mailboxesLeft == 0) {
            return true;
        } else if (getTimer().getState() == TimerState.STOPPED) {
            return true;
        } else {
            // The level is not over
            System.out.println("Level incomplete");
            return false;
        }
    }
}
