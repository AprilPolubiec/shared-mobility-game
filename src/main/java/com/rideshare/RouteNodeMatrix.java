package com.rideshare;

// RouteNodeMatrix is a matrix of Transportation nodes for a given route
public class RouteNodeMatrix {
    private TransportationNode[][] matrix;
  
    public RouteNodeMatrix(int[][] routeMatrix, int[][] stopMatrix, TransportationType transportationType) {
        matrix = new TransportationNode[routeMatrix.length][routeMatrix[0].length];
        for (int i = 0; i < routeMatrix.length; i++) {
            int rowIdx = i;
            int[] row = routeMatrix[i];
            for (int j = 0; j < row.length; j++) {
              int colIdx = j;
              TransportationNode node = new TransportationNode(colIdx, rowIdx, transportationType, this);
              if (routeMatrix[rowIdx][colIdx] == 1) {
                if (stopMatrix[rowIdx][colIdx] == 1) {
                    node.setAsValidStop();
                }
              } else {
                node.setAsSolid();
              }
              matrix[rowIdx][colIdx] = node;
            }
          }
    }

    public TransportationNode getNode(int rowIdx, int colIdx) {
      return matrix[rowIdx][colIdx];
    }
    public TransportationNode[][] get() {
      return matrix;
    }
}
