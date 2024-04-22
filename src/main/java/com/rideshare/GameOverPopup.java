package com.rideshare;

import com.rideshare.TileManager.TileUtils;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameOverPopup {
    AnchorPane dialogRoot;
    private final int imageWidth = 402;
    private final int imageHeight = 362;
    private final double top = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageHeight / 2);
    private final double left = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageWidth / 2);
    Button repeatLevelButton = new Button();

    public GameOverPopup() {
        this.dialogRoot = new AnchorPane();
        String dialogUrl = App.class.getResource("/images/ui/level_failed.png").toString();
        ImageView dialogImage = new ImageView(dialogUrl);
        this.dialogRoot.getChildren().add(dialogImage);

        AnchorPane.setTopAnchor(this.dialogRoot, top);
        AnchorPane.setLeftAnchor(this.dialogRoot, left);

       // Add buttons
       repeatLevelButton.setPrefSize(108, 108);
       repeatLevelButton.setOpacity(0.0);
       repeatLevelButton.setTranslateY(255);
       repeatLevelButton.setTranslateX(150);
       this.dialogRoot.getChildren().add(repeatLevelButton);
       repeatLevelButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent me) {
            repeatLevelButton.setCursor(Cursor.HAND);
        }
    });
    }

    public void render(AnchorPane root) {
        root.getChildren().add(this.dialogRoot);
    }

    public void onRepeatLevelSelected(EventHandler<ActionEvent> listener) {
        repeatLevelButton.setOnAction(listener);
    }

    public void hide() {
        this.dialogRoot.setVisible(false);
    }
}
