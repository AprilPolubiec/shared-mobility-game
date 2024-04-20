package com.rideshare;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/** 
 * Description: A ScoreKeeper is an object whose main function is to store information about a player’s score.
    Attributes:
        CO2Budget (int, constant): Represents the maximum allowable carbon emissions within the game environment
        CO2Saved (int): Represents the amount of carbon that the player has hypothetically “Saved” by the choice that they made
        CO2Used (int): Represents the quantity of carbon emissions generated by the player's activities.
        mailboxesCompleted (int): Counter for each successful trip to a Mailbox in the current level
        totalMailboxes (int): Total number of mailboxes that need to be completed in the current level
        Level(int): Current level that the user is on
        hasExceededBudget
        amountOverBudget
    Methods:
        Constructor
        incrementCO2Saved(int): This method updates the ‘CO2 Saved attribute by adding a specified amount to its current value.
        incrementCO2Used (int): This method decreased the CO2Used by the specified amount
       */

public class ScoreKeeper {
    // All relevant attributes initialised
    // The CO2 budget is just a random value here.
    static final int maxCo2Budget = 8500; // NOTE: budget - average driver does about 160km per day, electric vehicle emits 53 per km. budget = 160*53 = 8480
    // co2Budget here is a 'more local variable' allowed to go into the negatives in order to check if the budget has been exceeded
    int co2Budget = 8500;
    int CO2Saved;
    int CO2Used;
    private int mailboxesCompleted;
    private int totalMailboxes;
    double mailboxesMultiplier;
    int score;
    int level;
    boolean exceededBudgetFlag;
    int amountOverBudget;

    private Text scoreText;
    private Text mailboxText;

    // The ScoreKeeper constructor
    public ScoreKeeper() {
        this.CO2Saved = 0;
        this.CO2Used = 0;
        this.mailboxesCompleted = 0;
        this.totalMailboxes = 0;
        this.level = 0;
        this.exceededBudgetFlag = false;
        this.amountOverBudget = 0;
    }

    public void render(AnchorPane root) {
        AnchorPane scorekeeperPane = UIComponentUtils.createStyledDialog(300.0, 300.0);
        AnchorPane.setBottomAnchor(scorekeeperPane, 150.0);
        AnchorPane.setRightAnchor(scorekeeperPane, 0.0);
        
        VBox scoreVbox = new VBox();
        AnchorPane.setTopAnchor(scoreVbox, 50.0);
        AnchorPane.setLeftAnchor(scoreVbox, 50.0);

        this.mailboxText = new Text(String.format("%s/%s Mailboxes", this.mailboxesCompleted, this.totalMailboxes));
        this.mailboxText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(mailboxText);
        
        this.scoreText = new Text(String.format("Score: %s",0));
        this.scoreText.setFont(Font.font("Futura Bold", 21));
        scoreVbox.getChildren().add(scoreText);
        
        scorekeeperPane.getChildren().add(scoreVbox);
        root.getChildren().add(scorekeeperPane);
    }

    public int getMailboxesCompleted() {
        return this.mailboxesCompleted;
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

    public int getLevel(){
        return this.level;
    }

    public int getCO2Saved() {
        return this.CO2Saved;
    }

    public int getCO2Used() {
        return this.CO2Used;
    }

    // setters
    public int incrementCO2Saved(int incrementValue) {
        if (incrementValue < 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }
        this.CO2Saved += incrementValue;
        return this.CO2Saved;
    }

    public int incrementCO2Used(int incrementValue) {
        if (incrementValue < 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }
        this.hasExceededBudget(); 

        this.CO2Used += incrementValue;
        return this.CO2Used;
    }

    // Don't know if we need this so I'll just leave it here. I imagine level incrementer
    public int setLevel(int newLevel) {
        if (newLevel < 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }

        this.level = newLevel;
        return this.level;
    }

    // budget checkers
    public boolean hasExceededBudget() {
        if (this.CO2Used > this.co2Budget) {
            this.exceededBudgetFlag = true;
            // CO2Used = co2Budget;
        }
        return this.exceededBudgetFlag;
    }

    public int getAmountOverBudget() {
        if (this.hasExceededBudget()) {
            return this.CO2Used - this.co2Budget;
        } else {
            return 0;
        }
    }

    public int resetOverBudget() {
        if (this.hasExceededBudget()) {
            this.CO2Used = this.co2Budget;
            this.exceededBudgetFlag = false;
            return this.co2Budget;
        } 
        return this.CO2Used;
    }

    // TODO: fix this - its returning 0 (probably rounding down)
    public int calculateScore() {
        this.mailboxesCompleted = this.getMailboxesCompleted();
        this.totalMailboxes = this.getTotalMailboxes();
        int CO2score = this.CO2Saved;

        double mailboxesRatio = (double) this.mailboxesCompleted / this.totalMailboxes;
        // mailboxesMultiplier = mailboxesRatio + 1;
        this.mailboxesMultiplier = mailboxesRatio;

        double scoreDouble = CO2score * this.mailboxesMultiplier;
        this.score = (int) Math.ceil(scoreDouble);

        this.exceededBudgetFlag = this.hasExceededBudget();
        if (this.exceededBudgetFlag == true) {
            this.score = 0;
            return this.score;
        }

        this.scoreText.setText(String.format("Score: %s", this.score));
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setCO2Saved(int C02Saved) {
        this.CO2Saved = C02Saved;
    }

    public void setCO2Used(int setC02Used) {
        this.CO2Used = setC02Used;
    }

    public void print() {
        Utils.print("SCOREKEEPER");
        Utils.print(String.format("Mailboxes completed: %s/%s", mailboxesCompleted, totalMailboxes));
        Utils.print(String.format("CO2 used: %s/%s", CO2Used, co2Budget));
        Utils.print(String.format("Score: %s", calculateScore()));
    }
}
