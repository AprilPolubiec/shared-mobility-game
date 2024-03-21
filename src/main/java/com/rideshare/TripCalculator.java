package com.rideshare;

import java.util.ArrayList;

import com.rideshare.GameManager.GameController;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class TripCalculator {
    final int maxCol = 50;
    final int maxRow = 50;
    final int nodeSize = 16;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;
    boolean goalReached = false;
    GameController gameController;
    Scene scene;
    City city;

    // NODE
    TransportationNode[][] node = new TransportationNode[maxCol][maxRow];
    TransportationNode startNode, goalNode, currentNode;
    ArrayList<TransportationNode> openList = new ArrayList<>();
    ArrayList<TransportationNode> checkedList = new ArrayList<>();

    // calculateTrips (locationA, locationB, city)
    public TripCalculator(GameController gameController, City city) {
        this.gameController = gameController;
        this.scene = gameController.get_scene();
        this.city = city;

        drawNodeMap();
    }

    private void setStartNode(int col, int row) {
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }

    private void setGoalNode(int col, int row) {
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNode(int col, int row) {
        node[col][row].setAsSolid();
    }

    public void drawNodeMap() {
        // Make grid - this will be done elsewhere ultimately
        GridPane grid = new GridPane();

        // PLACE NODES
        int col = 0;
        int row = 0;
        // This loop should actually be going through each index of the city grid and
        // looking for route
        while (col < maxCol && row < maxRow) {
            node[col][row] = new TransportationNode(col, row, TransportationType.WALKING, grid);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
        this.gameController.get_root().getChildren().add(grid);

        setStartNode(0, 0);
        setGoalNode(20, 20);

        while(col < maxCol && row < maxRow) {
            node[col][row].getCost(node[0][0], node[20][20]);;
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
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
                openNode(node[col][row - 1]);
            }

            if (col - 1 >= 0) { // Node to the left
                openNode(node[col - 1][row]);
            }

            if (row + 1 < maxRow) { // Node below
                openNode(node[col][row + 1]);
            }

            if (col + 1 < maxCol) { // Node to the right
                openNode(node[col + 1][row]);
            }

            // FIND BEST NODE
            int bestNodeIdx = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Check if the F cost is better than current best
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIdx = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                // If F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIdx).gCost) {
                        bestNodeIdx = i;
                    }
                }
            }

            // After loop, get the best node
            currentNode = openList.get(bestNodeIdx);
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
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

    public Trip[] calculateTrips(Position startLocation, Position endLocation, City city) {
        Trip[] trips = {};
        // Get routes in the city
        Route[] routes = city.getRoutes();
        for (Route route : routes) {
            // Check if startLocation and endLocation are a valid stop
            if (!(route.isStop(startLocation) && route.isStop(endLocation))) {
                continue;
            }
            // Starting at startLocation, walk through the route until the next stop or
            // endLocation is reached. If we hit a stop, we run the search from there.

            // TODO: This is a pathfinding algorithm :')
            // Consider just finding a library to do this
        }

        drawNodeMap();

        return trips;
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