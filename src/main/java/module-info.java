module com.parks.imd.imdparking {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires commons.validator;

    opens com.parking.imd to javafx.fxml;
    opens com.parking.imd.controllers to javafx.fxml;
    exports com.parking.imd;
    exports com.parking.imd.controllers to javafx.fxml;
    opens com.parking.imd.domain to javafx.base;
}