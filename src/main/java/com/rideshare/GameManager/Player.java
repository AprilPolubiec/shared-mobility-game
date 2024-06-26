package com.rideshare.GameManager;

import java.util.ArrayList;

import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.Trip.TransportationNode;

import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;

/**
 * @author April Polubec <aprilpdev@gmail.com>
 *         <p>
 * 
 *         <p>
 */
public class Player extends Sprite {
    private String name;
    private String avatarName;
    private ScoreKeeper scoreKeeper;
    private ObjectProperty<PlayerStatus> status = new SimpleObjectProperty<>();

    public Player(String name, String avatar) {
        super(avatar);
        this.avatarName = avatar;
        this.name = name;
        this.scoreKeeper = new ScoreKeeper();
        status.set(PlayerStatus.IDLE);
    }

    public void setAvatar(String avatarName) {
        super.setSpriteName(avatarName);
        this.avatarName = avatarName;
        super.load(avatarName);
    }

    public void startAnimation() {
        super.isMoving = true;
    }

    public void endAnimation() {
        super.isMoving = false;
    }

    public void setSpriteSize(Integer size) {
        super.setSpriteSize(size);
    }

    public void loadExisting() {
        // do things
    }

    public void render(AnchorPane root, int x, int y) {
        super.render(root, x, y);
    }

    public void render(AnchorPane root, GridPanePosition startPosition) {
        super.render(root, startPosition);
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }

    public boolean isMoving() {
        return super.isMoving();
    }

    @Override
    public void moveOnRoute(ArrayList<TransportationNode> nodes) {
        SequentialTransition animation = getRouteAnimation(nodes);
        isMoving = true;
        status.set(PlayerStatus.ON_TRIP);
        animation.play();
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Player.super.onRouteCompleted();
                status.set(PlayerStatus.IDLE);
            }
        });
    }

    public void addStatusListener(ChangeListener<? super PlayerStatus> listener) {
        this.status.addListener(listener);
    }

    public PlayerStatus getStatus() {
        return status.get();
    }

    public String getPlayerName() {
        return this.name;
    }

    public void setPlayerName(String name) {
        this.name = name;
        this.scoreKeeper.setPlayerName(name);
    }

    public String getAvatarName() {
        return this.avatarName;
    }
}