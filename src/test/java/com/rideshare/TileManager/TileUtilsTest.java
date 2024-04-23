package com.rideshare.TileManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.rideshare.TileManager.TileUtils;

public class TileUtilsTest {
    @Test
    void Test_CalculateKmFromPixels() {
        double km = TileUtils.pixelsToKm(32);
        assertEquals(0.5, km);

        km = TileUtils.pixelsToKm(16);
        assertEquals(0.25, km);
    
        km = TileUtils.pixelsToKm(64);
        assertEquals(1, km);
    }

    @Test
    void Test_CalculatePixelsFromKm() {
        int pixels = TileUtils.kmToPixels(1);
        assertEquals(64, pixels);

        pixels = TileUtils.kmToPixels(0.5);
        assertEquals(32, pixels);
    
        pixels = TileUtils.kmToPixels(0.25);
        assertEquals(16, pixels);
    
        pixels = TileUtils.kmToPixels(2);
        assertEquals(128, pixels);
    }
}
