package com.rideshare.GameManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.rideshare.GameManager.ScoreKeeper;

public class ScoreKeeperTest {
    @Test
    public void Test_CanCreateScoreKeeper() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        scoreKeeper.totalMailboxes = 10;
        // scoreKeeper.co2Budget = 10; - it's a constant variable
        assertEquals(0, scoreKeeper.getMailboxesCompleted());
        assertEquals(10, scoreKeeper.getTotalMailboxes());
        assertEquals(0, scoreKeeper.getCo2Used());
        assertEquals(0, scoreKeeper.getCO2Saved());
    }

    @Test
    public void Test_CanIncrementCO2Used() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        int newCO2 = scoreKeeper.incrementCO2Used(1);
        assertEquals(1, newCO2);
    }

    @Test
    public void Test_CanIncrementCO2Saved() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        int newSavedCO2 = scoreKeeper.incrementCO2Saved(1);
        assertEquals(1, newSavedCO2);
    }

    @Test
    public void Test_CanOnlyIncrementWithPositiveIntegers() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        assertThrows(Exception.class, () -> scoreKeeper.incrementCO2Used(-1));
        assertThrows(Exception.class, () -> scoreKeeper.incrementCO2Saved(-1));
    }

    @Test
    public void Test_CannotExceedCO2Budget() {
        int co2Budget = 10;
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        scoreKeeper.co2Budget= 10;
        int newCO2 = scoreKeeper.incrementCO2Used(co2Budget + 1);
        boolean hasExceededBudget = scoreKeeper.hasExceededBudget();
        int amountOverBudget = scoreKeeper.getAmountOverBudget();
        
        assertEquals(true, hasExceededBudget);
        assertEquals(1, amountOverBudget);

        newCO2 = scoreKeeper.resetOverBudget();
    
        assertEquals(co2Budget, newCO2);
    }


    // Note: This is my version of calculating the score in other words not generalised
    // If we move with a different implementation this will have to be changed.
    // this.mailboxesCompleted = this.getMailboxesCompleted();
    // this.totalMailboxes = this.getTotalMailboxes();
    // int CO2score = this.CO2Saved;

    // double mailboxesRatio = (double) this.mailboxesCompleted / this.totalMailboxes;
    // // mailboxesMultiplier = mailboxesRatio + 1;
    // this.mailboxesMultiplier = mailboxesRatio;

    // double scoreDouble = CO2score * this.mailboxesMultiplier;
    // this.score = (int) Math.ceil(scoreDouble);

    // this.exceededBudgetFlag = this.hasExceededBudget();
    // if (this.exceededBudgetFlag == true) {
    //     this.score = 0;
    //     return this.score;
    // }

    // this.scoreText.setText(String.format("Score: %s", this.score));
    // return this.score;

    @Test
    public void Test_CanCalculateScore() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        scoreKeeper.totalMailboxes = 10;
        scoreKeeper.mailboxesCompleted = 5;
        scoreKeeper.incrementCO2Saved(25);
        int result = scoreKeeper.calculateLevelScore();

        assertEquals(25, scoreKeeper.CO2Saved);
        assertEquals(10, scoreKeeper.getTotalMailboxes());
        assertEquals(5, scoreKeeper.getMailboxesCompleted());
        assertEquals(1.5, scoreKeeper.mailboxesMultiplier);
        assertEquals(38, result);
    }

    @Test
    public void Test_CanCalculateScoreWhenBudgetExceeds() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        int co2Budget = 20;
        scoreKeeper.totalMailboxes = 10;
        scoreKeeper.mailboxesCompleted = 5;

        scoreKeeper.incrementCO2Used(co2Budget + 1);
        scoreKeeper.incrementCO2Saved(20);
        
        assertEquals(0, scoreKeeper.calculateLevelScore());

    }
}
