package com.rideshare.Controllers;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseCharacterController {
    Stage stage;
    AnchorPane root;

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
