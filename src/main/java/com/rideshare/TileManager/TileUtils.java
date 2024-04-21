package com.rideshare.TileManager;

public class TileUtils {
    public static final Integer[] ROAD_TILE_IDS = { 109, 110, 111, 112, 127, 128, 129, 130, 145, 146, 147, 148, 163, 164,
            165, 166 };
    public static final Integer[] STOP_TILE_IDS = { 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 198, 199 };
    public static final Integer[] GRASS_TILE_IDS = { 1, 2, 3 };
    public static final Integer[] TRAIN_TILE_IDS = { 59, 43, 60, 61, 78, 79, 198, 199 };
    public static final Integer[] HOUSE_TILE_IDS = { 10, 28, 46, 64, 82 };
    public static final Integer[] WALKING_TILE_IDS = { 2 }; // TODO: may change this
    public static final Integer[] FLAG_TILE_IDS = { 17, 18, 35, 36, 53, 54, 71, 72, 89, 90 }; // TODO: separate out by color/level
    public static final Integer[] COMPLETED_FLAG_IDS = { 17, 18 };
    public static final Integer FLAG_HOUSE_OFFSET = 7; // Every flag Id is +7

    public static double TILE_DISTANCE_IN_KM = 0.25;
    public static int TILE_SIZE_IN_PIXELS = 28;
    
    public static double pixelsToKm(int pixels) {
        return (TILE_DISTANCE_IN_KM * pixels) / TILE_SIZE_IN_PIXELS;
    }

    public static int kmToPixels(double km) {
        return (int)((TILE_SIZE_IN_PIXELS * km) / TILE_DISTANCE_IN_KM);
    }
}
