package com.rideshare;

public class TransportationNode {
    TransportationNode parent;
    public int col;
    public int row;
    int gCost; // Distance between start node and current node
    int hCost; // Distance between current node and goal node
    int fCost; // Sum of gCost and hCost
    int co2EmissionRate; // https://www.youtube.com/watch?v=T0Qv4-KkAUo
    int speed;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;
    boolean canStop;
    public TransportationMode modeOfTransport;
    public TransportationType transportationType;
    RouteNodeMatrix routeMatrix;

    public TransportationNode(int col, int row, TransportationType transportationType, RouteNodeMatrix routeNodeMatrix) {
        this.col = col;
        this.row = row;
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
                break;
            
        }
        co2EmissionRate = modeOfTransport.getEmissionRate();
        speed = modeOfTransport.getSpeed();

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
        int xDistance = Math.abs(this.col - startNode.col);
        int yDistance = Math.abs(this.row - startNode.row);
        this.gCost = xDistance + yDistance;

        // GET HCOST
        xDistance = Math.abs(this.col - endNode.col);
        yDistance = Math.abs(this.row - endNode.row);
        this.hCost = xDistance + yDistance;
        switch (tripType) {
            case EFFICIENT:
                this.hCost += this.co2EmissionRate; // Weighted by emission for now
                break;
            case FAST:
                this.hCost -= this.speed; // Weighted by speed
                break;
            case TRANSIT_ONLY:
                // TODO: turn off car nodes??
                // if (transportationType == TransportationType.TRAIN) {
                //     this.hCost -= 1000;
                // }
                break;
            default:
                break;
        }
        

        // GET FCOST
        this.fCost = this.gCost + this.hCost;
    }
}
