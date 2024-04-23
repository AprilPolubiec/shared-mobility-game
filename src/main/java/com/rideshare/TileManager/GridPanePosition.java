package com.rideshare.TileManager;

public class GridPanePosition {
    public int row;
    public int col;

    public GridPanePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public GridPanePosition toTheLeft() {
        return new GridPanePosition(row, col - 1);
    }

    public GridPanePosition toTheRight() {
        return new GridPanePosition(row, col + 1);
    }

    public GridPanePosition below() {
        return new GridPanePosition(row + 1, col);
    }

    public GridPanePosition above() {
        return new GridPanePosition(row - 1, col);
    }

    // Checks if two grid pane positions are the same row/col
    public boolean equals(GridPanePosition position) {
        return this.row == position.row && this.col == position.col;
    }
}
