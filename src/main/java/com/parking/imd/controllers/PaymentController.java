package com.parking.imd.controllers;

import com.parking.imd.dao.impl.ParkingDaoImpl;
import com.parking.imd.dao.interfaces.ParkingDAO;
import com.parking.imd.domain.Movement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class PaymentController {

    @FXML
    private Label labelCashChange;

    @FXML
    private Label labelClientName;

    @FXML
    private Label labelPaymentMethod;

    @FXML
    private Label labelPaymentTime;

    @FXML
    private Label labelTotal;

    @FXML
    private TableView<Movement> tableViewMovements;

    @FXML
    private TableColumn<Movement, LocalDateTime> tableColumnEntry;

    @FXML
    private TableColumn<Movement, LocalDateTime> tableColumnExit;

    @FXML
    private TableColumn<Movement, Integer> tableColumnId;

    @FXML
    private TableColumn<Movement, String> tableColumnPlate;

    @FXML
    private TableColumn<Movement, String> tableColumnType;

    @FXML
    private TableColumn<Movement, LocalDateTime> tableColumnTime;

    @FXML
    private TableColumn<Movement, Double> tableColumnValue;

    private int method;
    private List<Movement> movementList;
    private Double paymentValue;

    @FXML
    void handleButtonCancel(ActionEvent event) {

    }

    @FXML
    void handleButtonPaymentMethod(ActionEvent event) {

    }

    @FXML
    void handleButtonReleasePayment(ActionEvent event) {

    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }

    public void setPaymentValue(Double paymentValue) {
        this.paymentValue = paymentValue;
    }

    public void setLabels() {
        ParkingDAO parkingDAO = new ParkingDaoImpl();
        List<String> methods = parkingDAO.getPaymentMethods();
        labelPaymentMethod.setText(methods.get(method));
        Locale ptBr = new Locale("pt", "BR");
        labelTotal.setText(NumberFormat.getCurrencyInstance(ptBr).format(paymentValue));
        String pattern = "dd/MM/YY HH:mm";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
        labelPaymentTime.setText(dateFormat.format(LocalDateTime.now()));
        loadTableViewMovements();
    }

    private void loadTableViewMovements() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idMovement"));
        tableColumnPlate.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getVehicle().getLicencePlate())
        );
        tableColumnType.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getVehicle().getTypeName())
        );
        tableColumnEntry.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        tableColumnEntry.setCellFactory(getColumnTableTimeCallback());
        tableColumnExit.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        tableColumnExit.setCellFactory(getColumnTableTimeCallback());
        tableColumnTime.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        tableColumnTime.setCellFactory(getColumnTableStringCallback());
        tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        ObservableList<Movement> movementObservableList = FXCollections.observableList(movementList);
        tableViewMovements.setItems(movementObservableList);
    }

    private Callback<TableColumn<Movement, LocalDateTime>, TableCell<Movement, LocalDateTime>> getColumnTableTimeCallback() {
        return col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String pattern = "dd/MM/YY HH:mm";
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(pattern);
                    setText(dateFormat.format(item));
                }
            }
        };
    }

    private Callback<TableColumn<Movement, LocalDateTime>, TableCell<Movement, LocalDateTime>> getColumnTableStringCallback() {
        return col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int diff = (int) ChronoUnit.HOURS.between(item, LocalDateTime.now());
                    String plural = diff == 1 ? "" : "s";
                    setText(diff + " hora" + plural);
                }
            }
        };
    }
}
