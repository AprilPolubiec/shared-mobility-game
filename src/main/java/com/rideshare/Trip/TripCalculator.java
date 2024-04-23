package com.rideshare.Trip;

import java.util.ArrayList;

import com.rideshare.Utils;
import com.rideshare.City.City;
import com.rideshare.City.Route;
import com.rideshare.City.RouteNodeMatrix;
import com.rideshare.City.Mailbox;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TransportationMode.TransportationType;

// https://www.youtube.com/watch?v=2JNEme00ZFA&list=RDCMUCS94AD0gxLakurK-6jnqV1w&index=6
public class TripCalculator {
    private int cityHeight;
    private int cityWidth;

    private boolean goalReached = false;
    private City city;

    private TransportationNode _startNode, _goalNode, _currentNode;
    private RouteNodeMatrix currentRouteMatrix;
    private ArrayList<TransportationNode> openList = new ArrayList<>();
    private ArrayList<TransportationNode> checkedList = new ArrayList<>();

    public TripCalculator(City city) {
        this.city = city;
        this.cityHeight = this.city.getSize();
        this.cityWidth = this.city.getSize();
    }

    public ArrayList<Trip> calculateTrips(GridPanePosition startPosition, GridPanePosition endPosition) {
        ArrayList<Trip> trips = new ArrayList<Trip>();

        this._startNode = getStartNode(startPosition);
        this._currentNode = _startNode;
        this.currentRouteMatrix = this._currentNode.routeMatrix;

        print(String.format("Mailbox at [%s, %s]", endPosition.row, endPosition.col));
        GridPanePosition housePosition = Mailbox.getHousePosition(endPosition.row, endPosition.col);
        this._goalNode = getGoalNode(housePosition);
        print(String.format("Starting search from [%s, %s] to [%s, %s]", startPosition.row, startPosition.col,
                _goalNode.position.row,
                _goalNode.position.col));

        // Option 1: Bus
        Trip busTrip = runTransitPathFinding(this._startNode, this._goalNode, TransportationType.BUS);
        if (busTrip != null) {
            trips.add(busTrip);
            busTrip.print();
        }

        // Option 2: Train
        Trip trainTrip = runTransitPathFinding(this._startNode, this._goalNode, TransportationType.TRAIN);
        if (trainTrip != null) {
            trips.add(trainTrip);
            trainTrip.print();
        }

        // Option 2: Fastest trip
        Trip fastestTrip = runPathFinding(TripType.FAST, this._startNode,
                this._currentNode, this._goalNode);
        fastestTrip.print();
        trips.add(fastestTrip);

        // Option 3: Most efficient trip
        Trip mostEfficientTrip = runPathFinding(TripType.EFFICIENT, this._startNode,
                this._currentNode, this._goalNode);
        mostEfficientTrip.print();
        trips.add(mostEfficientTrip);
        return trips;
    }

    public TransportationNode getStartNode(GridPanePosition position) {
        // Start at the node with the lowest CO2 emissions
        TransportationNode startNode = null;
        ArrayList<TransportationNode> startNodeOptions = city.getRouteNodes(position);
        int minEmissions = Integer.MAX_VALUE;
        for (TransportationNode transportationNode : startNodeOptions) {
            if (transportationNode.modeOfTransport.getEmissionRate() < minEmissions) {
                startNode = transportationNode;
                minEmissions = transportationNode.modeOfTransport.getEmissionRate();
            }
        }
        return startNode;
    }

    public TransportationNode getGoalNode(GridPanePosition housePosition) {
        // The goal node is the walking path which is either in front, to the left or to
        // the
        // right
        if (housePosition.below().row < cityHeight) { // Prefer just below the house
            ArrayList<TransportationNode> nodesBelow = city.getRouteNodes(housePosition.below());
            for (TransportationNode node : nodesBelow) {
                if (node.transportationType == TransportationType.WALKING && !node.isSolid()) {
                    return node;
                }
            }
        }

        if (housePosition.toTheLeft().col >= 0) {
            ArrayList<TransportationNode> leftNodes = city.getRouteNodes(housePosition.toTheLeft());
            for (TransportationNode node : leftNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.isSolid()) {
                    return node;
                }
            }
        }

        if (housePosition.toTheRight().col < cityWidth) {
            ArrayList<TransportationNode> rightNodes = city.getRouteNodes(housePosition.toTheRight());
            for (TransportationNode node : rightNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.isSolid()) {
                    return node;
                }
            }
        }

        if (housePosition.above().row >= 0) {
            ArrayList<TransportationNode> aboveNodes = city.getRouteNodes(housePosition.above());
            for (TransportationNode node : aboveNodes) {
                if (node.transportationType == TransportationType.WALKING && !node.isSolid()) {
                    return node;
                }
            }
        }

        throw new IllegalArgumentException(
                String.format("There are no walking nodes available around [%s, %s]", housePosition.row,
                        housePosition.col)); // Not good
    }

    private void openNeighbors(GridPanePosition position, TransportationNode currentNode) {
        // print(String.format("Opening all neighbors to [%s, %s]", row, col));
        // OPEN NODES
        if (position.above().row >= 0) { // Node above
            // print(String.format("Opening nodes at [%s, %s]", row - 1, col));
            openNode(this.currentRouteMatrix.getNode(position.above()), currentNode);
            if (currentNode.canStop()) {
                // print("Valid switch spot - opening all transportation nodes.");
                // Check all of the surrounding route options!
                for (Route route : this.city.getRoutes()) {
                    openNode(route.getRouteNodeMatrix().getNode(position.above()), currentNode);
                }
            }
        }

        if (position.toTheLeft().col >= 0) { // Node to the left
            // print(String.format("Opening nodes at [%s, %s]", row, col - 1));
            openNode(this.currentRouteMatrix.getNode(position.toTheLeft()), currentNode);
            if (currentNode.canStop()) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.getRoutes()) {
                    openNode(route.getRouteNodeMatrix().getNode(position.toTheLeft()), currentNode);
                }
            }
        }

        if (position.below().row < this.cityHeight) { // Node below
            // print(String.format("Opening nodes at [%s, %s]", position.below(), col));
            openNode(this.currentRouteMatrix.getNode(position.below()), currentNode);
            if (currentNode.canStop()) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.getRoutes()) {
                    openNode(route.getRouteNodeMatrix().getNode(position.below()), currentNode);
                }
            }
        }

        if (position.toTheRight().col < this.cityWidth) { // Node to the right
            // print(String.format("Opening nodes at [%s, %s]", row, col + 1));
            openNode(this.currentRouteMatrix.getNode(position.toTheRight()), currentNode);
            if (currentNode.canStop()) {
                // print("Valid switch spot - opening all transportation nodes.");
                for (Route route : this.city.getRoutes()) {
                    openNode(route.getRouteNodeMatrix().getNode(position.toTheRight()), currentNode);
                }
            }
        }
    }

    // Sets all transportation nodes to closed at row/col
    private void closeAllNodes(GridPanePosition position, TransportationNode node) {
        // print(String.format("Closing all nodes at [%s, %s]", row, col));
        node.setAsChecked();
        checkedList.add(node);
        openList.remove(node);
        for (Route route : this.city.getRoutes()) {
            TransportationNode n = route.getRouteNodeMatrix().getNode(position);
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
        for (Route route : city.getRoutes()) {
            if (route.getTransportationType() == transportationType
                    && (routeName == null || route.getName().equals(routeName))) {
                transitMatrices.add(route.getRouteNodeMatrix());
            }
        }

        TransportationNode closestStation = null;
        // TODO: this actually doesn't take into account the walking distance, its just
        // x,y coords
        for (RouteNodeMatrix routeNodeMatrix : transitMatrices) {
            TransportationNode[][] matrix = routeNodeMatrix.get();
            for (int i = 0; i < matrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < matrix[rowIdx].length; j++) {
                    int colIdx = j;
                    if (matrix[i][j].canStop()) {
                        int xDistance = Math.abs(colIdx - goalNode.position.col);
                        int yDistance = Math.abs(rowIdx - goalNode.position.row);
                        int distance = xDistance + yDistance;

                        if (closestStation == null || distance < Math.abs(closestStation.position.col - goalNode.position.col)
                                + Math.abs(closestStation.position.row - goalNode.position.row)) {
                            closestStation = matrix[rowIdx][colIdx];
                        }
                    }
                }
            }
        }

        return closestStation;
    }

    public Trip runTransitPathFinding(TransportationNode startNode, TransportationNode goalNode,
            TransportationType transportationType) {
        TransportationNode startStation = getClosestStation(startNode, transportationType, null);
        if (startStation == null) { // No stations available!
            return null;
        }
        Utils.print(String.format("Start station: %s [%s, %s]", startStation.modeOfTransport.getName(),
                startStation.position.row, startStation.position.col));
        TransportationNode endStation = getClosestStation(goalNode, transportationType,
                startStation.modeOfTransport.getName());
        Utils.print(String.format("End station: %s [%s, %s]", endStation.modeOfTransport.getName(), endStation.position.row,
                endStation.position.col));

        // TODO: handle if start and end are the same, we can just skip this path finder
        if (startStation == endStation) {
            return null;
        }
        Trip baseLeg;

        TripType tripType = transportationType == TransportationType.TRAIN ? TripType.TRAIN : TripType.BUS;
        TransportationNode startStationEntryNode = getStartNode(new GridPanePosition(startStation.position.row, startStation.position.col));
        TransportationNode endStationEntryNode = getStartNode(new GridPanePosition(endStation.position.row, endStation.position.col));
        Trip firstLeg = runPathFinding(TripType.EFFICIENT, startNode, startNode, startStationEntryNode);
        Trip middleLeg = runPathFinding(tripType, startStation, startStation, endStation);
        Trip lastLeg = runPathFinding(TripType.EFFICIENT, endStationEntryNode, endStationEntryNode, goalNode);

        baseLeg = firstLeg == null ? middleLeg : firstLeg;
        if (firstLeg == null) { // Start at the middle leg
            baseLeg = middleLeg;
        } else {
            baseLeg = firstLeg;
            baseLeg.appendTrip(middleLeg);
        }

        baseLeg.appendTrip(lastLeg);
        baseLeg.setTripType(tripType);
        baseLeg.print();
        return baseLeg;
    }

    // Given a transit node, closes all nodes that are not on the same line
    private void closeNodesNotOnTransitRoute(TransportationNode startNode) {
        for (Route route : this.city.getRoutes()) {
            TransportationNode[][] nodeMatrix = route.getRouteNodeMatrix().get();

            for (int i = 0; i < nodeMatrix.length; i++) {
                int rowIdx = i;
                for (int j = 0; j < nodeMatrix[rowIdx].length; j++) {
                    TransportationNode n = nodeMatrix[i][j];
                    if (!route.getName().equals(startNode.modeOfTransport.getName())) {
                        n.setAsChecked();
                        checkedList.add(n);
                    }
                }
            }
        }
    }

    public Trip runPathFinding(TripType tripType, TransportationNode startNode, TransportationNode currentNode,
            TransportationNode goalNode) {
        if (tripType == TripType.BUS || tripType == TripType.TRAIN) {
            closeNodesNotOnTransitRoute(startNode);
        }
        if (startNode == goalNode || currentNode == goalNode) {
            return null;
        }
        while (goalReached == false) {
            GridPanePosition currentNodePosition = new GridPanePosition(currentNode.position.row, currentNode.position.col);

            closeAllNodes(currentNodePosition, currentNode);
            openNeighbors(currentNodePosition, currentNode);

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
            if (currentNode.position.row == goalNode.position.row && currentNode.position.col == goalNode.position.col) {
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
        Boolean isCheckableNode = node.isOpen() == false && node.isChecked() == false && node.isSolid() == false;

        if (isCheckableNode) {
            if (currentNode.modeOfTransport.hasStops()) {
                // We are currently on a form of public transportation - the only nodes we can
                // open are nodes on the same path
                if (!currentNode.canStop()
                        && !node.modeOfTransport.getName().equals(currentNode.modeOfTransport.getName()))
                    return;
            }
            if (node.modeOfTransport.hasStops()) {
                // If the node we are checking is a public transit - it has to either be a valid
                // stop or be on the transit path
                if (!node.canStop() && !node.modeOfTransport.getName().equals(currentNode.modeOfTransport.getName()))
                    return;
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
