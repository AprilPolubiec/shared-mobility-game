package com.rideshare;

import com.rideshare.TileManager.TileUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public abstract class Popup {
    protected AnchorPane dialogRoot;
    protected Font font;
    protected Font smallFont;

    protected final int imageWidth;
    protected final int imageHeight;
    protected final double top;
    protected final double left;

    public Popup(String imageUrl, int imageWidth, int imageHeight) {
        this.font = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 42);
        this.smallFont = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 24);

        this.dialogRoot = new AnchorPane();
        ImageView dialogImage = new ImageView(imageUrl);
        this.dialogRoot.getChildren().add(dialogImage);

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.top = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageHeight / 2);
        this.left = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageWidth / 2);

        AnchorPane.setTopAnchor(this.dialogRoot, top);
        AnchorPane.setLeftAnchor(this.dialogRoot, left);
    }

    public void render(AnchorPane root) {
        root.getChildren().add(this.dialogRoot);
    }

    public void hide() {
        this.dialogRoot.setVisible(false);
    }
}
