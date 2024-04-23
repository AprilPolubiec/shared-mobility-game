package com.rideshare.UI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.rideshare.App;

import javafx.scene.ImageCursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UIComponentUtils {
    public static final Set<String> UI_COLORS = new HashSet<String>(
            Arrays.asList("blue", "green", "grey", "red", "yellow"));

    static public AnchorPane createStyledDialog(double height, double width) {
        AnchorPane root = new AnchorPane();
        root.setPrefHeight(height);
        root.setPrefWidth(width);
        ImageView redPanel = getPanel("red");
        redPanel.setFitHeight(height);
        redPanel.setFitWidth(width);
        AnchorPane.setBottomAnchor(redPanel, 0.0);
        ImageView greyPanel = getPanel("grey");
        greyPanel.setFitHeight(height - 25);
        greyPanel.setFitWidth(width);
        AnchorPane.setBottomAnchor(greyPanel, 0.0);
        root.getChildren().addAll(redPanel, greyPanel);
        return root;
    }

    static public ImageView getPanel(String color) {
        if (!UI_COLORS.contains(color)) {
            throw new IllegalArgumentException(color + " is not a valid UI color");
        }
        String panelUrl = App.class.getResource(String.format("/images/ui/%s_panel.png", color)).toString();
        return new ImageView(panelUrl);
    }

    static public ImageCursor getCursor() {
        // TODO: custom cursor
        // Image image = new Image("batman.png");
        // return new ImageCursor(image);
        return null;
    }

    static public int[] getCenterStageCoordinates(Stage stage) {
        // Get the center x coordinate
        double centerX = stage.getX() + stage.getWidth() / 2;

        // Get the center y coordinate
        double centerY = stage.getY() + stage.getHeight() / 2;
        return new int[] { (int) centerX, (int) centerY };
    }

    static public double getStageHeight(AnchorPane root) {
        return root.getScene().getWindow().getHeight();
    }

    static public double getStageWidth(AnchorPane root) {
        return root.getScene().getWindow().getWidth();
    }
}
