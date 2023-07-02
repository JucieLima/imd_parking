package com.parking.imd.controllers;

import com.parking.imd.dao.impl.MovementDaoImpl;
import com.parking.imd.dao.impl.VehicleDaoImpl;
import com.parking.imd.dao.interfaces.MovementDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Movement;
import com.parking.imd.domain.Vehicle;
import com.parking.imd.util.ValidateFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import java.util.ResourceBundle;

public class EntriesController implements Initializable {

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;

    {
        assert database != null;
        connection = database.connect();
    }

    MovementDAO movementDAO = new MovementDaoImpl();

    @FXML
    private ComboBox<String> comboBoxType;

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
    void handleButtonReleaseEntry() {
        String error = "";
        if (!ValidateFields.validatePlate(textFieldLicencePlate.getText())) {
            error = "Por favor, informe uma placa válida!\n";
        }

        if (comboBoxType.getSelectionModel().getSelectedItem() == null){
            error += "Selecione um tipo de veículo!";
        }

        if(!error.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(textFieldLicencePlate.getScene().getWindow());
            alert.setTitle("Placa inválida");
            alert.setContentText(error);
            alert.setHeaderText("Corrija isso antes");
            alert.show();
        }else{
            releaseNewEntry();
        }
    }

    private void releaseNewEntry() {
        VehicleDaoImpl vehicleDAO = new VehicleDaoImpl();
        vehicleDAO.setConnection(connection);
        Vehicle vehicle = vehicleDAO.find(textFieldLicencePlate.getText(), comboBoxType.getSelectionModel().getSelectedIndex());
        Movement movement = new Movement();
        movement.setStatus(0);
        movement.setEntryTime(LocalDateTime.now());
        movement.setVehicle(vehicle);
        movementDAO.insert(movement);
        showSuccessDialog();
        stage.close();
        dashboard.handleMenuItemMovements();
    }

    private void showSuccessDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/successDialog.fxml"));
        AnchorPane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Sucesso!");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(textFieldLicencePlate.getScene().getWindow());
        SuccessDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setTextMsg("Entrada realizada com sucesso!");
        stage.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementDAO.setConnection(connection);
        for (int i = 0; i < 7; i++) {
            comboBoxType.getItems().add(movementDAO.getTypeName(i));
        }
        setTextFieldLicencePlate();
    }

    private void setTextFieldLicencePlate() {
        textFieldLicencePlate.setOnKeyReleased(e -> {
            if (textFieldLicencePlate.getText().length() > 7) {
                textFieldLicencePlate.setText(textFieldLicencePlate.getText().substring(0, 7));
            }
            textFieldLicencePlate.setText(textFieldLicencePlate.getText().toUpperCase());
            textFieldLicencePlate.positionCaret(textFieldLicencePlate.getText().length());
        });
    }
}
