package com.rideshare;
// import java.awt.Color;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// import javax.swing.JButton;

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
    GridPane grid;
    TransportationMode modeOfTransport;

    // For visualizations
    Button button = new Button();
    int tileSize = 16;

    public TransportationNode(int col, int row, TransportationType transportationType) {
        this.col = col;
        this.row = row;
        // this.grid = grid;

        switch (transportationType) {
            case BUS:
                modeOfTransport = new BusTransportationMode();
                break;
            case CAR:
                modeOfTransport = new CarTransportationMode();
                break;
            case TRAIN:
                modeOfTransport = new TrainTransportationMode();  
                break;
            case WALKING:
                modeOfTransport = new WalkingTransportationMode();
                break;
            default:
                break;
            
        }
        co2EmissionRate = modeOfTransport.getEmissionRate();

        // Check if it is a stop or what
        // renderNode();
    }

    // public void renderNode() {
    //     button.setPrefSize(tileSize, tileSize);
    //     grid.add(button, col, row);
    // }
    
    public void setAsStart() {
        button.setStyle("-fx-background-color: #324aa8; ");
        start = true;
    }

    public void setAsGoal() {
        button.setStyle("-fx-background-color: #ebca10; ");
        goal = true;
    }

    public void setAsSolid() {
        button.setStyle("-fx-background-color: #000000; ");
        solid = true;
    }

    public void setAsOpen() {
        open = true;
    }

    public void setAsValidStop() {
        canStop = true;
    }

    public void setAsChecked() {
        if(start == false && goal == false) {
            button.setStyle("-fx-background-color: #eb9834; ");
        }
        checked = true;
    }

    public void setAsPath() {
       button.setStyle("-fx-background-color: #04d457;");
    }

    public void getCost(TransportationNode startNode, TransportationNode endNode) {
        // GET G COST
        int xDistance = Math.abs(this.col - startNode.col);
        int yDistance = Math.abs(this.row - startNode.row);
        this.gCost = xDistance + yDistance;

        // GET HCOST
        xDistance = Math.abs(this.col - endNode.col);
        yDistance = Math.abs(this.row - endNode.row);
        this.hCost = xDistance + yDistance + this.co2EmissionRate;

        // GET FCOST
        this.fCost = this.gCost + this.hCost;
    }
}
