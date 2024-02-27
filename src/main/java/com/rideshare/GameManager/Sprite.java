package com.rideshare.GameManager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rideshare.App;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Sprite {
    private String resource_directory;
    HashMap<String, List<Image>> icons = new HashMap<String, List<Image>>();
    Scene scene;
    GameController gameController;
    ImageView imageView;
    private double xPos;
    private double yPos;
    private String direction;
    private int spriteIdx;
    private int spriteTimer;
    private boolean isMoving;

    public Sprite(String name, GameController gameController) {
        this.gameController = gameController;
        this.scene = gameController.getScene();
        // Get sprite files
        this.load(name);
        // Set key event handlers
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        direction = "up";
                        isMoving = true;
                        break;
                    case S:
                        direction = "down";
                        isMoving = true;
                        break;
                    case A:
                        direction = "left";
                        isMoving = true;
                        break;
                    case D:
                        direction = "right";
                        isMoving = true;
                        break;
                    default:
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                    case A:
                    case S:
                    case D:
                        isMoving = false;
                        break;
                }
            }
        });
    }

    private void load(String name) {
        try {
            this.resource_directory = "/images/sprites/" + name;
            File dir = new File(resource_directory);
            if(!dir.exists()) { 
                List<Image> up_icons = new ArrayList<Image>();
                List<Image> down_icons = new ArrayList<Image>();
                List<Image> left_icons = new ArrayList<Image>();
                List<Image> right_icons = new ArrayList<Image>();
                for (int i = 1; i <= 3; i++) {
                    up_icons.add(new Image(App.class.getResource(resource_directory + "/up-" + i + ".png").toString()));
                    down_icons.add(new Image(App.class.getResource(resource_directory + "/down-" + i + ".png").toString()));
                    left_icons.add(new Image(App.class.getResource(resource_directory + "/left-" + i + ".png").toString()));
                    right_icons.add(new Image(App.class.getResource(resource_directory + "/right-" + i + ".png").toString()));
                }
    
                icons.put("up", up_icons);
                icons.put("down", down_icons);
                icons.put("left", left_icons);
                icons.put("right", right_icons);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render() {
        // Place on the screen
        imageView = new ImageView(icons.get("down").get(0));
        xPos = scene.getWidth() / 2;
        yPos = scene.getHeight() / 2;
        imageView.setX(xPos);
        imageView.setY(yPos);

        this.gameController.getRoot().getChildren().add(imageView);
        spriteLoop().playFromStart();
    }

    private Transition spriteLoop() {
        Transition loop = new Transition() {
            { setCycleDuration(Duration.millis(1000 / 60.0)); }
            @Override
            protected void interpolate(double frac) {
                if (frac != 1) 
                    return;
                if (isMoving) {
                    move();
                }
            }
            
        };
        loop.setCycleCount(Animation.INDEFINITE);
        return loop;
    }

    public void move() {
        System.out.println(spriteTimer);
        if (spriteTimer > 12) {
            if (spriteIdx < 1) {
                spriteIdx += 1;
            } else {
                spriteIdx = 0;
            }
            spriteTimer = 0;
        }
        var r = icons.get(direction).get(spriteIdx);
        imageView.setImage(icons.get(direction).get(spriteIdx));
    
        switch (direction) {
            case "up":
                up();
                break;
            case "down":
                down();
                break;
            case "left":
                left();
                break;
            case "right":
                right();
                break;
            default:
                break;
        }
        spriteTimer++;
    }

    public void up() {
        System.out.println("UP");
        yPos -= 2;
        imageView.setY(yPos);
    }
    public void down() {
        System.out.println("down");
        yPos += 2;
        imageView.setY(yPos);
    }
    public void left() {
        System.out.println("left");
        xPos -= 2;
        imageView.setX(xPos);
    }
    public void right() {
        System.out.println("right");
        xPos += 2;
        imageView.setX(xPos);
    }
}
