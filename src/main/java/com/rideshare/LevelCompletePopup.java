package com.rideshare;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class LevelCompletePopup extends Popup {
    private Button repeatLevelButton;
    private Button nextLevelButton;

    public LevelCompletePopup() {
        super("/images/ui/level_complete.png");

        // Add buttons
        repeatLevelButton = new Button();
        repeatLevelButton.setPrefSize(108, 108);
        repeatLevelButton.setOpacity(0.0);
        repeatLevelButton.setTranslateY(255);
        repeatLevelButton.setTranslateX(25);

        nextLevelButton = new Button();
        nextLevelButton.setPrefSize(108, 108);
        nextLevelButton.setOpacity(0.0);
        nextLevelButton.setTranslateY(255);
        nextLevelButton.setTranslateX(135);

        for (Button button : new Button[] { nextLevelButton, repeatLevelButton }) {
            button.setOnMouseEntered(me -> button.setCursor(Cursor.HAND));
        }

        this.dialogRoot.getChildren().addAll(repeatLevelButton, nextLevelButton);
    }

    // ... rest of the methods remain unchanged
}
