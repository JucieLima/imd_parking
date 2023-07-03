package com.parking.imd.controllers;

import com.parking.imd.dao.impl.AccountDaoImpl;
import com.parking.imd.dao.impl.ClientDaoImpl;
import com.parking.imd.dao.impl.VehicleDaoImpl;
import com.parking.imd.dao.interfaces.ClientDAO;
import com.parking.imd.dao.interfaces.VehicleDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Account;
import com.parking.imd.domain.Client;
import com.parking.imd.domain.Vehicle;
import com.parking.imd.util.ValidateFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterClientController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxType;

    @FXML
    private Button buttonRemove;

    @FXML
    private Label labelTitle;

    @FXML
    private TableView<Vehicle> tableViewVehicles;


    @FXML
    private TableColumn<Vehicle, String> tableColumnPlate;

    @FXML
    private TableColumn<Vehicle, String> tableColumnType;

    @FXML
    private TextField textFieldCPF;

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldPlate;

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;

    {
        assert database != null;
        connection = database.connect();
    }

    VehicleDAO vehicleDAO = new VehicleDaoImpl();
    ClientDAO clientDAO = new ClientDaoImpl();

    ArrayList<Vehicle> vehicles = new ArrayList<>();

    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleButtonAddVehicle() {
        if(validateVehicleField()){
            Vehicle vehicle = vehicleDAO.find(textFieldPlate.getText(), comboBoxType.getSelectionModel().getSelectedIndex());
            vehicles.add(vehicle);
            loadTableView();
        }
    }

    private void loadTableView() {
        tableColumnPlate.setCellValueFactory(new PropertyValueFactory<>("licencePlate"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        ObservableList<Vehicle> observableList = FXCollections.observableList(vehicles);
        tableViewVehicles.setItems(observableList);
    }

    private boolean validateVehicleField() {
        String errorMsg = "";
        if(!ValidateFields.validatePlate(textFieldPlate.getText())) {
            errorMsg = "Digite uma placa válida\n";
        }

        if(comboBoxType.getSelectionModel().getSelectedItem() == null){
            errorMsg += "Selecione o tipo de veículo que você quer adicinar";
        }

        if(!errorMsg.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao adicionar veículo");
            alert.setHeaderText("Corrija isso antes!");
            alert.setContentText(errorMsg);
            alert.setResizable(false);
            alert.initOwner(comboBoxType.getScene().getWindow());
            alert.initModality(Modality.WINDOW_MODAL);
            alert.show();
            return false;
        }

        return  true;
    }

    public void handleButtonRemoveVehicle() {;
        vehicles.remove(tableViewVehicles.getSelectionModel().getSelectedItem());
        loadTableView();
    }

    public void handleButtonRegister() {
        clearCPF();
        if (validateFields() && validateVehicles()) {
            Client client = new Client();
            client.setNome(textFieldName.getText());
            client.setCPF(textFieldCPF.getText());
            Integer idClient = clientDAO.insert(client);
            setVehiclesOwner(idClient);
            setClientAccount(idClient);
            showSuccessDialog();
            stage.close();
        }
    }

    private void clearCPF() {
        String cpf = textFieldCPF.getText().replace(".","").replace("-","");
        textFieldCPF.setText(cpf);
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
        stage.initOwner(textFieldName.getScene().getWindow());
        SuccessDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setTextMsg("Cliente cadastrado com sucesso!");
        stage.showAndWait();
    }

    private void setClientAccount(Integer idClient) {
        Account account = new Account();
        account.setIdClient(idClient);
        account.setBalance(0);
        AccountDaoImpl accountDAO = new AccountDaoImpl();
        accountDAO.setConnection(connection);
        accountDAO.insert(account);
    }

    private void setVehiclesOwner(Integer client) {
        for (Vehicle v : vehicles) {
            vehicleDAO.setVehicleOwner(v.getIdVehicle(), client);
        }
    }

    private boolean validateVehicles() {
        for (Vehicle v : vehicles) {
            v = vehicleDAO.find(v.getLicencePlate(), v.getType());
            if (vehicleDAO.checkAlreadyOwned(v)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro ao cadastrar veículos");
                alert.setContentText("O veículo de placa (" + v.getLicencePlate() + ") já pertence a outro cliente!");
                alert.initModality(Modality.WINDOW_MODAL);
                alert.initOwner(textFieldName.getScene().getWindow());
                alert.setHeaderText("Erro ao cadastrar veículos");
                return false;
            }
        }
        return true;
    }

    private boolean validateFields() {
        String error = "";
        if (textFieldName.getText().isEmpty()) {
            error += "Digite o nome do cliente\n";
        }

        if (!ValidateFields.isCPF(textFieldCPF.getText())) {
            error += "Digite um CPF válido\n";
        }

        if (vehicles.isEmpty()) {
            error += "Você não adicionou nenhum veículo para este cliente\n";
        }

        if(checkClientCPF()){
            error += "O CPF que você informou já está cadastrado";
        }

        if (!error.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao cadastrar!");
            alert.setHeaderText("Corrija isso antes!");
            alert.setContentText(error);
            alert.initOwner(textFieldName.getScene().getWindow());
            alert.initModality(Modality.WINDOW_MODAL);
            alert.show();
            return false;
        }

        return true;
    }

    private boolean checkClientCPF() {
        setStringCPF();
        return clientDAO.checkClientCPF(textFieldCPF.getText());
    }

    private void setStringCPF() {
        StringBuilder cpf = new StringBuilder();
        for (int i = 0 ; i < textFieldCPF.getText().length() ; i++ ){
            if(i == 3 || i == 6){
                cpf.append(".");
            }else if(i == 9){
                cpf.append("-");
            }
            cpf.append(textFieldCPF.getText().charAt(i));
        }
        textFieldCPF.setText(cpf.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vehicleDAO.setConnection(connection);
        clientDAO.setConnection(connection);
        for (int i = 0; i < 7; i++) {
            comboBoxType.getItems().add(vehicleDAO.getTypeName(i));
        }
        tableViewVehicles.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue, newValue) -> setSelectedVehicle(newValue)
        );
        ValidateFields.setTextFieldLicencePlate(textFieldPlate);
    }

    private void setSelectedVehicle(Vehicle selectedVehicle) {
        buttonRemove.setDisable(selectedVehicle == null);
    }
}
