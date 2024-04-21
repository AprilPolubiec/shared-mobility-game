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

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private Mailbox _currentMailbox;
    private Timeline _timeline;
    private ChooseTripComponent _tripChooser;
    private SaveLoad _saveLoad;
    private int mailboxesLeft;
    private ScoreKeeper scoreKeeper;

    // TODO: a constructor for loading from a saved game
    public Game(SaveLoad savedGame) {
        this._saveLoad = savedGame;
    }

    public Game(AnchorPane root, City city, Player player) {
        Utils.print(String.format("Creating a game"));
        this._player = player;
        this._city = city;
        this._root = root;
        
        this._tripCalculator = new TripCalculator(this._city);

        this._level = 0; // TODO: or pull in from the loader

        initializeTimer();
        initializeScoreKeeper();
        initializeTripChooser();
        initializeGameLoop();
    }

    private void initializeTimer() {
        this._timer = new Timer();
        this._timer.render(_root);
    }

    private void initializeScoreKeeper() {
        // TODO - unless its being loaded?
        this._player.getScoreKeeper().setLevel(0);
        this._player.getScoreKeeper().setTotalMailboxes(_city.getMailboxes().size());
        // this._saveLoad = new SaveLoad(this._player.getScoreKeeper());
        this._player.getScoreKeeper().render(_root);
    }

    private void initializeTripChooser() {
        _tripChooser = new ChooseTripComponent(_root);
        _tripChooser.onSelectedTripChanged(new ChangeListener<Trip>() {
            @Override
            public void changed(ObservableValue<? extends Trip> observable, Trip oldValue, Trip newValue) {
                handleTripSelected(newValue);
            }
        });
    }

    private void handleTripSelected(Trip selectedTrip) {
        _currentTrip = selectedTrip;
        _timer.resume();
        _player.moveOnRoute(selectedTrip.getNodeList());
        _tripChooser.clear();
    }

    // TODO: this will take in whatever is loaded by the loader and initialize from
    // there
    // public void loadExisting() {
    //     this._player.loadExisting();
    //     return;
    // }

    private void initializeGameLoop() {
        Utils.print(String.format("Starting game loop"));
        _timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            
            // Get the mailboxes that have not been rendered yet
            int numMailboxes = _city.getMailboxes().size();
            int mailboxesLeft = numMailboxes - _city.getFailedOrCompletedMailboxes().size();
            Utils.print(String.format("%s mailboxes left", mailboxesLeft));
            Utils.print(String.format("Timer state: %s", _timer.getState().name()));

            // No more mailboxes are left - we've completed the level
            if (mailboxesLeft == 0) {
                handleLevelCompleted();
            // If timer has stopped with mailboxes left over or the player exceeded CO2, level failed
            } else if ((_timer.getState() == TimerState.STOPPED && mailboxesLeft > 0)
                    || _player.getScoreKeeper().hasExceededBudget()) {
                handleLevelFailed();
            // If the timer is running, we can show a random mailbox
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
        int numMailboxes = _city.getMailboxes().size();
        int mailboxesLeft = numMailboxes - _city.getFailedOrCompletedMailboxes().size();
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
        if (isLevelOver()) {
            _saveLoad.save("game_state.dat");
            _timeline.stop();
            this._level += 1;
            // dosomething()
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
    }

    private void handleLevelFailed() {
        Utils.print(String.format("Level failed"));
        if (isLevelOver()) {
            _saveLoad.save("game_state.dat");
            _timeline.stop();
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
        // Render game over!
    }

    private void showMailbox(Mailbox mailbox) {
        Utils.print(String.format("Showing mailbox at [%s, %s]", mailbox.getGridPanePosition().row,
                mailbox.getGridPanePosition().col));

        mailbox.render();
        mailbox.show();
        mailbox.addStatusListener(new ChangeListener<MailboxStatus>() {
            @Override
            public void changed(ObservableValue<? extends MailboxStatus> observable, MailboxStatus oldStatus,
                    MailboxStatus newStatus) {
                System.out.println("Status changed: " + newStatus);
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

    // TODO: handle switching between selecting different mailboxes - right now the trip selector renders on top of each other,need to hide
    private void handleMailboxSelected(Mailbox mailbox) {
        Utils.print(String.format("Mailbox selected"));

        if (_player.getStatus() == PlayerStatus.ON_TRIP) {
            return;
        }

        if (_currentMailbox != null && _currentMailbox != mailbox) {
            _currentMailbox.markWaiting();
        }
        _currentMailbox = mailbox;

        // Pause the clock
        pause();
        // Calculate trips from player to mailbox
        ArrayList<Trip> trips = _tripCalculator.calculateTrips(_player.getGridPanePosition().row,
                _player.getGridPanePosition().col, mailbox.getGridPanePosition().row,
                mailbox.getGridPanePosition().col);
        Utils.print(String.format("Found trips!"));

        // Render the trips in the trip chooser
        _tripChooser.setTrips(trips);
        _tripChooser.render();

        // Listen for when the player has started and completed their trip
        _player.addStatusListener(new ChangeListener<PlayerStatus>() {
            @Override
            public void changed(ObservableValue<? extends PlayerStatus> observable, PlayerStatus oldValue,
                    PlayerStatus newValue) {
                if (newValue == PlayerStatus.ON_TRIP) {
                    mailbox.markInProgress();
                }
                if (newValue == PlayerStatus.IDLE) {
                    // Mailbox is compeleted - this could be so much better :')
                    handleTripCompleted();
                }
            }

        });
    }

    private void handleTripCompleted() {
        _currentMailbox.markComplete();
        // _currentTrip.getEmission();
        // progressBar.setEmission();
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
        return getMailboxesLeft() == 0 || getTimer().getState() == TimerState.STOPPED;
    }

}
