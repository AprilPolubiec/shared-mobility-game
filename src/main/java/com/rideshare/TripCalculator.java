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
    int startRow;
    int startCol;
    int endRow;
    int endCol;

    int cityHeight;
    int cityWidth;

    boolean goalReached = false;
    GameController gameController;
    Scene scene;
    City city;

    TransportationNode startNode, goalNode, currentNode;
    RouteNodeMatrix currentRouteMatrix;
    ArrayList<TransportationNode> openList = new ArrayList<>();
    ArrayList<TransportationNode> checkedList = new ArrayList<>();

    public TripCalculator(City city) {
        this.gameController = gameController;
        this.city = city;
        this.cityHeight = this.city.size;
        this.cityWidth = this.city.size;
    }

    public ArrayList<Trip> calculateTrips(int startRow, int startCol, int endRow, int endCol) {
        ArrayList<Trip> trips = new ArrayList<Trip>();
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;

        // Start at the node with the lowest CO2 emissions
        ArrayList<TransportationNode> startNodeOptions = city.getRouteNodes(startRow, startCol);
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
        for (TransportationNode transportationNode : city.getRouteNodes(endRow, endCol)) {
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
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row - 1, col));
                }
            }
        }

        if (col - 1 >= 0) { // Node to the left
            // print(String.format("Opening nodes at [%s, %s]", row, col - 1));
            openNode(this.currentRouteMatrix.getNode(row, col - 1));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row, col - 1));
                }
            }
        }

        if (row + 1 < this.cityHeight) { // Node below
            // print(String.format("Opening nodes at [%s, %s]", row + 1, col));
            openNode(this.currentRouteMatrix.getNode(row + 1, col));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row + 1, col));
                }
            }
        }

        if (col + 1 < this.cityWidth) { // Node to the right
            // print(String.format("Opening nodes at [%s, %s]", row, col + 1));
            openNode(this.currentRouteMatrix.getNode(row, col + 1));
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row, col + 1));
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
        for (Route route : this.city.routes) {
            TransportationNode node = route.getRouteNodeMatrix().getNode(row, col);
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
                nodeToCheck.getCost(startNode, goalNode, "emission");
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

    // TODO: highlight a line
    private void trackThePath() {
        // Backtrack and draw the best path
        TransportationNode current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                // things
                print(String.format("[%s, %s] %s", current.row, current.col, current.transportationType.name()));
            }
        }
    }

}