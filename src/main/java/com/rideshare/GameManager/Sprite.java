package com.rideshare.GameManager;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rideshare.App;
import com.rideshare.GridPanePosition;
import com.rideshare.TransportationMode;
import com.rideshare.TransportationNode;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Sprite {
    private String resource_directory;
    HashMap<String, List<Image>> icons = new HashMap<String, List<Image>>();
    Scene scene;
    Stage _stage;
    ImageView imageView;
    private double xPos;
    private double yPos;
    private String direction = "down";
    private int spriteIdx;
    private int spriteTimer;
    private boolean isMoving;

    public Sprite(String name, Stage stage) {
        this._stage = stage;
        this.scene = stage.getScene();
        this.load(name);       
    }

    private void load(String name) {
        try {
            this.resource_directory = "/images/sprites/" + name;
            File dir = new File(resource_directory);
            if (!dir.exists()) {
                List<Image> up_icons = new ArrayList<Image>();
                List<Image> down_icons = new ArrayList<Image>();
                List<Image> left_icons = new ArrayList<Image>();
                List<Image> right_icons = new ArrayList<Image>();
                for (int i = 1; i <= 3; i++) {
                    up_icons.add(new Image(App.class.getResource(resource_directory + "/up-" + i + ".png").toString()));
                    down_icons.add(
                            new Image(App.class.getResource(resource_directory + "/down-" + i + ".png").toString()));
                    left_icons.add(
                            new Image(App.class.getResource(resource_directory + "/left-" + i + ".png").toString()));
                    right_icons.add(
                            new Image(App.class.getResource(resource_directory + "/right-" + i + ".png").toString()));
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
        xPos = 32 * 13; // [13,13]
        yPos = 32 * 13; // [13,13]
        // xPos = (32 * 30) / 2;
        // yPos = (32 * 30) / 2;
        imageView.setX(xPos);
        imageView.setY(yPos);

        ((AnchorPane) _stage.getScene().getRoot()).getChildren().add(imageView);
        spriteLoop().playFromStart();
    }

    public GridPanePosition getGridPanePosition() {
        int col = (int) Math.floor(xPos / 30);
        int row = (int) Math.floor(yPos / 30);
        return new GridPanePosition(row, col);
    }

    private Transition spriteLoop() {
        Transition loop = new Transition() {
            {
                setCycleDuration(Duration.millis(1000 / 60.0));
            }

            @Override
            protected void interpolate(double frac) {
                if (frac != 1)
                    return;
                if (isMoving) {
                    animate();
                }
            }

        };
        loop.setCycleCount(Animation.INDEFINITE);
        return loop;
    }

    public void moveOnRoute(ArrayList<TransportationNode> nodes) {
        TransportationNode endNode = nodes.get(nodes.size() - 1);
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < nodes.size() - 1; i++) {
            TransportationNode currentNode = nodes.get(i);
            TransportationNode nextNode = nodes.get(i + 1);
            // TODO: duration is dependent on tranportation type
            // TransportationMode
            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), this.imageView);

            if (currentNode.row < nextNode.row) {
                transition.setByY(32.0);
                yPos += 32;
            }
            if (currentNode.row > nextNode.row) {
                transition.setByY(-32.0);
                yPos -= 32;
            }
            if (currentNode.col > nextNode.col) {
                transition.setByX(-32.0);
                xPos -= 32;
            }
            if (currentNode.col < nextNode.col) {
                transition.setByX(32.0);
                xPos += 32;
            }

            final TransportationNode nextNextNode = i + 2 < nodes.size() ? nodes.get(i + 2) : null;
            
            transition.setOnFinished(e -> {
                if (nextNextNode != null) {
                    if (nextNode.row < nextNextNode.row) {
                        direction = "down";
                    }
                    if (nextNode.row > nextNextNode.row) {
                        direction = "up";
                    }
                    if (nextNode.col > nextNextNode.col) {
                        direction = "left";
                    }
                    if (nextNode.col < nextNextNode.col) {
                        direction = "right";
                    }
                }
                
                
            });
            sequentialTransition.getChildren().add(transition);
        }
        isMoving = true;
        sequentialTransition.play();
        sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                isMoving = false;
            }
        });
    }

    public void animate() {
        if (spriteTimer > 12) {
            if (spriteIdx < 1) {
                spriteIdx += 1;
            } else {
                spriteIdx = 0;
            }
            spriteTimer = 0;
        }
        var r = icons.get(this.direction).get(spriteIdx);
        imageView.setImage(icons.get(this.direction).get(spriteIdx));
        spriteTimer++;
    }
}
