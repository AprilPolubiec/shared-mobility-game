package com.rideshare.GameManager;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.rideshare.Route;
import com.rideshare.RouteNodeMatrix;
import com.rideshare.TransportationNode;
import com.rideshare.TransportationType;
import com.rideshare.TileManager.MapJson;
import com.rideshare.TileManager.TileUtils;

public class MapLoaderTest {
    @Test
    void Test_ArrayToMatrix() {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        int[][] matrix = MapLoader.arrayToMatrix(arr, 5, 5);
        assertEquals(5, matrix.length);
        assertEquals(5, matrix[0].length);
        assertEquals(5, matrix[1].length);
        assertEquals(5, matrix[2].length);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, matrix[0]);
        assertArrayEquals(new int[]{6, 7, 8, 9, 10}, matrix[1]);
        assertArrayEquals(new int[]{11, 12, 13, 14, 15}, matrix[2]);
    }

    @Test
    void Test_GetMapData() throws Exception {
        MapJson data = MapLoader.getMapDataFromFile("test-map");
        assertEquals(30, data.height);
        assertEquals(30, data.width);
        assertEquals(16, data.tileheight);
        assertEquals(16, data.tilewidth);
        assertEquals(6, data.layers.length);

        // Check the first layer contains all its data
        assertEquals(30, data.layers[0].height);
        assertEquals(30, data.layers[0].width);
        assertEquals(1, data.layers[0].id);
        assertEquals("Ground", data.layers[0].name);
    }

    @Test
    void Test_GetWalkingRoute() throws Exception {
        MapJson map = MapLoader.getMapDataFromFile("test-map");
        int[][] matrix = MapLoader.arrayToMatrix(map.layers[1].data, map.height, map.width);
        Route walkingRoute = MapLoader.getRoute(map, "Walking", TransportationType.WALKING, "");
        assertEquals(TransportationType.WALKING, walkingRoute.getTransportationType());
        RouteNodeMatrix routeNodeMatrix = walkingRoute.getRouteNodeMatrix();
        for (int i = 0; i < matrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < matrix[rowIdx].length; j++) {
                int colIdx = j;
                TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
                int dataValue = matrix[rowIdx][colIdx];
                if (Arrays.asList(TileUtils.GRASS_TILE_IDS).contains(dataValue)) {
                    assertTrue(node.canStop());
                } else {
                    assertFalse(node.canStop());
                    assertTrue(node.isSolid());
                }
            }
        }
    }
    @Test
    void Test_GetCarRoute() throws Exception {
        MapJson map = MapLoader.getMapDataFromFile("test-map");
        int[][] matrix = MapLoader.arrayToMatrix(map.layers[2].data, map.height, map.width);
        Route carRoute = MapLoader.getRoute(map, "Roads", TransportationType.CAR, "");
        assertEquals(TransportationType.CAR, carRoute.getTransportationType());
        RouteNodeMatrix routeNodeMatrix = carRoute.getRouteNodeMatrix();
        for (int i = 0; i < matrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < matrix[rowIdx].length; j++) {
                int colIdx = j;
                TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
                int dataValue = matrix[rowIdx][colIdx];
                if (Arrays.asList(TileUtils.ROAD_TILE_IDS).contains(dataValue)) {
                    assertTrue(node.canStop());
                    assertFalse(node.isSolid());
                } else {
                    assertFalse(node.canStop());
                    assertTrue(node.isSolid());
                }
            }
        }
    }

    @Test
    void Test_GetTrainRoute() throws Exception {
        MapJson map = MapLoader.getMapDataFromFile("test-map");
        int[][] matrix = MapLoader.arrayToMatrix(map.layers[4].data, map.height, map.width);
        Route trainRoute = MapLoader.getRoute(map, "Train", TransportationType.TRAIN, "A train");
        assertEquals(TransportationType.TRAIN, trainRoute.getTransportationType());
        
        RouteNodeMatrix routeNodeMatrix = trainRoute.getRouteNodeMatrix();
        for (int i = 0; i < matrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < matrix[rowIdx].length; j++) {
                int colIdx = j;
                TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
                int dataValue = matrix[rowIdx][colIdx];
                if (Arrays.asList(TileUtils.STOP_TILE_IDS).contains(dataValue)) {
                    assertTrue(node.canStop());
                } else if (Arrays.asList(TileUtils.TRAIN_TILE_IDS).contains(dataValue)){
                    assertFalse(node.canStop());
                    assertFalse(node.isSolid());
                } else {
                    assertFalse(node.canStop());
                    assertTrue(node.isSolid());
                }
            }
        }
    }
    
    @Test
    void Test_GetBusRoute() throws Exception {
        MapJson map = MapLoader.getMapDataFromFile("test-map");
        int[][] matrix = MapLoader.arrayToMatrix(map.layers[3].data, map.height, map.width);
        Route busRoute = MapLoader.getRoute(map, "Bus", TransportationType.BUS, "39A");
        assertEquals(TransportationType.BUS, busRoute.getTransportationType());
        
        RouteNodeMatrix routeNodeMatrix = busRoute.getRouteNodeMatrix();
        for (int i = 0; i < matrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < matrix[rowIdx].length; j++) {
                int colIdx = j;
                TransportationNode node = routeNodeMatrix.getNode(rowIdx, colIdx);
                int dataValue = matrix[rowIdx][colIdx];
                ArrayList<Integer> openValues = new ArrayList<Integer>();
                Collections.addAll(openValues, 145, 147);
                if (Arrays.asList(TileUtils.STOP_TILE_IDS).contains(dataValue)) {
                    assertTrue(node.canStop());
                } else if (Arrays.asList(TileUtils.ROAD_TILE_IDS).contains(dataValue)){
                    assertFalse(node.canStop());
                    assertFalse(node.isSolid());
                } else {
                    assertFalse(node.canStop());
                    assertTrue(node.isSolid());
                }
            }
        }
    }
    
}
