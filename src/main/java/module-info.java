module com.rideshare {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires org.jgrapht.core;
    opens com.rideshare to javafx.fxml;
    opens com.rideshare.GameManager to com.google.gson;

    exports com.rideshare;
    exports com.rideshare.GameManager;
    exports com.rideshare.TileManager;
    exports com.rideshare.Controllers;
    exports com.rideshare.TransportationMode;
}
