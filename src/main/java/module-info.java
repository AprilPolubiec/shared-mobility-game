module com.rideshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.rideshare to javafx.fxml;
    opens com.rideshare.GameManager to com.google.gson;

    exports com.rideshare;
    exports com.rideshare.GameManager;
}
