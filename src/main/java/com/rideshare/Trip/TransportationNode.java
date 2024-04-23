package com.rideshare.Trip;

import com.rideshare.City.RouteNodeMatrix;
import com.rideshare.TileManager.GridPanePosition;
import com.rideshare.TransportationMode.*;

public class TransportationNode {
    TransportationNode parent;
    GridPanePosition position;
    TransportationMode modeOfTransport;
    TransportationType transportationType;
    RouteNodeMatrix routeMatrix;
    int fCost; // Sum of gCost and hCost
    int gCost; // Distance between start node and current node
    int hCost; // Distance between current node and goal node

    private boolean start;
    private boolean goal;
    private boolean solid;
    private boolean open;
    private boolean checked;
    private boolean canStop;

    public TransportationNode(GridPanePosition position, TransportationType transportationType,
            RouteNodeMatrix routeNodeMatrix) {
        this.position = position;
        this.routeMatrix = routeNodeMatrix;
        this.transportationType = transportationType;
        String name = routeNodeMatrix.getRouteName();
        switch (transportationType) {
            case BUS:
                modeOfTransport = new BusTransportationMode(name);
                break;
            case CAR:
                modeOfTransport = new CarTransportationMode(name);
                break;
            case TRAIN:
                modeOfTransport = new TrainTransportationMode(name);
                break;
            case WALKING:
                modeOfTransport = new WalkingTransportationMode(name);
                break;
            default:
                throw new IllegalArgumentException("Transportation type is invalid.");
        }

        // Check if it is a stop or what
        // renderNode();
    }

    public void reset() {
        start = false;
        goal = false;
        open = false;
        checked = false;
        parent = null;
    }

    public boolean canStop() {
        return this.canStop;
    }

    public boolean isSolid() {
        return this.solid;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setAsStart() {
        start = true;
    }

    public void setAsGoal() {
        goal = true;
    }

    public void setAsSolid() {
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsValidStop() {
        canStop = true;
    }

    public void setAsChecked() {
        checked = true;
    }

    public void getCost(TransportationNode startNode, TransportationNode endNode, TripType tripType) {
        // GET G COST
        int xDistance = Math.abs(this.position.col - startNode.position.col);
        int yDistance = Math.abs(this.position.row - startNode.position.row);
        this.gCost = xDistance + yDistance;

        // GET HCOST
        xDistance = Math.abs(this.position.col - endNode.position.col);
        yDistance = Math.abs(this.position.row - endNode.position.row);
        this.hCost = xDistance + yDistance;
        switch (tripType) {
            case EFFICIENT:
                this.hCost += this.modeOfTransport.getEmissionRate(); // Weighted by emission for now
                break;
            case FAST:
                this.hCost -= this.modeOfTransport.getSpeed(); // Weighted by speed
                break;
            case TRANSIT_ONLY:
                // TODO: turn off car nodes??
                // if (transportationType == TransportationType.TRAIN) {
                // this.hCost -= 1000;
                // }
                break;
            default:
                break;
        }

        // GET FCOST
        this.fCost = this.gCost + this.hCost;
    }
}
