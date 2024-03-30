package com.rideshare;

import java.util.ArrayList;


import com.rideshare.GameManager.GameController;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
// https://www.youtube.com/watch?v=2JNEme00ZFA&list=RDCMUCS94AD0gxLakurK-6jnqV1w&index=6
public class TripCalculator {
    final int maxCol = 50;
    final int maxRow = 50;
    final int nodeSize = 16;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    
    int startX;
    int startY;
    int endX;
    int endY;

    boolean goalReached = false;
    GameController gameController;
    Scene scene;
    City city;

    // NODE
    // TransportationNode[][] node = new TransportationNode[maxCol][maxRow];
    TransportationNode startNode, goalNode, currentNode;
    RouteNodeMatrix currentRouteMatrix;
    ArrayList<TransportationNode> openList = new ArrayList<>();
    ArrayList<TransportationNode> checkedList = new ArrayList<>();

    public TripCalculator(City city, Stage stage) {
        this.city = city;
        drawCity(stage);
    }

    private void drawCity(Stage stage){
        GridPane gridPane = new GridPane();
        int gridSize = 8; // Size of the grid
        double cellSize = 60; // Size of each cell in the grid
        double routeTextOffset = 5; // Offset for route text

        // Create the grid of rectangles
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Rectangle rectangle = new Rectangle(cellSize, cellSize, Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                gridPane.add(rectangle, col, row);
            }
        }
        
    
        for (RouteNodeMatrix route : this.city.routes) {
            TransportationNode[][] matrix = route.get();
            for (int i = 0; i < matrix.length; i++) {
                TransportationNode[] row = matrix[i];
                for (int j = 0; j < row.length; j++) {
                    TransportationNode n = matrix[i][j];

                    if (n.solid == false) {
                        Text text = new Text(Integer.toString(route.getTransportationType().ordinal()));
                        // Check if a Text node already exists at this position
                        Text existingText = getTextFromGridPane(gridPane, j, i);

                        if(n.modeOfTransport.hasStops() && n.canStop) {
                            text.setFill(Color.RED);
                        }

                        // If a Text node exists, append text to it; otherwise, add a new Text node
                        if (existingText != null) {
                            existingText.setText(existingText.getText() + " " + text.getText());
                        } else {
                            gridPane.add(text, j, i);
                            text.setTranslateX(10);
                        }
                    }
                }
                }
            }

        Scene scene = new Scene(gridPane, Color.WHITE); // Set background color as needed
        stage.setScene(scene);
        stage.setTitle("City Map");
        stage.show();
    }

    private Text getTextFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                if (node instanceof Text) {
                    return (Text) node;
                }
                // If the node is not a Text, skip it
            }
        }
        return null; // No Text node found at the specified position
    }
    public ArrayList<Trip> calculateTrips(int startX, int startY, int endX, int endY) {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        // Start at the node with the lowest CO2 emissions
        ArrayList<TransportationNode> startNodeOptions = city.getRouteNodes(startY, startX);
        int minEmissions = Integer.MAX_VALUE;
        for (TransportationNode transportationNode : startNodeOptions) {
            if(transportationNode.co2EmissionRate < minEmissions) {
                this.startNode = transportationNode;
                minEmissions = transportationNode.co2EmissionRate;
            }
        }
        this.currentNode = startNode;
        this.currentRouteMatrix = this.currentNode.routeMatrix;
        this.goalNode = city.getRouteNodes(endY, endX).get(0); // Just get the first transportationtype, doesn't really matter

        autoSearch();
        return trips;
    }


    public void autoSearch() {
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // OPEN NODES
            if (row - 1 >= 0) { // Node above
                openNode(this.currentRouteMatrix.getNode(row - 1, col));
                if (currentNode.modeOfTransport.hasStops() && currentNode.canStop) {
                    // Check all of the surrounding route options!
                    for (RouteNodeMatrix route : this.city.routes) {
                        openNode(route.getNode(row - 1, col));
                    }
                }
            }

            if (col - 1 >= 0) { // Node to the left
                openNode(this.currentRouteMatrix.getNode(row, col - 1));
                if (currentNode.modeOfTransport.hasStops() && currentNode.canStop) {
                    for (RouteNodeMatrix route : this.city.routes) {
                        openNode(route.getNode(row - 1, col));
                    }
                }
            }

            if (row + 1 < maxRow) { // Node below
                openNode(this.currentRouteMatrix.getNode(row + 1, col));
                if (currentNode.modeOfTransport.hasStops() && currentNode.canStop) {
                    for (RouteNodeMatrix route : this.city.routes) {
                        openNode(route.getNode(row - 1, col));
                    }
                }
            }

            if (col + 1 < maxCol) { // Node to the right
                openNode(this.currentRouteMatrix.getNode(row, col + 1));
                if (currentNode.modeOfTransport.hasStops() && currentNode.canStop) {
                    for (RouteNodeMatrix route : this.city.routes) {
                        openNode(route.getNode(row - 1, col));
                    }
                }
            }

            // FIND BEST NODE
            int bestNodeIdx = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Check if the F cost is better than current best
                TransportationNode nodeToCheck = openList.get(i);
                nodeToCheck.getCost(startNode, currentNode);
                if (nodeToCheck.fCost < bestNodeFCost) {
                    bestNodeIdx = i;
                    bestNodeFCost = nodeToCheck.fCost;
                }
                // If F cost is equal, check the G cost
                else if (nodeToCheck.fCost == bestNodeFCost) {
                    if (nodeToCheck.gCost < openList.get(bestNodeIdx).gCost) {
                        bestNodeIdx = i;
                    }
                }
            }

            // After loop, get the best node
            currentNode = openList.get(bestNodeIdx);
            currentRouteMatrix = currentNode.routeMatrix; // Switch our route matrix
            if (currentNode == goalNode) {
                goalReached = true;
                // Do the thing
                // trackThePath();
            }
        }
    }

    private void openNode(TransportationNode node) {
        if (node.open == false && node.checked == false && node.solid == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath() {
        // Backtrack and draw the best path
        TransportationNode current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }

}

/**
 * Sample input:
 * 
 * Bus:
 * [0, 1, 0]
 * [0, 1, 0]
 * [0, 1, 0]
 * 
 * Train:
 * [1, 1, 1]
 * [1, 0, 0]
 * [1, 1, 1]
 */