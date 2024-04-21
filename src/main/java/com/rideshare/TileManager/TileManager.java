package com.rideshare.TileManager;

import com.rideshare.App;
import com.rideshare.GameManager.MapLoader;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class TileManager {
    private TiledMapLayer[] _layers;
    AnchorPane _root;
    int _height;
    int _width;
    GridPane _tileGrid;

    public TileManager(AnchorPane root, TiledMapLayer[] layers, int height, int width) {
        _layers = layers;
        _height = height;
        _width = width;
        _root = root;
    }

    private String getTileImageFileName(int tileId) {
        String tileIdString = Integer.toString(tileId - 1);

        while (tileIdString.length() < 3) {
            tileIdString = String.format("0%s", tileIdString);
        }

        return String.format("tile%s.png", tileIdString);
    }

    public void draw() {
        _tileGrid = new GridPane();
        // Add the GridPane to the AnchorPane
        for (TiledMapLayer layer : _layers) {
            int[][] matrix = MapLoader.arrayToMatrix(layer.data, _height, _width);
            for (int i = 0; i < matrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < matrix[rowIdx].length; j++) {
                    int colIdx = j;
                    int tileId = matrix[rowIdx][colIdx];
                    if (tileId == 0) {
                        continue;
                    }
                    drawTile(tileId, rowIdx, colIdx);
                }
            }
        }
        _root.getChildren().add(0, _tileGrid);
    }


    public ImageView drawTile(int tileId, int rowIdx, int colIdx) {
        String tileUrl = App.class.getResource(String.format("/images/tiles/%s", getTileImageFileName(tileId))).toString();
        ImageView tileImage = new ImageView(tileUrl);
        tileImage.setFitHeight(TileUtils.TILE_SIZE_IN_PIXELS);
        tileImage.setFitWidth(TileUtils.TILE_SIZE_IN_PIXELS);
        _tileGrid.add(tileImage, colIdx, rowIdx);
        
        return tileImage;
    }

    public ImageView replaceTileImage(ImageView tile, int tileId) {
        String tileUrl = App.class.getResource(String.format("/images/tiles/%s", getTileImageFileName(tileId))).toString();
        tile.setImage(new Image(tileUrl));
        return tile;
    }

    public void removeTile(ImageView tile, int rowIdx, int colIdx) {
        _tileGrid.getChildren().remove(tile);
    }
}
