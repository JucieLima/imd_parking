module com.parks.imd.imdparking {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.parking.imd to javafx.fxml;
    exports com.parking.imd;
    exports com.parking.imd.controllers to javafx.fxml;
}