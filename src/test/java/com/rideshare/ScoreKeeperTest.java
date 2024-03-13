package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ScoreKeeperTest {
    @Test
    public void Test_CanCreateScoreKeeper() {
        int co2Budget = 10;
        int totalMailboxes = 10;
        // TODO: this doesn't have to be what the constructor looks like, tbd!
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        assertEquals(0, scoreKeeper.getMailboxesCompleted());
        assertEquals(totalMailboxes, scoreKeeper.getTotalMailboxes());
        assertEquals(0, scoreKeeper.getCO2Used());
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
        int newCO2 = scoreKeeper.incrementCO2Used(co2Budget + 1);
        boolean hasExceededBudget = scoreKeeper.hasExceededBudget();
        int amountOverBudget = scoreKeeper.getAmountOverBudget();
        
        assertEquals(co2Budget, newCO2);
        assertEquals(true, hasExceededBudget);
        assertEquals(1, amountOverBudget);
    }

    public void Test_CanCalculateScore() {
        // Leave this TODO for whoever implements the class
        throw new UnsupportedOperationException("Not implemented");
    }
}
