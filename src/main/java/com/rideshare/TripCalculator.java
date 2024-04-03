package com.rideshare;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.Scene;

// https://www.youtube.com/watch?v=2JNEme00ZFA&list=RDCMUCS94AD0gxLakurK-6jnqV1w&index=6
public class TripCalculator {
    int startRow;
    int startCol;
    int endRow;
    int endCol;

    int cityHeight;
    int cityWidth;

    boolean goalReached = false;
    Scene scene;
    City city;

    TransportationNode _startNode, _goalNode, _currentNode;
    RouteNodeMatrix currentRouteMatrix;
    ArrayList<TransportationNode> openList = new ArrayList<>();
    ArrayList<TransportationNode> checkedList = new ArrayList<>();

    public TripCalculator(City city) {
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
                this._startNode = transportationNode;
                minEmissions = transportationNode.co2EmissionRate;
            }
        }
        this._currentNode = _startNode;
        this.currentRouteMatrix = this._currentNode.routeMatrix;

        print(String.format("Mailbox at [%s, %s]", endRow, endCol));
        this._goalNode = getGoalNode(endRow, endCol - 1);
        print(String.format("Starting search from [%s, %s] to [%s, %s]", startRow, startCol, _goalNode.row,
                _goalNode.col));

        // Option 1: The fastest path
        Trip fastestTrip = runPathFinding(TripType.FAST, this._startNode, this._currentNode, this._goalNode);
        trips.add(fastestTrip);
        // Option 2: Get most CO2 efficient
        Trip mostEfficientTrip = runPathFinding(TripType.EFFICIENT, this._startNode, this._currentNode, this._goalNode);
        trips.add(mostEfficientTrip);
        // Option 3: Get public transit path
        // Trip publicTransitTrip = runTransitPathFinding();
        // trips.add(publicTransitTrip);
        return trips;
    }

    private TransportationNode getGoalNode(int row, int col) {
        // The goal node is the street which is either in front, to the left or to the
        // right
        if (row + 1 < cityHeight) { // Prefer just below the house
            ArrayList<TransportationNode> rightNodes = city.getRouteNodes(row + 1, col);
            for (TransportationNode node : rightNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.solid) {
                    return node;
                }
            }
        }

        if (col - 1 >= 0) {
            ArrayList<TransportationNode> leftNodes = city.getRouteNodes(row, col - 1);
            for (TransportationNode node : leftNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.solid) {
                    return node;
                }
            }
        }

        if (col + 1 < cityWidth) {
            ArrayList<TransportationNode> rightNodes = city.getRouteNodes(row, col + 1);
            for (TransportationNode node : rightNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.solid) {
                    return node;
                }
            }
        }

        if (row - 1 >= 0) {
            ArrayList<TransportationNode> aboveNodes = city.getRouteNodes(row - 1, col);
            for (TransportationNode node : aboveNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.solid) {
                    return node;
                }
            }
        }

        return city.getRouteNodes(row, col - 1).get(0); // Not good
    }

    private void openNeighbors(int row, int col) {
        // print(String.format("Opening all neighbors to [%s, %s]", row, col));
        // OPEN NODES
        if (row - 1 >= 0) { // Node above
            // print(String.format("Opening nodes at [%s, %s]", row - 1, col));
            openNode(this.currentRouteMatrix.getNode(row - 1, col));
            if (_currentNode.canStop) {
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
            if (_currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row, col - 1));
                }
            }
        }

        if (row + 1 < this.cityHeight) { // Node below
            // print(String.format("Opening nodes at [%s, %s]", row + 1, col));
            openNode(this.currentRouteMatrix.getNode(row + 1, col));
            if (_currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row + 1, col));
                }
            }
        }

        if (col + 1 < this.cityWidth) { // Node to the right
            // print(String.format("Opening nodes at [%s, %s]", row, col + 1));
            openNode(this.currentRouteMatrix.getNode(row, col + 1));
            if (_currentNode.canStop) {
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
        _currentNode.setAsChecked();
        checkedList.add(_currentNode);
        openList.remove(_currentNode);
        for (Route route : this.city.routes) {
            TransportationNode node = route.getRouteNodeMatrix().getNode(row, col);
            node.setAsChecked();
            checkedList.add(node);
            openList.remove(node);
            // print(String.format("[%s, %s] %s IS CLOSED", node.row, node.col,
            // node.transportationType.name()));
        }
    }

    private void print(String txt) {
        System.out.println(txt);
    }

    public Trip runTransitPathFinding() {
        RouteNodeMatrix trainNodeMatrix = null;

        // First - find the closest train to our starting nodes and ending nodes
        for (Route route : city.routes) {
            if (route.getTransportationType() == TransportationType.TRAIN) {
                trainNodeMatrix = route.getRouteNodeMatrix();
                break;
            }
        }

        TransportationNode startStation = trainNodeMatrix.getNode(0, 0);
        TransportationNode endStation = trainNodeMatrix.getNode(0, 0);

        TransportationNode[][] matrix = trainNodeMatrix.get();
        for (int i = 0; i < matrix.length; i++) {
            int rowIdx = i;
            for (int j = 0; j < matrix[rowIdx].length; j++) {
                int colIdx = j;
                if (matrix[i][j].canStop()) {
                    int xDistance = Math.abs(colIdx - _startNode.col);
                    int yDistance = Math.abs(rowIdx - _startNode.row);
                    int distance = xDistance + yDistance;

                    if (distance < Math.abs(startStation.col - _startNode.col)
                            + Math.abs(startStation.row - _startNode.row)) {
                        startStation = matrix[rowIdx][colIdx];
                    }

                    if (distance < Math.abs(endStation.col - this._goalNode.col)
                            + Math.abs(endStation.row - this._goalNode.row)) {
                        endStation = matrix[rowIdx][colIdx];
                    }
                }
            }
        }

        Trip firstLeg = runPathFinding(TripType.EFFICIENT, this._startNode, this._startNode, startStation);
        Trip middleLeg = runPathFinding(TripType.TRANSIT_ONLY, startStation, startStation, endStation);
        firstLeg.append(middleLeg);
        Trip lastLeg = runPathFinding(TripType.EFFICIENT, endStation, endStation, this._goalNode);
        firstLeg.append(lastLeg);

        return firstLeg;
    }

    public Trip runPathFinding(TripType tripType, TransportationNode startNode, TransportationNode currentNode,
            TransportationNode goalNode) {
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;
            // print(String.format("CURRENT NODE: [%s, %s] %s", row, col, currentNode.transportationType.name()));

            closeAllNodes(row, col);
            openNeighbors(row, col);

            // FIND BEST NODE
            int bestNodeIdx = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Check if the F cost is better than current best
                TransportationNode nodeToCheck = openList.get(i);

                if (tripType == TripType.TRANSIT_ONLY && nodeToCheck.transportationType != TransportationType.TRAIN) {
                    continue;
                }

                nodeToCheck.getCost(startNode, goalNode, tripType);

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

            if (openList.size() == 0) {
                print("NO OPRTIONS");
                goalNode.parent = currentNode.parent;
                Trip trip =  createTrip(tripType, goalNode, startNode);
                resetNodes();
                return trip;
            }

            currentNode = openList.get(bestNodeIdx);
            currentRouteMatrix = currentNode.routeMatrix; // Switch our route mat
            // print(String.format("BEST NODE: [%s, %s] %s", currentNode.row, currentNode.col,
            //         currentNode.transportationType.name()));
            if (currentNode.row == goalNode.row && currentNode.col == goalNode.col) {
                goalNode.parent = currentNode.parent;
                goalReached = true;
                // Do the thing
                Trip trip = createTrip(tripType, goalNode, startNode);
                resetNodes();
                return trip;
            }
        }
        return null;
    }

    private void openNode(TransportationNode node) {
        if (_currentNode.canStop() && !node.canStop()) {
            // Can only get off at a stop
            return;
        }
        if (node.open == false && node.checked == false && node.solid == false) {
            node.setAsOpen();
            node.parent = _currentNode;
            openList.add(node);
        } 
    }

    private void resetNodes() {
        for (Route route : city.getRoutes()) {
            TransportationNode[][] rm = route.getRouteNodeMatrix().get();
            for (int i = 0; i < rm.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < rm[rowIdx].length; j++) {
                    TransportationNode n = rm[rowIdx][j];
                    n.reset();
                }
            }
        }
        goalReached = false;
    }

    private Trip createTrip(TripType tripType, TransportationNode endNode, TransportationNode startNode) {
        Trip trip = new Trip(endNode, startNode, tripType);
        return trip;
    }

}