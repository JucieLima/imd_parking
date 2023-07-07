package com.parking.imd.controllers;

import com.parking.imd.dao.impl.MovementDaoImpl;
import com.parking.imd.dao.impl.ParkingDaoImpl;
import com.parking.imd.dao.interfaces.MovementDAO;
import com.parking.imd.dao.interfaces.ParkingDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Movement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
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
    private Label labelReceivedCash;

    @FXML
    private TableView<Movement> tableViewMovements;

    @FXML
    private ComboBox<String>  comboBoxPaymentMethod;

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

    @FXML
    Button buttonPrintInvoice;

    private int method;
    private boolean finished = false;
    private List<Movement> movementList;
    private Double paymentValue;

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;
    {
        assert database != null;
        connection = database.connect();
    }

    MovementDAO movementDAO = new MovementDaoImpl();

    @FXML
    void handleButtonCancel() {

    }

    @FXML
    void handleButtonReleasePayment() {
        switch (method) {
            case 0 -> releaseCashPayment();
            case 1 -> releaseCardPayment();
            case 2 -> System.out.println("Pagamento via PIX"); //todo
            case 3 -> System.out.println("Lancamento de debito na conta do cliente"); //todo
            default -> {
                openAlert(
                        "Erro ao confirmar pagamento",
                        "ERRO!",
                        "Método de pagamento não suportado!",
                        Alert.AlertType.ERROR
                );
            }
        }
    }

    private void releaseCardPayment() {
        if(!finished){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/cardPayment.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Receber pagamento por cartã de crédito");
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            CardPaymentController controller = loader.getController();
            controller.startPayment(stage, this.paymentValue);
            stage.showAndWait();
            if(controller.isConfirmed()){
                finishPayment(controller.getPayment());
            }else{
                openAlert(
                        "Pagamento não realizado",
                        "AVISO",
                        "Não foi possível concluir o pagamento!",
                        Alert.AlertType.WARNING
                );
            }
        }else{
            openAlert(
                    "Pagamento finalzado",
                    "AVISO",
                    "Este pagamento já está finalizado",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private void releaseCashPayment() {
        if(!finished){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/cashPayment.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Receber pagamento em dinheiro");
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            CashPaymentController controller = loader.getController();
            controller.setStage(stage);
            controller.setValue(this.paymentValue);
            stage.showAndWait();
            if(controller.isConfirmed()){
                finishPayment(controller.getPayment());
            }
        }else{
            openAlert(
                    "Pagamento finalzado",
                    "AVISO",
                    "Este pagamento já está finalizado",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private void finishPayment(Double payment) {
        if(payment >= paymentValue){
            finished = true;
            updateMovementList(2);
            Locale ptBr = new Locale("pt", "BR");
            labelCashChange.setText(NumberFormat.getCurrencyInstance(ptBr).format(payment - paymentValue));
            labelReceivedCash.setText(NumberFormat.getCurrencyInstance(ptBr).format(payment));
            buttonPrintInvoice.setDisable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/successDialog.fxml"));
            AnchorPane anchorPane;
            try {
                anchorPane = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(labelCashChange.getScene().getWindow());
            stage.setTitle("Pagamento finalizado!");
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            SuccessDialogController controller = loader.getController();
            controller.setTextMsg("Pagamento realizado com sucesso!");
            controller.setStage(stage);
            stage.show();
        }else{
            openAlert(
                    "Pagamento não realizado",
                    "Confira o valor!",
                    "O valor informado é inferior ao débito!",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private void updateMovementList(int status) {
        movementDAO.setConnection(connection);
        movementList.forEach(movement -> {
            movement.setStatus(status);
            movementDAO.update(movement);
        });
    }

    public void setMethod(int method) {
        this.method = method;
        ParkingDAO parkingDAO = new ParkingDaoImpl();
        List<String> methods = parkingDAO.getPaymentMethods();
        ObservableList<String> observableList = FXCollections.observableList(methods);
        comboBoxPaymentMethod.setItems(observableList);
        comboBoxPaymentMethod.getSelectionModel().select(method);
        comboBoxPaymentMethod.getSelectionModel().selectedItemProperty().addListener(
                item -> {
                    this.method = comboBoxPaymentMethod.getSelectionModel().getSelectedIndex();
                    labelPaymentMethod.setText(methods.get(this.method));
                }
        );
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
                    String timeString = diff == 1 ? "hora" : "horas";
                    if(diff < 1){
                        diff = (int) ChronoUnit.MINUTES.between(item, LocalDateTime.now());
                        timeString = diff == 1 ? "minuto" : "minutos";
                    }
                    setText(diff + " " + timeString);
                }
            }
        };
    }

    @FXML
    public void handleButtonPrintInvoice() {
        if(finished){
            System.out.println("Imprimindo comprovamte...");
        }else{
            openAlert(
                    "Aguardando pagamento",
                    "Aviso",
                    "Só é possivel imprimir comprovantes após a finalização do pagamento",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private void openAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(labelCashChange.getScene().getWindow());
        alert.setResizable(false);
        alert.show();
    }
}
