package com.rideshare.Controllers;

import java.io.IOException;

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
    public ImageView spritePreview;
    @FXML
    public ImageView rightButton;
    @FXML
    public ImageView leftButton;
    @FXML
    public TextField nameInput;

    @FXML
    public void onRightButtonClicked() {
        Utils.print("Right clicked");
    }

    @FXML
    public void onLeftButtonClicked() {
        Utils.print("Left clicked");
    }
    @FXML
    public void onStartButtonClicked() {
        Utils.print("Start clicked");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
