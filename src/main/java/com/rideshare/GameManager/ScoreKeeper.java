package com.rideshare.GameManager;

import com.rideshare.Utils;
import com.rideshare.City.Mailbox;
import com.rideshare.TileManager.TileUtils;
import com.rideshare.Trip.Trip;
import com.rideshare.UI.UIComponentUtils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Description: A ScoreKeeper is an object whose main function is to store
 * information about a player’s score.
 * Attributes:
 * CO2Budget (int, constant): Represents the maximum allowable carbon emissions
 * within the game environment
 * CO2Used (int): Represents the quantity of carbon emissions generated by the
 * player's activities.
 * mailboxesCompleted (int): Counter for each successful trip to a Mailbox in
 * the current level
 * totalMailboxes (int): Total number of mailboxes that need to be completed in
 * the current level
 * Level(int): Current level that the user is on
 * hasExceededBudget
 * amountOverBudget
 * Methods:
 * Constructor
 * incrementCO2Used (int): This method decreased the CO2Used by the specified
 * amount
 */

public class ScoreKeeper {
    private final int maxCo2Budget = 8500; // NOTE: budget - average driver does about 160km per day, electric vehicle
                                           // emits 53 per km. budget = 160*53 = 8480
    int co2Used;
    private int mailboxesCompleted;
    private int totalMailboxes;
    private int minutesLeft;
    double mailboxesMultiplier;
    int score;
    Integer scoreWithTimeBonus;
    int level;
    boolean exceededBudgetFlag;
    int amountOverBudget;
    private String playerName;
    private ProgressBar emissionsProgressBar;

    private Text scoreText;
    private Text mailboxText;
    private Text co2Text;

    // The ScoreKeeper constructor
    public ScoreKeeper() {
        this.co2Used = 0;
        this.mailboxesCompleted = 0;
        this.totalMailboxes = 0;
        this.level = 0;
        this.exceededBudgetFlag = false;
        this.amountOverBudget = 0;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void render(AnchorPane root) {
        AnchorPane scorekeeperPane = UIComponentUtils.createStyledDialog(200.0, 300.0);
        AnchorPane.setBottomAnchor(scorekeeperPane, 150.0);
        AnchorPane.setLeftAnchor(scorekeeperPane, TileUtils.TILE_SIZE_IN_PIXELS * 30.0);

        VBox scoreVbox = new VBox();
        AnchorPane.setTopAnchor(scoreVbox, 50.0);
        AnchorPane.setLeftAnchor(scoreVbox, 30.0);

        Text playerNameText = new Text(String.format("Player: %s", playerName));
        playerNameText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(playerNameText);

        Text levelText = new Text(String.format("Level: %s", level));
        levelText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(levelText);

        this.mailboxText = new Text(String.format("%s/%s Mailboxes", this.mailboxesCompleted, this.totalMailboxes));
        this.mailboxText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(mailboxText);

        this.scoreText = new Text(String.format("Score: %s", 0));
        this.scoreText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(scoreText);

        this.co2Text = new Text(String.format("CO2 Used: %s/%s", 0, this.maxCo2Budget));
        this.co2Text.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(co2Text);

        scorekeeperPane.getChildren().add(scoreVbox);
        root.getChildren().add(scorekeeperPane);
    }

    public int getMailboxesCompleted() {
        return this.mailboxesCompleted;
    }

    public void renderEmissionsProgressBar(AnchorPane root) {
        this.emissionsProgressBar = new ProgressBar(0);
        emissionsProgressBar.setProgress(1);

        VBox vBox = new VBox(emissionsProgressBar);
        emissionsProgressBar.setPrefWidth(TileUtils.TILE_SIZE_IN_PIXELS * 30);
        emissionsProgressBar.setPrefHeight(50);

        emissionsProgressBar.setStyle("-fx-accent: #fa8132;");
        AnchorPane.setBottomAnchor(vBox, 0.0);

        root.getChildren().add(vBox);
    }

    public void setMailboxesCompleted(int numCompleted) {
        if (numCompleted > totalMailboxes) {
            throw new IllegalArgumentException("Attempted to complete more mailboxes than exist.");
        }
        this.mailboxesCompleted = numCompleted;
        this.mailboxText.setText(String.format("%s/%s Mailboxes", this.mailboxesCompleted, this.totalMailboxes));
    }

    public int getTotalMailboxes() {
        return this.totalMailboxes;
    }

    public void setTotalMailboxes(int total) {
        this.totalMailboxes = total;
    }

    public int getLevel() {
        return this.level;
    }

    public int updateLevel() {
        this.level += 1;
        return this.level;
    }

    public String getMapName() {
        return String.format("level-%s", this.level + 1);
    }

    public int getCo2Used() {
        return this.co2Used;
    }

    public int incrementCO2Used(int incrementValue) {
        if (incrementValue < 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }

        this.co2Used += incrementValue;
        if (!this.hasExceededBudget()) {
            this.co2Text.setText(String.format("CO2 Used: %s/%s", this.co2Used, this.maxCo2Budget));
            updateProgressBar();
        }

        return this.co2Used;
    }

    public void updateProgressBar() {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(emissionsProgressBar.progressProperty(),
                1.0 - ((double) co2Used / (double) maxCo2Budget));
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    // Don't know if we need this so I'll just leave it here. I imagine level
    // incrementer
    public int setLevel(int newLevel) {
        if (newLevel < 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }

        this.level = newLevel;
        return this.level;
    }

    // budget checkers
    public boolean hasExceededBudget() {
        return this.co2Used > this.maxCo2Budget;
    }

    public int calculateLevelScore(int minutesLeft) {
        // For each minute thats left - get 10 more points
        Utils.print(String.format("Adding %s for minutes left", 10 * minutesLeft));
        scoreWithTimeBonus = this.score + (10 * minutesLeft);
        return scoreWithTimeBonus;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Faster you get a mailbox, the more it is worth
    public void updateScore(Mailbox mailbox, Trip trip) {
        int timeLeftOnMailbox = mailbox.getTimeLeft(); // This we want as high as possible
        int tripEmission = (int) trip.getEmission(); // This we want as low as possible
        // 100 points per second left on the mailbox
        // Plus the proportion of the total budget that was unused
        this.score += (timeLeftOnMailbox * 100) + (int) ((1.0 - ((double) tripEmission / maxCo2Budget)) * 100);
        this.scoreText.setText(String.format("Score: %s", this.score));
        Utils.print(String.format("Updating score: (%s * 100) + (int)((1.0 - (%s / %s)) * 100) = %s", timeLeftOnMailbox,
                tripEmission, maxCo2Budget, this.score));
    }

    public int getCurrentScore(int score) {
        return this.score;
    }

    public Integer getFinalLevelScore() {
        if (this.scoreWithTimeBonus == null) {
            throw new IllegalArgumentException("Final level score has not been calculated yet!");
        }
        return this.scoreWithTimeBonus;
    }

    public void setCo2Used(int setC02Used) {
        this.co2Used = setC02Used;
    }

    public void print() {
        Utils.print("SCOREKEEPER");
        Utils.print(String.format("Mailboxes completed: %s/%s", mailboxesCompleted, totalMailboxes));
        Utils.print(String.format("CO2 used: %s/%s", co2Used, maxCo2Budget));
        Utils.print(String.format("Score: %s", this.score));
    }
}