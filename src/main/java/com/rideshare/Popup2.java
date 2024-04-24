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
import javafx.scene.text.Text;

public abstract class Popup {
    protected AnchorPane dialogRoot;
    protected Font font;
    protected Font smallFont;

    protected final int imageWidth = 400;
    protected final int imageHeight = 360;
    protected final double top = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageHeight / 2);
    protected final double left = ((TileUtils.TILE_SIZE_IN_PIXELS * 30.0) / 2) - (imageWidth / 2);

    public Popup(String imageUrl) {
        this.font = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 42);
        this.smallFont = Font.loadFont(getClass().getResourceAsStream("/fonts/kenvector_future.ttf"), 24);

        this.dialogRoot = new AnchorPane();
        ImageView dialogImage = new ImageView(imageUrl);
        this.dialogRoot.getChildren().add(dialogImage);

        AnchorPane.setTopAnchor(this.dialogRoot, top);
        AnchorPane.setLeftAnchor(this.dialogRoot, left);
    }

    public abstract void render(AnchorPane root);

    public void hide() {
        this.dialogRoot.setVisible(false);
    }
}
