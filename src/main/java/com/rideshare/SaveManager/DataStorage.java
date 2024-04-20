package com.rideshare.SaveManager;

// https://www.youtube.com/watch?v=AHgqcvuHBRA - followed this tutorial by RyiSnow!
import java.io.Serializable;

public class DataStorage implements Serializable {

    public String spriteName;
    public String name;
    public int score;
    public int mailboxesCompleted;
    public int totalMailboxes;
    public int level;
    public int CO2Saved = 0;
    public int CO2Used = 0;
    // made public so they can be accessed from game class

}