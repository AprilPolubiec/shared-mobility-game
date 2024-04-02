package com.rideshare.TileManager;

public class TileUtils {
    public static final Integer[] ROAD_TILE_IDS = { 109, 110, 111, 112, 127, 128, 129, 130, 145, 146, 147, 148, 163, 164,
            165, 166 };
    public static final Integer[] STOP_TILE_IDS = { 181, 182, 183, 184, 185, 186, 187, 188, 189, 190 };
    public static final Integer[] GRASS_TILE_IDS = { 1, 2, 3 };
    public static final Integer[] TRAIN_TILE_IDS = { 59, 43, 60, 61, 78, 79 };
    public static final Integer[] HOUSE_TILE_IDS = { 10, 28, 46, 64, 82 };
    public static final Integer[] WALKING_TILE_IDS = { 2 }; // TODO: may change this
    public static final Integer[] FLAG_TILE_IDS = { 17, 18, 35, 36, 53, 54, 71, 72, 89, 90 }; // TODO: separate out by color/level
    public static final Integer[] COMPLETED_FLAG_IDS = { 17, 18 };
    public static final Integer FLAG_HOUSE_OFFSET = 7; // Every flag Id is +7
}
