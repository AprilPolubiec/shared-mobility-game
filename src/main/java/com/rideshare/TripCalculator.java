package com.rideshare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

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

        this._startNode = getStartNode(startRow, startCol);
        this._currentNode = _startNode;
        this.currentRouteMatrix = this._currentNode.routeMatrix;

        print(String.format("Mailbox at [%s, %s]", endRow, endCol));
        GridPanePosition housePosition = Mailbox.getHousePosition(endRow, endCol);
        this._goalNode = getGoalNode(housePosition.row, housePosition.col); // Col - 1 b/c that is where the house is
        print(String.format("Starting search from [%s, %s] to [%s, %s]", startRow, startCol, _goalNode.row,
                _goalNode.col));

        // // Option 1: The fastest path
        // Trip fastestTrip = runPathFinding(TripType.FAST, this._startNode,
        // this._currentNode, this._goalNode);
        // fastestTrip.print();
        // trips.add(fastestTrip);
        // // // Option 2: Get most CO2 efficient
        // Trip mostEfficientTrip = runPathFinding(TripType.EFFICIENT, this._startNode,
        // this._currentNode, this._goalNode);
        // mostEfficientTrip.print();
        // trips.add(mostEfficientTrip);
        // Option 3: Get public transit path
        Trip publicTransitTrip = runTransitPathFinding(this._startNode, this._goalNode);
        if (publicTransitTrip != null) {
            trips.add(publicTransitTrip);
            publicTransitTrip.print();
        }
        Trip fastestTrip = runPathFinding(TripType.FAST, this._startNode,
                this._currentNode, this._goalNode);
        fastestTrip.print();
        trips.add(fastestTrip);
        return trips;
    }

    public TransportationNode getStartNode(int startRow, int startCol) {
        // Start at the node with the lowest CO2 emissions
        TransportationNode startNode = null;
        ArrayList<TransportationNode> startNodeOptions = city.getRouteNodes(startRow, startCol);
        int minEmissions = Integer.MAX_VALUE;
        for (TransportationNode transportationNode : startNodeOptions) {
            if (transportationNode.co2EmissionRate < minEmissions) {
                startNode = transportationNode;
                minEmissions = transportationNode.co2EmissionRate;
            }
        }
        return startNode;
    }

    private TransportationNode getGoalNode(int row, int col) {
        // The goal node is the walking path which is either in front, to the left or to
        // the
        // right
        if (row + 1 < cityHeight) { // Prefer just below the house
            ArrayList<TransportationNode> nodesBelow = city.getRouteNodes(row + 1, col);
            for (TransportationNode node : nodesBelow) {
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

        throw new IllegalArgumentException(
                String.format("There are no walking nodes available around [%s, %s]", row, col)); // Not good
    }

    private void openNeighbors(int row, int col, TransportationNode currentNode) {
        // print(String.format("Opening all neighbors to [%s, %s]", row, col));
        // OPEN NODES
        if (row - 1 >= 0) { // Node above
            // print(String.format("Opening nodes at [%s, %s]", row - 1, col));
            openNode(this.currentRouteMatrix.getNode(row - 1, col), currentNode);
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                // Check all of the surrounding route options!
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row - 1, col), currentNode);
                }
            }
        }

        if (col - 1 >= 0) { // Node to the left
            // print(String.format("Opening nodes at [%s, %s]", row, col - 1));
            openNode(this.currentRouteMatrix.getNode(row, col - 1), currentNode);
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row, col - 1), currentNode);
                }
            }
        }

        if (row + 1 < this.cityHeight) { // Node below
            // print(String.format("Opening nodes at [%s, %s]", row + 1, col));
            openNode(this.currentRouteMatrix.getNode(row + 1, col), currentNode);
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row + 1, col), currentNode);
                }
            }
        }

        if (col + 1 < this.cityWidth) { // Node to the right
            // print(String.format("Opening nodes at [%s, %s]", row, col + 1));
            openNode(this.currentRouteMatrix.getNode(row, col + 1), currentNode);
            if (currentNode.canStop) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.routes) {
                    openNode(route.getRouteNodeMatrix().getNode(row, col + 1), currentNode);
                }
            }
        }
    }

    // Sets all transportation nodes to closed at row/col
    private void closeAllNodes(int row, int col, TransportationNode node) {
        // print(String.format("Closing all nodes at [%s, %s]", row, col));
        node.setAsChecked();
        checkedList.add(node);
        openList.remove(node);
        for (Route route : this.city.routes) {
            TransportationNode n = route.getRouteNodeMatrix().getNode(row, col);
            n.setAsChecked();
            checkedList.add(n);
            openList.remove(n);
            // print(String.format("[%s, %s] %s IS CLOSED", node.row, node.col,
            // node.transportationType.name()));
        }
    }

    private void print(String txt) {
        System.out.println(txt);
    }

    // TODO: what about getting off and hopping on the next stop?
    public TransportationNode getClosestStation(TransportationNode goalNode, TransportationType transportationType,
            String routeName) {
        ArrayList<RouteNodeMatrix> transitMatrices = new ArrayList<RouteNodeMatrix>();
        // First - find the closest train to our starting nodes and ending nodes
        for (Route route : city.routes) {
            Boolean isTransit = route.getTransportationType() == TransportationType.TRAIN
                    || route.getTransportationType() == TransportationType.BUS;

            if ((transportationType == null && isTransit)
                    || (route.getTransportationType() == transportationType && route.name.equals(routeName))) {
                transitMatrices.add(route.getRouteNodeMatrix());
            }
        }

        TransportationNode closestStation = transitMatrices.get(0).getNode(0, 0);

        for (RouteNodeMatrix routeNodeMatrix : transitMatrices) {
            TransportationNode[][] matrix = routeNodeMatrix.get();
            for (int i = 0; i < matrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < matrix[rowIdx].length; j++) {
                    int colIdx = j;
                    if (matrix[i][j].canStop()) {
                        int xDistance = Math.abs(colIdx - goalNode.col);
                        int yDistance = Math.abs(rowIdx - goalNode.row);
                        int distance = xDistance + yDistance;

                        if (distance < Math.abs(closestStation.col - goalNode.col)
                                + Math.abs(closestStation.row - goalNode.row)) {
                            closestStation = matrix[rowIdx][colIdx];
                        }
                    }
                }
            }
        }

        return closestStation;
    }

    public Trip runTransitPathFinding(TransportationNode startNode, TransportationNode goalNode) {
        TransportationNode startStation = getClosestStation(startNode, null, null);
        TransportationNode endStation = getClosestStation(goalNode, startStation.transportationType,
                startStation.modeOfTransport.getName());

        // TODO: handle if start and end are the same, we can just skip this path finder
        if (startStation == endStation) {
            return null;
        }
        Trip firstLeg = runPathFinding(TripType.EFFICIENT, startNode, startNode, startStation);
        Trip middleLeg = runPathFinding(TripType.TRANSIT_ONLY, startStation, startStation, endStation);
        Trip lastLeg = runPathFinding(TripType.EFFICIENT, endStation, endStation, goalNode);
        firstLeg.appendTrip(middleLeg);
        firstLeg.appendTrip(lastLeg);
        firstLeg.print();
        return firstLeg;
    }

    private void closeNonTransitNodes(TransportationNode startNode) {
        for (Route route : this.city.routes) {
            TransportationNode[][] nodeMatrix = route.getRouteNodeMatrix().get();

            for (int i = 0; i < nodeMatrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < nodeMatrix[rowIdx].length; j++) {
                    TransportationNode n = nodeMatrix[i][j];
                    if ((route.getTransportationType() != TransportationType.TRAIN
                            && route.getTransportationType() != TransportationType.BUS)
                            || !route.name.equals(startNode.modeOfTransport.getName())) {
                        n.setAsChecked();
                        checkedList.add(n);
                    }
                }
            }
        }
    }

    public Trip runPathFinding(TripType tripType, TransportationNode startNode, TransportationNode currentNode,
            TransportationNode goalNode) {
        if (tripType == TripType.TRANSIT_ONLY) {
            closeNonTransitNodes(startNode);
        }
        while (goalReached == false) {
            int col = currentNode.col;
            int row = currentNode.row;

            closeAllNodes(row, col, currentNode);
            openNeighbors(row, col, currentNode);

            // FIND BEST NODE
            int bestNodeIdx = 0;
            int bestNodeFCost = 999;
            int iterations = 0;
            int maxIterations = 1000;
            for (int i = 0; i < openList.size(); i++) {
                // Check if the F cost is better than current best
                TransportationNode nodeToCheck = openList.get(i);

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
                Trip trip = createTrip(tripType, goalNode, startNode);
                resetNodes();
                return trip;
            }

            currentNode = openList.get(bestNodeIdx);
            currentRouteMatrix = currentNode.routeMatrix; // Switch our route mat
            // print(String.format("BEST NODE: [%s, %s] %s", currentNode.row,
            // currentNode.col,
            // currentNode.transportationType.name()));
            if (currentNode.row == goalNode.row && currentNode.col == goalNode.col) {
                goalNode.parent = currentNode.parent;
                goalReached = true;
                // Do the thing
                Trip trip = createTrip(tripType, goalNode, startNode);
                resetNodes();
                return trip;
            }
            iterations++;

            if (iterations >= maxIterations) {
                System.out.println("Max iterations reached, no route found");
                return null;
            }
        }
        return null;
    }

    private void openNode(TransportationNode node, TransportationNode currentNode) {
        Boolean isCheckableNode = node.open == false && node.checked == false && node.solid == false;

        if (isCheckableNode) {
            if (currentNode.modeOfTransport.hasStops()) {
                // We are currently on a form of public transportation - the only nodes we can open are nodes on the same path
                if (!currentNode.canStop && !node.modeOfTransport.getName().equals(currentNode.modeOfTransport.getName())) return;
            }
            if (node.modeOfTransport.hasStops()) {
                // If the node we are checking is a public transit - it has to either be a valid stop or be on the transit path
                if (!node.canStop && !node.modeOfTransport.getName().equals(currentNode.modeOfTransport.getName())) return;
            }
            node.setAsOpen();
            node.parent = currentNode;
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
        checkedList = new ArrayList<>();
        openList = new ArrayList<>();
    }

    private Trip createTrip(TripType tripType, TransportationNode endNode, TransportationNode startNode) {
        Trip trip = new Trip(endNode, startNode, tripType);
        return trip;
    }

}