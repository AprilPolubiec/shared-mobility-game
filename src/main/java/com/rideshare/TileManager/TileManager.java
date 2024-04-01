package com.rideshare.TileManager;

import java.io.IOException;

import com.rideshare.App;
import com.rideshare.City;
import com.rideshare.GameManager.MapLoader;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TileManager {
    Tile[] tiles;
    private TiledMapLayer[] _layers;
    AnchorPane _root;

    public TileManager(AnchorPane root, TiledMapLayer[] layers) {
        _layers = layers;
        _root = root;
    }

    private String getTileImageFileName(int tileId) {
        String tileIdString = Integer.toString(tileId - 1);

        while (tileIdString.length() < 4) {
            tileIdString = String.format("0%s", tileIdString);
        }

        return String.format("tile_%s.png", tileIdString);
    }

    public void draw() {
        GridPane tileGrid = new GridPane();
        // TODO: maybe change
        // AnchorPane.setTopAnchor(tileGrid, 0.0);
        // AnchorPane.setLeftAnchor(tileGrid, 0.0);

        // Add the GridPane to the AnchorPane
        for (TiledMapLayer layer : _layers) {
            int[][] matrix = MapLoader.arrayToMatrix(layer.data, layer.height, layer.width);
            for (int i = 0; i < matrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < matrix[rowIdx].length; j++) {
                    int colIdx = j;
                    int tileId = matrix[rowIdx][colIdx];
                    if (tileId == 0) {
                        continue;
                    }
                    String tileUrl = App.class
                            .getResource(String.format("/images/tiles/%s", getTileImageFileName(tileId))).toString();
                    ImageView tileImage = new ImageView(tileUrl);
                    tileGrid.add(tileImage, colIdx, rowIdx);
                }
            }
        }
        _root.getChildren().add(tileGrid);
    }
}
