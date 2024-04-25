package com.rideshare.GameManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.rideshare.GameManager.ScoreKeeper;

public class ScoreKeeperTest {
    @Test
    public void Test_CanCreateScoreKeeper() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        scoreKeeper.setTotalMailboxes(10);;
        // scoreKeeper.co2Budget = 10; - it's a constant variable
        assertEquals(0, scoreKeeper.getMailboxesCompleted());
        assertEquals(10, scoreKeeper.getTotalMailboxes());
        assertEquals(0, scoreKeeper.getCo2Used());
    }

    @Disabled
    @Test
    public void Test_CanIncrementCO2Used() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        int newCO2 = scoreKeeper.incrementCO2Used(1);
        assertEquals(1, newCO2);
    }

    @Test
    public void Test_CanOnlyIncrementWithPositiveIntegers() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        assertThrows(Exception.class, () -> scoreKeeper.incrementCO2Used(-1));
    }

    @Disabled
    @Test
    public void Test_CannotExceedCO2Budget() {
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        int newCO2 = scoreKeeper.incrementCO2Used(8501);
        boolean hasExceededBudget = scoreKeeper.hasExceededBudget();
        
        assertEquals(true, hasExceededBudget);
        assertEquals(8500, newCO2);
    }

    @Disabled
    @Test
    public void Test_CanCalculateScore() {
        // TODO: this is not the newest calculation logic
        ScoreKeeper scoreKeeper = new ScoreKeeper();
        scoreKeeper.setTotalMailboxes(10);
        scoreKeeper.setMailboxesCompleted(5);
        int result = scoreKeeper.calculateLevelScore(10);
        assertEquals(10, scoreKeeper.getTotalMailboxes());
        assertEquals(5, scoreKeeper.getMailboxesCompleted());
        assertEquals(38, result);
    }

    // TODO: more tests!
}
