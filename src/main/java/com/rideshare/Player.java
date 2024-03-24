package com.rideshare;

import com.rideshare.GameManager.GameController;
import com.rideshare.GameManager.Sprite;

import javafx.scene.image.ImageView;

/**
 * @author      April Polubec <aprilpdev@gmail.com>
 * <p>
 * 
 * <p>
 */
public class Player extends Sprite {
    private double xPosition; 
    private double yPosition;
    private ImageView avatar;
    private String name;
    private ScoreKeeper scoreKeeper;
    private GameController gameController;
   
    

    public Player(double xPosition, double yPosition, ImageView avatar, String name,GameController gameController){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.avatar = avatar;
        this.name = name;
        this.scoreKeeper = new ScoreKeeper();
        this.gameController = gameController;
        this.sprite = new Sprite(name, gameController);
        this.sprite.setAssociatedPlayer(this);
    }

    // Methods
    //to set the x and y coordinates of the sprite
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    
    // getter method for scorekeeper
    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }
}
