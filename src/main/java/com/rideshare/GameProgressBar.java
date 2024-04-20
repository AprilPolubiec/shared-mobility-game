package com.rideshare;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ProgressBar;

public class GameProgressBar extends ProgressBar {

    private int totalMailboxes;
    private DoubleProperty progress;


    public GameProgressBar(double progress, int totalMailboxes) {
        super(progress);
        this.totalMailboxes = totalMailboxes;
    }

    public void setGameProgressBar(double progress2){
        this.progress = progress;
    }

    public void updateProgress(int completedMailboxes) {
        double progress = (double) completedMailboxes / totalMailboxes;
        setGameProgressBar(progress);
    }

    // so methods for a progressbar
    // setEmission
    // getMailboxes completed from the player
    // a render method
    // update by increment/ some sort of calculation
    // initialize ProgressBar
    // 


}
