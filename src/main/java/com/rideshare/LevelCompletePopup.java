package com.rideshare;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class LevelCompletePopup extends Popup {
    private Button repeatLevelButton;
    private Button nextLevelButton;

    public LevelCompletePopup() {
        super("/images/ui/level_complete.png", 400, 360);

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

    // TODO: animate the increase
    public void setScore(int score) {
        Text scoreText = new Text(String.format("%s", score));
        scoreText.setTranslateY(170);
        scoreText.setTranslateX(50);
        scoreText.setFont(font);
        this.dialogRoot.getChildren().add(scoreText);
    }

    public void setEmission(int emission) {
        Text emissionText = new Text(String.format("%s", emission));
        emissionText.setTranslateY(207);
        emissionText.setTranslateX(100);
        emissionText.setFont(smallFont);
        this.dialogRoot.getChildren().add(emissionText);
    }

    public void setTime(String time) {
        Text emissionText = new Text(String.format("%s", time));
        emissionText.setTranslateY(233);
        emissionText.setTranslateX(100);
        emissionText.setFont(smallFont);
        this.dialogRoot.getChildren().add(emissionText);
    }

    public void render(AnchorPane root) {
        root.getChildren().add(this.dialogRoot);
    }

    public void onNextLevelSelected(EventHandler<ActionEvent> listener) {
        nextLevelButton.setOnAction(listener);
    }

    public void onRepeatLevelSelected(EventHandler<ActionEvent> listener) {
        repeatLevelButton.setOnAction(listener);
    }

    public void hide() {
        this.dialogRoot.setVisible(false);
    }

}