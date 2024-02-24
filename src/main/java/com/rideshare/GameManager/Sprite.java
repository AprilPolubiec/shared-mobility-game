package com.rideshare.GameManager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rideshare.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class Sprite {
    private String resource_directory;
    HashMap<String, List<Image>> icons = new HashMap<String, List<Image>>();
    Scene scene;
    GameController gameController;
    private int xPos;
    private int yPos;

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
                        up();
                        break;
                    case S:
                        down();
                        break;
                    case A:
                        left();
                        break;
                    case D:
                        right();
                        break;
                    default:
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
        ImageView imageView = new ImageView(icons.get("down").get(0));
        imageView.setX(scene.getWidth() / 2);
        imageView.setY(scene.getHeight() / 2);

        this.gameController.getRoot().getChildren().add(imageView);
    }

    public void up() {
        System.out.println("UP");
        yPos -= 10;
    }
    public void down() {
        System.out.println("down");
        yPos += 10;
    }
    public void left() {
        System.out.println("left");
        xPos -= 10;
    }
    public void right() {
        System.out.println("right");
        xPos += 10;
    }
}
