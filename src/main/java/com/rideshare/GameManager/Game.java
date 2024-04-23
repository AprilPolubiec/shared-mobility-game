package com.rideshare.GameManager;

import java.util.ArrayList;
import java.util.Random;

import com.rideshare.ChooseTripComponent;
import com.rideshare.City;
import com.rideshare.EducationalPopup;
import com.rideshare.GameOverPopup;
import com.rideshare.GridPanePosition;
import com.rideshare.LevelCompletePopup;
import com.rideshare.Mailbox;
import com.rideshare.MailboxStatus;
import com.rideshare.Player;
import com.rideshare.PlayerStatus;
import com.rideshare.ScoreKeeper;
import com.rideshare.Timer;
import com.rideshare.TimerState;
import com.rideshare.Utils;
import com.rideshare.SaveManager.SaveLoad;
import com.rideshare.Trip.Trip;
import com.rideshare.Trip.TripCalculator;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game {
    private Player _player;
    private Timer _timer;
    private int _level; // TODO: remove me - lets just use the scorekeeper
    private City _city;
    private TripCalculator _tripCalculator;
    private AnchorPane _root;
    private Trip _currentTrip;
    private Mailbox _currentMailbox;
    private Timeline _timeline;
    private ChooseTripComponent _tripChooser;
    private EducationalPopup _educationalContent;
    private SaveLoad _saveLoad;
    private int mailboxesLeft;
    private ScoreKeeper scoreKeeper;

    public Game(AnchorPane root, City city, Player player) {
        Utils.print(String.format("Creating a game"));
        this._player = player;
        this._city = city;
        this._root = root;
        this._tripCalculator = new TripCalculator(this._city);
        _saveLoad = new SaveLoad();

        initializeTimer();
        initializeScoreKeeper();
        initializeTripChooser();
        initializeGameLoop();
        intializeEducationalContentContainer();

        // cl
        System.out.println(this._player.getScoreKeeper().getMapName());
    }

    private void renderLevelCompleted() {
        LevelCompletePopup l = new LevelCompletePopup();
        l.setScore(this._player.getScoreKeeper().calculateLevelScore((int) _timer.getMinutesLeft()));
        l.setEmission(this._player.getScoreKeeper().getCo2Used());
        l.setTime(this._timer.getTimeElapsedString());
        l.render(this._root);

        l.onNextLevelSelected(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.print("next level clicked!");
                loadMap();
                l.hide();
            }
        });
        l.onRepeatLevelSelected(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.print("next level clicked!");
                _player.getScoreKeeper().setLevel(_player.getScoreKeeper().getLevel() - 1);
                loadMap();
                l.hide();
            }
        });
    }

    private void renderGameOver() {
        GameOverPopup gameOverPopup = new GameOverPopup();
        gameOverPopup.render(this._root); 
        gameOverPopup.onRepeatLevelSelected(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.print("repeat level clicked!");
                _player.getScoreKeeper().setLevel(_player.getScoreKeeper().getLevel());
                loadMap();
                gameOverPopup.hide();
            }
        });
    }

    private void loadMap() {
        String nextMap = this._player.getScoreKeeper().getMapName();

        MapLoader loader = new MapLoader(_root.getScene());
        loader.load(nextMap);
        this._city.clear();
        this._city = loader.getCity();
        this._tripCalculator = new TripCalculator(this._city);

        initializeTimer();
        initializeScoreKeeper();
        initializeTripChooser();
        initializeGameLoop();
        intializeEducationalContentContainer();
        this.start();
    }


    private void initializeTimer() {
        this._timer = new Timer();
        this._timer.render(_root);
    }

    private void initializeScoreKeeper() {
        this._player.getScoreKeeper().render(_root);
        this._player.getScoreKeeper().setTotalMailboxes(_city.getMailboxes().size());
        this._player.getScoreKeeper().setMailboxesCompleted(0);
        this._level = this._player.getScoreKeeper().getLevel();
        this._player.getScoreKeeper().renderEmissionsProgressBar(_root);
    }

    private void intializeEducationalContentContainer() {
        _educationalContent = new EducationalPopup();
        _educationalContent.render(_root);
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
        _educationalContent.renderFact(selectedTrip.getTripType());
    }

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
                _timer.stop();
                handleLevelCompleted();
                // If timer has stopped with mailboxes left over
            } else if (_timer.getState() == TimerState.STOPPED && mailboxesLeft > 0) {
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
            _timeline.stop();
            this._level += 1;
            this._player.getScoreKeeper().updateLevel();
            renderLevelCompleted();
            _saveLoad.save(_player);
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
    }

    private void handleLevelFailed() {
        // TODO: block user from clicking anything
        Utils.print(String.format("Level failed"));
        if (isLevelOver()) {
            renderGameOver();
            _timeline.stop();
        } else {
            System.out.println("Level is incomplete, cannot save game state!");
        }
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
                System.out.println(String.format("Status changed for [%s %s]: %s ", mailbox.getGridPanePosition().row,
                        mailbox.getGridPanePosition().col, newStatus));
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

    // TODO: handle switching between selecting different mailboxes - right now the
    // trip selector renders on top of each other,need to hide
    private void handleMailboxSelected(Mailbox mailbox) {
        Utils.print(String.format("Mailbox selected"));

        if (_player.getStatus() == PlayerStatus.ON_TRIP) {
            mailbox.markWaiting();
            return;
        }

        if (_currentMailbox != null && _currentMailbox.getStatus() == MailboxStatus.SELECTED) {
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
        _tripChooser.clear(); // Clear any existing trips in there
        _tripChooser.setTrips(trips);
        _tripChooser.render();

        // Listen for when the player has started and completed their trip
        _player.addStatusListener(new ChangeListener<PlayerStatus>() {
            @Override
            public void changed(ObservableValue<? extends PlayerStatus> observable, PlayerStatus oldValue,
                    PlayerStatus newValue) {
                if (newValue == PlayerStatus.ON_TRIP) {
                    _currentMailbox.markInProgress();
                }
                if (newValue == PlayerStatus.IDLE) {
                    // Mailbox is compeleted - this could be so much better :')
                    handleTripCompleted();
                }
            }

        });
    }

    // Player state has changed to idle and stopped at the destination - mark mailbox completed
    private void handleTripCompleted() {
        _currentMailbox.markComplete();
        // _currentTrip.getEmission();
        // progressBar.setEmission();
    }

    // Mailbox state has changed to completed
    private void handleMailboxCompleted() {
        ScoreKeeper scoreKeeper = _player.getScoreKeeper();
        scoreKeeper.incrementCO2Used((int) _currentTrip.getEmission());
        if (scoreKeeper.hasExceededBudget()) {
            _timer.stop();
            handleLevelFailed();
            return;
        }
        scoreKeeper.setMailboxesCompleted(scoreKeeper.getMailboxesCompleted() + 1);
        scoreKeeper.updateScore(_currentMailbox, _currentTrip);
        scoreKeeper.print();
    }

    private void handleMailboxFailed() {
        // TODO?
    }

    private boolean isLevelOver() {
        return getMailboxesLeft() == 0 || getTimer().getState() == TimerState.STOPPED;
    }

}
