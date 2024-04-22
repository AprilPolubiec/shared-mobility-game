package com.rideshare;

import com.rideshare.TileManager.TileUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelCompletePopup {
    ObjectProperty<Boolean> shouldGoToNextLevel = new SimpleObjectProperty<>(false);
    ObjectProperty<Boolean> shouldRepeatLevel = new SimpleObjectProperty<>(false);

    private AnchorPane dialogRoot;
    private Font font;
    private Font smallFont;
    private final int imageWidth = 400;
    private final int imageHeight = 360;
    private final double top = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageHeight / 2);
    private final double left = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageWidth / 2);

    public LevelCompletePopup() {
        this.font = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 42);
        this.smallFont = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 24);

        this.dialogRoot = new AnchorPane();
        String dialogUrl = App.class.getResource("/images/ui/level_complete.png").toString();
        ImageView dialogImage = new ImageView(dialogUrl);
        this.dialogRoot.getChildren().add(dialogImage);

        AnchorPane.setTopAnchor(this.dialogRoot, top);
        AnchorPane.setLeftAnchor(this.dialogRoot, left);

        // Add buttons
        Button repeatLevelButton = new Button();
        repeatLevelButton.setPrefSize(108, 108);
        repeatLevelButton.setOpacity(0.0);
        repeatLevelButton.setTranslateY(255);
        repeatLevelButton.setTranslateX(25);

        repeatLevelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.print("repeat level clicked!");
                shouldRepeatLevel.set(true);
            }
        });

        Button nextLevelButton = new Button();
        nextLevelButton.setPrefSize(108, 108);
        nextLevelButton.setOpacity(0.0);
        nextLevelButton.setTranslateY(255);
        nextLevelButton.setTranslateX(135);

        nextLevelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.print("next level clicked!");
                shouldGoToNextLevel.set(true);
            }
        });

        for (Button button : new Button[] { nextLevelButton, repeatLevelButton }) {
            button.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    button.setCursor(Cursor.HAND);
                }
            });
        }

        this.dialogRoot.getChildren().addAll(repeatLevelButton, nextLevelButton);
    }

    // TODO: animate the increase?
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

    public void onNextLevelSelected(ChangeListener<? super Boolean> listener) {
        this.shouldGoToNextLevel.addListener(listener);
    }

    public void onRepeatLevelSelected(ChangeListener<? super Boolean> listener) {
        this.shouldRepeatLevel.addListener(listener);
    }

    public void hide() {
        this.dialogRoot.setVisible(false);
    }

}
