module com.rideshare {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rideshare to javafx.fxml;
    exports com.rideshare;
}
