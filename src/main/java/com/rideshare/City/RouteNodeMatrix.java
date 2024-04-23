package com.rideshare.City;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TileManager.TileUtils;
import com.rideshare.TransportationMode.TransportationType;
import com.rideshare.Trip.TransportationNode;

// RouteNodeMatrix is a matrix of Transportation nodes for a given route
public class RouteNodeMatrix {
  private TransportationNode[][] matrix;
  private TransportationType transportationType;
  private String name;

  // For each transportation type, contains all tile ids where it is valid to
  // enter/exit
  private static final Map<TransportationType, Integer[]> stopCodes = new HashMap<>();
  // For each transportation type, contains all tile ids where it is valid to move
  private static final Map<TransportationType, Integer[]> openCodes = new HashMap<>();

  static {
    stopCodes.put(TransportationType.WALKING, TileUtils.WALKING_TILE_IDS);
    stopCodes.put(TransportationType.CAR, TileUtils.ROAD_TILE_IDS);
    stopCodes.put(TransportationType.BUS, TileUtils.STOP_TILE_IDS);
    stopCodes.put(TransportationType.TRAIN, TileUtils.STOP_TILE_IDS);
  }

  static {
    openCodes.put(TransportationType.WALKING, TileUtils.WALKING_TILE_IDS);
    openCodes.put(TransportationType.CAR, TileUtils.ROAD_TILE_IDS);
    openCodes.put(TransportationType.BUS, TileUtils.ROAD_TILE_IDS);
    openCodes.put(TransportationType.TRAIN, TileUtils.TRAIN_TILE_IDS);
  }

  public RouteNodeMatrix(int[][] mapDataMatrix, TransportationType transportationType, String name) {
    this.name = name;
    this.matrix = new TransportationNode[mapDataMatrix.length][mapDataMatrix[0].length];
    this.transportationType = transportationType;
    for (int i = 0; i < mapDataMatrix.length; i++) {
      int rowIdx = i;
      int[] row = mapDataMatrix[i];
      for (int j = 0; j < row.length; j++) {
        int colIdx = j;
        TransportationNode node = new TransportationNode(colIdx, rowIdx, transportationType, this);
        boolean isStopCode = Arrays.asList(stopCodes.get(transportationType)).contains(mapDataMatrix[rowIdx][colIdx]);
        if (isStopCode) {
          node.setAsValidStop();
        }
        boolean isOpenCode = Arrays.asList(openCodes.get(transportationType)).contains(mapDataMatrix[rowIdx][colIdx])
            || isStopCode;
        if (!isOpenCode) {
          node.setAsSolid();
        }
        matrix[rowIdx][colIdx] = node;
      }
    }
  }

  public String getRouteName() {
    return this.name;
  }

  /**
   * For a given row/col on the map, return the TransportationNode at that
   * position
   * 
   * @param position
   * @return TransportationNode
   */
  public TransportationNode getNode(GridPanePosition position) {
    if (position.row >= matrix.length || position.row < 0 || position.col >= matrix[0].length || position.col < 0) {
      return null;
    }
    return matrix[position.row][position.col];
  }

  public TransportationNode[][] get() {
    return matrix;
  }

  public TransportationType getTransportationType() {
    return this.transportationType;
  }
}
