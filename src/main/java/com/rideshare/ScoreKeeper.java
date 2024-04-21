package com.rideshare;

import com.rideshare.TileManager.TileUtils;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    // All relevant attributes initialised
    // The CO2 budget is just a random value here.
    private final int maxCo2Budget = 8500; // NOTE: budget - average driver does about 160km per day, electric vehicle
                                           // emits 53 per km. budget = 160*53 = 8480
    // co2Budget here is a 'more local variable' allowed to go into the negatives in
    // order to check if the budget has been exceeded
    int co2Budget = 8500;
    int co2Used;
    private int mailboxesCompleted;
    private int totalMailboxes;
    private int minutesLeft;
    double mailboxesMultiplier;
    int score;
    int level;
    boolean exceededBudgetFlag;
    int amountOverBudget;
    private ProgressBar progressBar;

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
        this.progressBar = new ProgressBar();
    }

    public void render(AnchorPane root) {
        AnchorPane scorekeeperPane = UIComponentUtils.createStyledDialog(300.0, 300.0);
        AnchorPane.setBottomAnchor(scorekeeperPane, 150.0);
        AnchorPane.setLeftAnchor(scorekeeperPane, TileUtils.TILE_SIZE_IN_PIXELS * 30.0);

        VBox scoreVbox = new VBox();
        AnchorPane.setTopAnchor(scoreVbox, 50.0);
        AnchorPane.setLeftAnchor(scoreVbox, 50.0);

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

    public void renderProgressBar(AnchorPane root) {
        AnchorPane progressBarPane = new AnchorPane();
        AnchorPane.setBottomAnchor(progressBarPane, 450.0);
        AnchorPane.setRightAnchor(progressBarPane, 140.0);
        progressBarPane.setStyle("-fx-background-color: grey;");
        progressBarPane.setPrefSize(300, 30);

        VBox progressBarVbox = new VBox();

        this.progressBar.setPrefWidth(300);
        this.progressBar.setPrefHeight(30);
        this.progressBar.setProgress(0.00);
        this.progressBar.setStyle("-fx-accent: #fa8132;");
        progressBarVbox.getChildren().add(this.progressBar);

        progressBarPane.getChildren().add(progressBarVbox);

        root.getChildren().add(progressBarPane);
    }

    public void updateProgressBar() {
        double progress = (double) mailboxesCompleted / totalMailboxes;
        progressBar.setProgress(progress);

    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
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
        this.hasExceededBudget();

        this.co2Used += incrementValue;
        this.co2Text.setText(String.format("CO2 Used: %s/%s", this.co2Used, this.maxCo2Budget));
        return this.co2Used;
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
        if (this.co2Used > this.co2Budget) {
            this.exceededBudgetFlag = true;
            // CO2Used = co2Budget;
        }
        return this.exceededBudgetFlag;
    }

    public int getAmountOverBudget() {
        if (this.hasExceededBudget()) {
            return this.co2Used - this.co2Budget;
        } else {
            return 0;
        }
    }

    public int resetOverBudget() {
        if (this.hasExceededBudget()) {
            this.co2Used = this.co2Budget;
            this.exceededBudgetFlag = false;
            return this.co2Budget;
        }
        return this.co2Used;
    }

    public int calculateLevelScore(int minutesLeft) {
        // For each minute thats left - get 10 more points
        Utils.print(String.format("Adding %s for minutes left", 10 * minutesLeft));
        return this.score + (10 * minutesLeft);
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
        this.score = (timeLeftOnMailbox * 100) + (int) ((1.0 - ((double) tripEmission / co2Budget)) * 100);
        this.scoreText.setText(String.format("Score: %s", this.score));
        Utils.print(String.format("Updating score: (%s * 100) + (int)((1.0 - (%s / %s)) * 100) = %s", timeLeftOnMailbox,
                tripEmission, co2Budget, this.score));
    }

    public int getScore(int score) {
        return this.score;
    }

    public void setCo2Used(int setC02Used) {
        this.co2Used = setC02Used;
    }

    public void print() {
        Utils.print("SCOREKEEPER");
        Utils.print(String.format("Mailboxes completed: %s/%s", mailboxesCompleted, totalMailboxes));
        Utils.print(String.format("CO2 used: %s/%s", co2Used, co2Budget));
        Utils.print(String.format("Score: %s", this.score));
    }
}
