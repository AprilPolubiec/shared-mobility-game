package com.rideshare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rideshare.GameManager.Player;
import com.rideshare.SaveManager.DataStorage;
import com.rideshare.SaveManager.SaveLoad;

public class SaveLoadTest {
    // TODO: these are NOT good tests - do not use
    @Test
    public void Test_SaveFile() {
        SaveLoad sl = new SaveLoad();
        sl.saveFile(new DataStorage(), "game_data/April/1.dat");
    }
    @Test
    public void Test_SavePlayer() {
        SaveLoad sl = new SaveLoad();
        Player p = new Player("April", "girl-1");
        p.getScoreKeeper().calculateLevelScore(100);
        sl.save(p);
    }
    @Test
    public void Test_GetExistingGames() {
        SaveLoad sl = new SaveLoad();
        int existing = sl.getExistingGames();
        assertEquals(1, existing);
    }
}
