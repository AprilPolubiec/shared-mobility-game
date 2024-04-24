package com.rideshare;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class GameOverPopup extends Popup {
    private Button repeatLevelButton;

    public GameOverPopup() {
        super("/images/ui/level_failed.png");

        // Add buttons
        repeatLevelButton = new Button();
        repeatLevelButton.setPrefSize(108, 108);
        repeatLevelButton.setOpacity(0.0);
        repeatLevelButton.setTranslateY(255);
        repeatLevelButton.setTranslateX(150);
        repeatLevelButton.setOnMouseEntered(me -> repeatLevelButton.setCursor(Cursor.HAND));
        this.dialogRoot.getChildren().add(repeatLevelButton);
    }

    // ... rest of the methods remain unchanged
}
