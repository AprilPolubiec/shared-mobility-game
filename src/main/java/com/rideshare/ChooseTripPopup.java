package com.rideshare;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public class ChooseTripPopup extends Popup {
    ObjectProperty<Trip> selectedTrip = new SimpleObjectProperty<>();

    public ChooseTripPopup(ArrayList<Trip> trips) {
        return;
    }

    public void onSelectedTripChanged(ChangeListener<? super Trip> listener) {
        this.selectedTrip.addListener(listener);
    }
    // Attributes
    // Trips

    // Methods
    // SelectTrip
}
