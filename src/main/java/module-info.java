module com.parks.imd.imdparking {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.parks.imd.imdparking to javafx.fxml;
    exports com.parks.imd.imdparking;
}