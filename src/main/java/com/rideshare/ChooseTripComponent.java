package com.rideshare;

import java.util.ArrayList;

import com.rideshare.TileManager.TileUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ChooseTripComponent {
    ObjectProperty<Trip> selectedTrip = new SimpleObjectProperty<>();
    AnchorPane _root;
    ArrayList<Trip> trips;
    private VBox component = new VBox();

    public ChooseTripComponent(AnchorPane root) {
        _root = root;
        AnchorPane.setLeftAnchor(component, TileUtils.TILE_SIZE_IN_PIXELS * 30.0);        
        AnchorPane.setTopAnchor(component, 25.0);        
        _root.getChildren().add(component);
        return;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    public void clear() {
        this.component.getChildren().clear();
    }

    public void render() {
        Text titleText = new Text("Choose trip");
        titleText.setFont(new Font("Futura Bold", 24));
        component.getChildren().add(titleText);
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            AnchorPane tripAnchor = UIComponentUtils.createStyledDialog(100.0, 300.0);
            tripAnchor.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    _root.setCursor(Cursor.HAND);
                    tripAnchor.setEffect(new ColorAdjust(0, 0, -0.2, 0));
                }
            });
    
            tripAnchor.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    _root.setCursor(Cursor.DEFAULT);
                    tripAnchor.setEffect(new ColorAdjust(0, 0, 0, 0));
                }
            });

            tripAnchor.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    selectedTrip.set(trip);
                }
                
            });
    
            // Put header (ie: FAST, EFFICIENT, TRANSIT ONLY)
            Text label = new Text("");
            switch (trip.getTripType()) {
                case EFFICIENT:
                    label = new Text(String.format("WALK"));
                    break;
                case FAST:
                    label = new Text(String.format("DRIVE"));
                    break;
                case TRANSIT_ONLY:
                    label = new Text(String.format("PUBLIC TRANSIT"));
                    break;
                default:
                    break;
            }
            
            label.setFill(Color.WHITE);
            label.setFont(Font.font("Futura Bold", 21));
            label.setX((300 - label.getBoundsInLocal().getWidth()) / 2);
            AnchorPane.setTopAnchor(label, 0.0);
            tripAnchor.getChildren().add(label);

            // Put trip stats
            VBox statsVbox = new VBox();
            AnchorPane.setTopAnchor(statsVbox, 35.0);
            AnchorPane.setLeftAnchor(statsVbox, 20.0);

            // Speed
            GridPane speedStats = createStatSection("Speed", trip);
            GridPane efficiencyStats = createStatSection("Efficiency", trip);
            statsVbox.getChildren().addAll(speedStats, efficiencyStats);
            
            tripAnchor.getChildren().add(statsVbox);
            component.getChildren().add(tripAnchor);
        }
    }

    private GridPane createStatSection(String stat, Trip trip) {
        Image fullCircle = new Image(App.class.getResource("/images/ui/green_circle.png").toString());
        Image emptyCircle = new Image(App.class.getResource("/images/ui/grey_circle.png").toString());

        GridPane stats = new GridPane();
        stats.setAlignment(Pos.CENTER_RIGHT);

        Text label = new Text(stat);
        label.setFont(Font.font("Futura Bold", 13));
        GridPane.setMargin(label, new Insets(0,10,0,0));
        stats.add(label, 0, 0);
        
        int numFullCircles = 0;
        switch (trip.getTripType()) {
            case EFFICIENT:
                numFullCircles = stat.equals("Speed") ? 1 : 5;
                break;
            case FAST:
                numFullCircles = stat.equals("Speed") ? 5 : 1;
                break;
            case TRANSIT_ONLY:
                numFullCircles = stat.equals("Speed") ? 3 : 3;
                break;
            default:
                break;
        }
    
        for (int i = 1; i < 6; i++) {
            ImageView imgView = i <= numFullCircles ? new ImageView(fullCircle) : new ImageView(emptyCircle);
            imgView.setFitHeight(17.0);
            imgView.setFitWidth(17.0);
            GridPane.setMargin(imgView, new Insets(1));
            stats.add(imgView, i, 0);
        }
        String amtLabel = stat.equals("Speed") ? String.format("%s min", (int)trip.getDuration()) : String.format("%s g", (int)trip.getEmission());
        Text amountText = new Text(amtLabel);
        amountText.setFont(Font.font("Futura", 13));
        stats.add(amountText, 7, 0);
        return stats;
    }

    public void onSelectedTripChanged(ChangeListener<? super Trip> listener) {
        this.selectedTrip.addListener(listener);
    }
    // Attributes
    // Trips

    // Methods
    // SelectTrip
}
