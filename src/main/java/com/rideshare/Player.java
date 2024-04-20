package com.rideshare;

import com.rideshare.GameManager.Sprite;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author April Polubec <aprilpdev@gmail.com>
 *         <p>
 * 
 *         <p>
 */
public class Player extends Sprite {
    private String name;
    private ScoreKeeper scoreKeeper;
    private GameProgressBar progressBar;

    public Player(String name, String avatar) {
        super(avatar);
        this.name = name;
        this.scoreKeeper = new ScoreKeeper();
    }

    public void loadExisting() {
        // do things
    }

    public void render(AnchorPane root, GridPanePosition startPosition) {
        super.render(root, startPosition);
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public GameProgressBar getProgressBar() {
        return progressBar;
    }
}