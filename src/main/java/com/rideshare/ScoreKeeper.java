package com.rideshare;

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
    static final int maxCo2Budget = 10;
    // co2Budget here is a 'more local variable' allowed to go into the negatives in order to check if the budget has been exceeded
    int co2Budget;
    int CO2Saved;
    int CO2Used;
    int mailboxesCompleted;
    int totalMailboxes;
    double mailboxesMultiplier;
    int score;
    int Level;
    boolean exceededBudgetFlag;
    int amountOverBudget;

    // The ScoreKeeper constructor
    public ScoreKeeper() {
        CO2Saved = 0;
        CO2Used = 0;
        mailboxesCompleted = 0;
        totalMailboxes = 0;
        Level = 1;
        exceededBudgetFlag = false;
        amountOverBudget = 0;
    }

    // getters
    public int getMailboxesCompleted() {
        return mailboxesCompleted;
    }

    public int getTotalMailboxes() {
        return totalMailboxes;
    }

    public int getCO2Saved() {
        return CO2Saved;
    }

    public int getCO2Used() {
        return CO2Used;
    }

    
    // setters
    public int incrementCO2Saved(int valueIncremented) {
        if (valueIncremented <= 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }
        CO2Saved += valueIncremented;
        return CO2Saved;
    }

    public int incrementCO2Used(int valueIncremented) {
        if (valueIncremented <= 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }
        hasExceededBudget(); 

        CO2Used += valueIncremented;
        return CO2Used;
    }

    // Don't know if we need this so I'll just leave it here. I imagine level incrementer
    public int setLevel(int newLevel) {
        if (newLevel <= 0) {
            throw new IllegalArgumentException("Input value must be a positive integer");
        }

        Level = newLevel;
        return Level;
    }
    

    // budget checkers
    public boolean hasExceededBudget() {
        if (CO2Used > co2Budget) {
            exceededBudgetFlag = true;
            // CO2Used = co2Budget;
        }
        return exceededBudgetFlag;
    }

    public int getAmountOverBudget() {
        if (hasExceededBudget()) {
            return CO2Used - co2Budget;
        } else {
            return 0;
        }

    }

    public int resetOverBudget() {
        if (hasExceededBudget()) {
            CO2Used = co2Budget;
            exceededBudgetFlag = false;
            return co2Budget;
        } 
        return CO2Used;
    }

    public int calculateScore() {
        mailboxesCompleted = getMailboxesCompleted();
        totalMailboxes = getTotalMailboxes();
        int CO2score = CO2Saved;

        double mailboxesRatio = (double) mailboxesCompleted / totalMailboxes;
        mailboxesMultiplier = mailboxesRatio + 1;

        double scoreDouble = CO2score * mailboxesMultiplier;
        score = (int) Math.ceil(scoreDouble);

        exceededBudgetFlag = hasExceededBudget();
        if (exceededBudgetFlag == true) {
            score = 0;
            return score;
        }

        return score;
    }






}