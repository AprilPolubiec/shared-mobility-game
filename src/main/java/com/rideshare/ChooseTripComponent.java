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
        AnchorPane.setLeftAnchor(component, (root.getScene().getWidth() / 2) + (TileUtils.TILE_SIZE_IN_PIXELS * 30 / 2));        
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
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            AnchorPane tripAnchor = UIComponentUtils.createStyledDialog(100.0, 250.0);
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
            Text label = new Text(String.format("Trip %s", i + 1));
            label.setFill(Color.WHITE);
            label.setFont(Font.font("Futura Bold", 21));
            label.setX((250 - label.getBoundsInLocal().getWidth()) / 2);
            AnchorPane.setTopAnchor(label, 0.0);
            tripAnchor.getChildren().add(label);

            // Put trip stats
            VBox statsVbox = new VBox();
            AnchorPane.setTopAnchor(statsVbox, 35.0);
            AnchorPane.setLeftAnchor(statsVbox, 10.0);

            // Speed
            GridPane speedStats = createStatSection("Speed", String.format("%s min", (int)trip.getDuration()));
            GridPane efficiencyStats = createStatSection("Efficiency", String.format("%sg", (int)trip.getEmission()));
            statsVbox.getChildren().addAll(speedStats, efficiencyStats);
            
            tripAnchor.getChildren().add(statsVbox);
            component.getChildren().add(tripAnchor);
        }
    }

    private GridPane createStatSection(String stat, String amtLabel) {
        Image fullCircle = new Image(App.class.getResource("/images/ui/green_circle.png").toString());
        Image emptyCircle = new Image(App.class.getResource("/images/ui/grey_circle.png").toString());

        GridPane stats = new GridPane();
        stats.setAlignment(Pos.CENTER_RIGHT);

        Text label = new Text(stat);
        label.setFont(Font.font("Futura Bold", 13));
        GridPane.setMargin(label, new Insets(0,10,0,0));
        stats.add(label, 0, 0);
        for (int i = 1; i < 6; i++) {
            ImageView imgView = new ImageView(fullCircle);
            imgView.setFitHeight(17.0);
            imgView.setFitWidth(17.0);
            GridPane.setMargin(imgView, new Insets(1));
            stats.add(imgView, i, 0);
        }

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
