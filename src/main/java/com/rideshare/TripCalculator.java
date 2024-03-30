package com.rideshare;

import java.util.ArrayList;

import com.rideshare.GameManager.GameController;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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

    public TripCalculator(City city) {
        this.city = city;

        // drawNodeMap();
    }

    // public void calculateCosts() {
    //     // Calculate costs for each node in the city relative to our current start node
    //     for (int i = 0; i < this.city.size; i++) {
    //         int rowIdx = i;
    //         for (int j = 0; j < this.city.size; j++) {
    //             int colIdx = j;
    //             ArrayList<TransportationNode> nodes = city.getRouteNodes(rowIdx, colIdx);
    //             for (TransportationNode transportationNode : nodes) {
    //                 transportationNode.getCost(startNode, goalNode);
    //             }
    //         }
    //     }
    // }

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