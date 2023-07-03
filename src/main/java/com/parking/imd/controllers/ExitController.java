package com.parking.imd.controllers;

import com.parking.imd.dao.impl.MovementDaoImpl;
import com.parking.imd.dao.impl.ParkingDaoImpl;
import com.parking.imd.dao.impl.VehicleDaoImpl;
import com.parking.imd.dao.interfaces.MovementDAO;
import com.parking.imd.dao.interfaces.ParkingDAO;
import com.parking.imd.dao.interfaces.VehicleDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Movement;
import com.parking.imd.domain.Vehicle;
import com.parking.imd.util.ValidateFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class ExitController implements Initializable {

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;

    {
        assert database != null;
        connection = database.connect();
    }

    private final VehicleDAO vehicleDAO = new VehicleDaoImpl();

    private Vehicle vehicle;
    private List<Movement> movements;

    @FXML
    private ComboBox<String> comboBoxPaymentMethod;

    @FXML
    private TextField textFieldLicencePlate;

    Stage stage;

    DashboardController dashboard;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    @FXML
    void handleButtonReleaseExit() {
        String error = "";
        if (comboBoxPaymentMethod.getSelectionModel().getSelectedItem() == null) {
            error += "Você não escolheu uma forma de pagamento\n";
        }

        vehicle = vehicleDAO.findOnYard(textFieldLicencePlate.getText());
        if (vehicle == null) {
            error += "Veículo não encontrado no pátio\n";
        }

        if (error.equals("")) {
            openPaymentScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro ao realizar saída");
            alert.setHeaderText("Aviso");
            alert.setContentText(error);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(textFieldLicencePlate.getScene().getWindow());
            alert.setResizable(false);
            alert.show();
        }
    }

    private void openPaymentScreen() {
        MovementDAO movementDAO = new MovementDaoImpl();
        movementDAO.setConnection(connection);
        movements = movementDAO.vehicleList(vehicle);
        Double value = calcValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/payment.fxml"));
        stage.close();
        AnchorPane root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnchorPane.setRightAnchor(root,0.0);
        AnchorPane.setLeftAnchor(root,0.0);
        AnchorPane.setTopAnchor(root,0.0);
        AnchorPane.setBottomAnchor(root,0.0);
        PaymentController controller = loader.getController();
        controller.setMethod(comboBoxPaymentMethod.getSelectionModel().getSelectedIndex());
        controller.setMovementList(movements);
        controller.setPaymentValue(value);
        controller.setLabels();
        dashboard.getDashboardPane().getChildren().setAll(root);
    }

    private Double calcValue() {
        ParkingDAO parkingDAO = new ParkingDaoImpl();
        parkingDAO.setConnection(connection);
        double value = 0.0;

        for (Movement movement : movements) {
            movement.setExitTime(LocalDateTime.now());
            Double price = parkingDAO.getTypeValue(movement.getVehicle().getType());
            long timeDiff = ChronoUnit.HOURS.between(movement.getEntryTime(), movement.getExitTime());
            double movementValue = price * timeDiff;
            movement.setValue(movementValue);
            value += movementValue;
        }

        return value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vehicleDAO.setConnection(connection);
        ParkingDAO parkingDAO = new ParkingDaoImpl();
        List<String> methods = parkingDAO.getPaymentMethods();
        ObservableList<String> observableList = FXCollections.observableList(methods);
        comboBoxPaymentMethod.setItems(observableList);
        ValidateFields.setTextFieldLicencePlate(textFieldLicencePlate);
    }
}
