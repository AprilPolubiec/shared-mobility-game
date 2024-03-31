package com.rideshare;

import java.util.ArrayList;

import com.rideshare.GameManager.GameController;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// https://www.youtube.com/watch?v=2JNEme00ZFA&list=RDCMUCS94AD0gxLakurK-6jnqV1w&index=6
public class TripCalculator {
    int startX;
    int startY;
    int endX;
    int endY;

    boolean goalReached = false;
    GameController gameController;
    Scene scene;
    City city;
    GridPane gridPane = new GridPane();

    // NODE
    // TransportationNode[][] node = new TransportationNode[maxCol][maxRow];
    TransportationNode startNode, goalNode, currentNode;
    RouteNodeMatrix currentRouteMatrix;
    ArrayList<TransportationNode> openList = new ArrayList<>();
    ArrayList<TransportationNode> checkedList = new ArrayList<>();

    public TripCalculator(City city, GameController gameController) {
        this.gameController = gameController;
        this.city = city;
        drawCity();
    }

    private void drawCity() {
        int gridSize = 8; // Size of the grid
        double cellSize = 60; // Size of each cell in the grid

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

                        if (n.modeOfTransport.hasStops() && n.canStop) {
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

        this.gameController.get_root().getChildren().add(gridPane);

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
            if (transportationNode.co2EmissionRate < minEmissions) {
                this.startNode = transportationNode;
                minEmissions = transportationNode.co2EmissionRate;
            }
        }
        this.currentNode = startNode;
        this.currentRouteMatrix = this.currentNode.routeMatrix;

        // Always use the walking node because its guaranteed to be there
        for (TransportationNode transportationNode : city.getRouteNodes(endY, endX)) {
            if(transportationNode.transportationType == TransportationType.WALKING) {
                this.goalNode = transportationNode;
                break;
            }
        }
        autoSearch();
        return trips;
    }

    private void openNeighbors(int row, int col) {
        print(String.format("Opening all neighbors to [%s, %s]", row, col));
        // OPEN NODES
        if (row - 1 >= 0) { // Node above
            // print(String.format("Opening nodes at [%s, %s]", row - 1, col));
            openNode(this.currentRouteMatrix.getNode(row - 1, col));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                // Check all of the surrounding route options!
                for (RouteNodeMatrix route : this.city.routes) {
                    openNode(route.getNode(row - 1, col));
                }
            }
        }

        if (col - 1 >= 0) { // Node to the left
            // print(String.format("Opening nodes at [%s, %s]", row, col - 1));
            openNode(this.currentRouteMatrix.getNode(row, col - 1));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (RouteNodeMatrix route : this.city.routes) {
                    openNode(route.getNode(row, col - 1));
                }
            }
        }

        if (row + 1 < 8) { // Node below
            // print(String.format("Opening nodes at [%s, %s]", row + 1, col));
            openNode(this.currentRouteMatrix.getNode(row + 1, col));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (RouteNodeMatrix route : this.city.routes) {
                    openNode(route.getNode(row + 1, col));
                }
            }
        }

        if (col + 1 < 8) { // Node to the right
            // print(String.format("Opening nodes at [%s, %s]", row, col + 1));
            openNode(this.currentRouteMatrix.getNode(row, col + 1));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (RouteNodeMatrix route : this.city.routes) {
                    openNode(route.getNode(row, col + 1));
                }
            }
        }
    }

    // Sets all transportation nodes to closed at row/col
    private void closeAllNodes(int row, int col) {
        // print(String.format("Closing all nodes at [%s, %s]", row, col));
        currentNode.setAsChecked();
        checkedList.add(currentNode);
        openList.remove(currentNode);
        for (RouteNodeMatrix route : this.city.routes) {
            TransportationNode node = route.getNode(row, col);
            node.setAsChecked();
            checkedList.add(node);
            openList.remove(node);
            print(String.format("[%s, %s] %s IS CLOSED", node.row, node.col, node.transportationType.name()));
        }
    }

    private void print(String txt) {
        System.out.println(txt);
    }

    public void autoSearch() {
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;
            print(String.format("CURRENT NODE: [%s, %s] %s", row, col, currentNode.transportationType.name()));

            closeAllNodes(row, col);
            openNeighbors(row, col);
    
            // FIND BEST NODE
            int bestNodeIdx = 0;
            int bestNodeFCost = 999;

            print("Evaluating best neighbor");
            for (int i = 0; i < openList.size(); i++) {
                // Check if the F cost is better than current best
                TransportationNode nodeToCheck = openList.get(i);
                nodeToCheck.getCost(startNode, goalNode);
                print(String.format("[%s, %s] %s: %s", nodeToCheck.row, nodeToCheck.col, nodeToCheck.transportationType.name(), nodeToCheck.fCost));
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

            currentNode = openList.get(bestNodeIdx);
            currentRouteMatrix = currentNode.routeMatrix; // Switch our route mat
            print(String.format("BEST NODE: [%s, %s] %s", currentNode.row, currentNode.col, currentNode.transportationType.name()));
            if (currentNode.row == goalNode.row && currentNode.col == goalNode.col) {
                goalNode.parent = currentNode.parent;
                goalReached = true;
                // Do the thing
                trackThePath();
            }
        }
    }

    private void openNode(TransportationNode node) {
        // print(String.format("Attempting open [%s, %s] %s",node.row, node.col, node.transportationType.name()));
        if (node.open == false && node.checked == false && node.solid == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
            print(String.format("[%s, %s] %s IS OPEN", node.row, node.col, node.transportationType.name()));
        } else {
            print(String.format("[%s, %s] %s Not eligible. Open: %s, Checked: %s, Solid: %s", node.row, node.col, node.transportationType.name(), node.open, node.checked, node.solid));
        }
    }

    private void trackThePath() {
        // Backtrack and draw the best path
        TransportationNode current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                for (Node node : gridPane.getChildren()) {
                    if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == current.row &&
                            GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == current.col &&
                            node instanceof Rectangle) {
                        if (current.transportationType == TransportationType.BUS) {
                            ((Rectangle) node).setFill(Color.YELLOW);
                        }
                        if (current.transportationType == TransportationType.WALKING) {
                            ((Rectangle) node).setFill(Color.GRAY);
                        }
                        if (current.transportationType == TransportationType.TRAIN) {
                            ((Rectangle) node).setFill(Color.BLUE);
                        }
                        if (current.transportationType == TransportationType.CAR) {
                            ((Rectangle) node).setFill(Color.ORANGE);
                        }
                    }
                    System.out.println(String.format("%s %s", current.row, current.col));
                }
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