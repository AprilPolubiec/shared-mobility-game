package com.rideshare.GameManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rideshare.App;
import com.rideshare.GridPanePosition;
import com.rideshare.Timer;
import com.rideshare.TransportationMode;
import com.rideshare.TransportationNode;
import com.rideshare.TransportationType;
import com.rideshare.Utils;
import com.rideshare.TileManager.TileUtils;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Sprite {
    private String resource_directory;
    private HashMap<String, List<Image>> icons = new HashMap<String, List<Image>>();
    private ImageView imageView;
    private double xPos;
    private double yPos;
    private String direction = "down";
    private int spriteIdx;
    private int spriteTimer;
    protected boolean isMoving;
    private String spriteName;
    private String currentSprite;

    public Sprite(String name) {
        spriteName = name;
        this.load(name);
    }

    private void load(String name) {
        if (currentSprite == name) {
            return;
        }
        // System.out.println(String.format("Loading %s", name));
        try {
            this.resource_directory = "/images/sprites/" + name;

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
            this.currentSprite = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    protected void render(AnchorPane root, GridPanePosition startPosition) {
        // Place on the screen
        imageView = new ImageView(icons.get("down").get(0));
        imageView.setFitHeight(TileUtils.TILE_SIZE_IN_PIXELS);
        imageView.setFitWidth(TileUtils.TILE_SIZE_IN_PIXELS);
        xPos = TileUtils.TILE_SIZE_IN_PIXELS * startPosition.row;
        yPos = TileUtils.TILE_SIZE_IN_PIXELS * startPosition.col;
        imageView.setX(xPos);
        imageView.setY(yPos);

        root.getChildren().add(imageView);
        spriteLoop().playFromStart();
    }

    protected GridPanePosition getGridPanePosition() {
        int col = (int) Math.floor(xPos / TileUtils.TILE_SIZE_IN_PIXELS);
        int row = (int) Math.floor(yPos / TileUtils.TILE_SIZE_IN_PIXELS);
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

    public abstract void moveOnRoute(ArrayList<TransportationNode> nodes);

    protected SequentialTransition getRouteAnimation(ArrayList<TransportationNode> nodes) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < nodes.size() - 1; i++) {
            TransportationNode currentNode = nodes.get(i);
            TransportationNode nextNode = nodes.get(i + 1);
            if (i == 0) {
                if (currentNode.row < nextNode.row) {
                    direction = "down";
                }
                if (currentNode.row > nextNode.row) {
                    direction = "up";
                }
                if (currentNode.col > nextNode.col) {
                    direction = "left";
                }
                if (currentNode.col < nextNode.col) {
                    direction = "right";
                }
            }

            TransportationMode transportationMode = currentNode.modeOfTransport;
            double minutes = (TileUtils.TILE_DISTANCE_IN_KM / transportationMode.getSpeed()) * 60.0; // Number of game
                                                                                                     // minutes to go
                                                                                                     // 0.5 km
            double seconds = Timer.gameMinutesToSeconds(minutes);
            TranslateTransition transition = new TranslateTransition(Duration.seconds(seconds), this.imageView);

            if (currentNode.row < nextNode.row) {
                transition.setByY(TileUtils.TILE_SIZE_IN_PIXELS);
                yPos += TileUtils.TILE_SIZE_IN_PIXELS;
            }
            if (currentNode.row > nextNode.row) {
                transition.setByY(-TileUtils.TILE_SIZE_IN_PIXELS);
                yPos -= TileUtils.TILE_SIZE_IN_PIXELS;
            }
            if (currentNode.col > nextNode.col) {
                transition.setByX(-TileUtils.TILE_SIZE_IN_PIXELS);
                xPos -= TileUtils.TILE_SIZE_IN_PIXELS;
            }
            if (currentNode.col < nextNode.col) {
                transition.setByX(TileUtils.TILE_SIZE_IN_PIXELS);
                xPos += TileUtils.TILE_SIZE_IN_PIXELS;
            }

            Utils.print(String.format("Now at [%s, %s]", getGridPanePosition().row, getGridPanePosition().col));
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
                    switch (nextNode.transportationType) {
                        case CAR:
                            load("car");
                            break;
                        case BUS:
                            if (nextNode.row == nextNextNode.row && nextNextNode.col == nextNextNode.col) {
                                load(spriteName);
                            } else {
                                load("bus");
                            }
                            break;
                        default:
                            load(spriteName);
                            break;
                    }
                }
            });
            sequentialTransition.getChildren().add(transition);
        }
        return sequentialTransition;
    }


    protected void onRouteCompleted() {
        isMoving = false;
        load(spriteName);
    }

    private void animate() {
        if (spriteTimer > 12) {
            if (spriteIdx < 1) {
                spriteIdx += 1;
            } else {
                spriteIdx = 0;
            }
            spriteTimer = 0;
        }
        imageView.setImage(icons.get(this.direction).get(spriteIdx));
        spriteTimer++;
    }

    public boolean isMoving() {
        return this.isMoving;
    }
}
