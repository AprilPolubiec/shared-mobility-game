package com.rideshare;

public class TransportationNode {
    TransportationNode parent;
    int col;
    int row;
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
    TransportationMode modeOfTransport;
    TransportationType transportationType;
    RouteNodeMatrix routeMatrix;

    public TransportationNode(int col, int row, TransportationType transportationType, RouteNodeMatrix routeNodeMatrix) {
        this.col = col;
        this.row = row;
        this.routeMatrix = routeNodeMatrix;
        this.transportationType = transportationType;
        switch (transportationType) {
            case BUS:
                modeOfTransport = new BusTransportationMode("39A");
                break;
            case CAR:
                modeOfTransport = new CarTransportationMode("Honda Civic");
                break;
            case TRAIN:
                modeOfTransport = new TrainTransportationMode("B");  
                break;
            case WALKING:
                modeOfTransport = new WalkingTransportationMode("");
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

    public void setAsPath() {
    //    button.setStyle("-fx-background-color: #04d457;");
    // TODO: do something
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
                if(!modeOfTransport.hasStops() && transportationType != TransportationType.WALKING) {
                    setAsSolid(); // Prioritize train
                }
                if (transportationType == TransportationType.TRAIN) {
                    this.hCost -= 1000;
                }
                break;
            default:
                break;
        }
        

        // GET FCOST
        this.fCost = this.gCost + this.hCost;
    }
}
