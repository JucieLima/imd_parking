package com.parking.imd.controllers;

import com.parking.imd.dao.impl.ClientDaoImpl;
import com.parking.imd.dao.impl.VehicleDaoImpl;
import com.parking.imd.dao.interfaces.ClientDAO;
import com.parking.imd.dao.interfaces.VehicleDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Client;
import com.parking.imd.domain.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

    @FXML
    private AnchorPane anchorPaneClients;

    @FXML
    private Pagination pagination;

    @FXML
    private Label labelClientBalance;

    @FXML
    private Label labelClientCPF;

    @FXML
    private Label labelClientName;

    @FXML
    private Label labelClientVehicles;

    @FXML
    private TableColumn<Client, String> tableColumnCPF;

    @FXML
    private TableColumn<Vehicle, String> tableColumnPlate;

    @FXML
    private TableColumn<Vehicle, String> tableColumnType;

    @FXML
    private TableColumn<Client, String> tableColumnName;

    @FXML
    private TableColumn<Client, String> tableColumnID;

    @FXML
    private TableView<Vehicle> tableViewVehicles;

    @FXML
    private TextField textFieldSearchClients;

    @FXML
    private TextField textFieldSearchVehicles;

    @FXML
    private TableView<Client> tableViewClients;

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;

    {
        assert database != null;
        connection = database.connect();
    }

    private final ClientDAO clientDAO = new ClientDaoImpl();
    private final VehicleDAO vehicleDAO = new VehicleDaoImpl();

    int maxItems = 100;

    List<Client> clientList;
    List<Vehicle> clientVehicleList;

    @FXML
    void searchClients(ActionEvent event) {

    }

    @FXML
    void searchVehicles(ActionEvent event) {

    }

    @FXML
    void handleButtonDelete(ActionEvent event) {

    }

    @FXML
    void handleButtonUpdate(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientDAO.setConnection(connection);
        vehicleDAO.setConnection(connection);
        int pages = (int) Math.ceil((float) clientDAO.getClientsCount()/maxItems);
       if(pages == 0) pages = 1;
        pagination.setPageCount(pages);
        anchorPaneClients.getChildren().remove(tableViewClients);
        pagination.setPageFactory(index -> {
            clientList = clientDAO.list(maxItems, index*maxItems);
            loadTableViewClients();
            return tableViewClients;
        });
        tableViewClients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            loadSelectedClient(newValue);
        });
    }

    private void loadSelectedClient(Client client) {
        if(client != null){
            client.setAccount(clientDAO.getAccount(client.getId()));
            client.setVehicles(clientDAO.getClientVehicles(client.getId()));
            Locale ptBr = new Locale("pt", "BR");
            labelClientBalance.setText(NumberFormat.getCurrencyInstance(ptBr).format(client.getAccount().getBalance()));
            labelClientName.setText(client.getNome());
            labelClientCPF.setText(client.getCPF());
            labelClientVehicles.setText(String.valueOf(client.getVehicles().size()));
            clientVehicleList = client.getVehicles();
            loadTableviewVehicles();
        }
    }

    private void loadTableviewVehicles() {
        tableColumnPlate.setCellValueFactory(new PropertyValueFactory<>("licencePlate"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        ObservableList<Vehicle> observableList = FXCollections.observableList(clientVehicleList);
        tableViewVehicles.setItems(observableList);
    }

    private void loadTableViewClients() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("CPF"));
        ObservableList<Client> observableList = FXCollections.observableList(clientList);
        tableViewClients.setItems(observableList);
    }

    @FXML
    void handleButtonSettleDebt(){
        Client  client = tableViewClients.getSelectionModel().getSelectedItem();
        String msg = "";
        if(client != null){
            if(client.getAccount().getBalance() < 0){
                //todo
            }else{
                msg = "O cliente selecionado não possui débito";
            }
        }else{
            msg = "Você precisa selecionar um cliente na lista";
        }
        if(!msg.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro ao quitar débidto");
            alert.setHeaderText("Aviso");
            alert.setContentText(msg);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(labelClientBalance.getScene().getWindow());
            alert.show();
        }
    }

}
