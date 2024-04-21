package com.rideshare.Controllers;

import java.io.IOException;

import com.rideshare.Player;
import com.rideshare.UIComponentUtils;
import com.rideshare.Utils;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseCharacterController {
    Stage stage;
    AnchorPane root;

    @FXML
    public ImageView rightButton;
    @FXML
    public ImageView leftButton;
    @FXML
    public TextField nameInput;

    private int currentSpriteIdx = 0;
    private final String[] sprites = { "girl-1", "girl-2", "girl-3", "guy-1", "guy-2", "guy-3", "orc-1" };
    private Player newPlayer;

    @FXML
    public void onRightButtonClicked() {
        Utils.print("Right clicked");
        showNextSprite();
    }

    @FXML
    public void onLeftButtonClicked() {
        Utils.print("Left clicked");
        showPreviousSprite();
    }

    @FXML
    public void onStartButtonClicked() {
        Utils.print("Start clicked");
    }

    public void showNextSprite() {
        currentSpriteIdx += 1;
        if (currentSpriteIdx == sprites.length) {
            currentSpriteIdx = 0;
        }
        this.newPlayer.setAvatar(sprites[currentSpriteIdx]);
    }

    public void showPreviousSprite() {
        currentSpriteIdx -= 1;
        if (currentSpriteIdx < 0) {
            currentSpriteIdx = sprites.length - 1;
        }
        this.newPlayer.setAvatar(sprites[currentSpriteIdx]);
    }

    private AnchorPane setScene(boolean isFullScreen) throws IOException {
        Scene scene = this.stage.getScene();
        scene.setRoot(this.root);
        this.stage.setFullScreen(isFullScreen);
        return this.root;
    }

    void load(AnchorPane root, Stage stage) {
        try {
            this.stage = stage;
            this.root = root;
            setScene(true);
            initialiseSpritePreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initialiseSpritePreview() {
        this.newPlayer = new Player("", sprites[currentSpriteIdx]);
        this.newPlayer.setSpriteSize(100);
        this.newPlayer.render(root, UIComponentUtils.getCenterStageCoordinates(stage)[0] - 50, UIComponentUtils.getCenterStageCoordinates(stage)[1] - 50);
        this.newPlayer.startAnimation();
    }
}
