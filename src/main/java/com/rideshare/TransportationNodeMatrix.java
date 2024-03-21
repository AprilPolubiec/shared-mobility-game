package com.rideshare;

public class TransportationNodeMatrix {
    private TransportationNode[][] matrix;
    public TransportationNodeMatrix(int[][] intMatrix, int[][] stopMatrix, TransportationType transportationType) {
        matrix = new TransportationNode[intMatrix.length][intMatrix[0].length];
        for (int i = 0; i < intMatrix.length; i++) {
            int rowIdx = i;
            int[] row = intMatrix[i];
            for (int j = 0; j < row.length; j++) {
              int colIdx = j;
              TransportationNode node = new TransportationNode(colIdx, rowIdx, transportationType);
              if (intMatrix[rowIdx][colIdx] == 1) {
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

    public TransportationNode[][] get() {
      return matrix;
    }
}
