package com.parking.imd.controllers;

import com.parking.imd.dao.impl.MovementDaoImpl;
import com.parking.imd.dao.interfaces.MovementDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Movement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovementsController implements Initializable {

    @FXML
    private Button handleButtonOpenGateEntry;

    @FXML
    private Button handleButtonOpenGateExit;

    @FXML
    private Button handleButtonPayment;

    @FXML
    private Button handleButtonVehicleEntrance;

    @FXML
    private Button handleButtonVehicleExit;

    @FXML
    private Label labelBalance;

    @FXML
    private Label labelEntriesNumber;

    @FXML
    private Label labelExitsNumber;

    @FXML
    private Label labelRate;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<Movement, LocalDateTime> tableColumnEntryTime;

    @FXML
    private TableColumn<Movement, String> tableColumnType;

    @FXML
    private TableColumn<Movement, LocalDateTime> tableColumnExitTime;

    @FXML
    private TableColumn<Movement, Integer> tableColumnId;

    @FXML
    private TableColumn<Movement, String> tableColumnLicencePlate;

    @FXML
    private TableColumn<Movement, String> tableColumnStatus;

    @FXML
    private TableColumn<Movement, Double> tableColumnValue;

    @FXML
    private TableView<Movement> tableViewMovements;

    @FXML
    private Label vacanciesLabels;

    @FXML
    private AnchorPane anchorPanePageView;

    //Atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;
    {
        assert database != null;
        connection = database.connect();
    }

    MovementDAO movementDAO = new MovementDaoImpl();
    int maxItems = 200;
    List<Movement> movements = new ArrayList<>();

    DashboardController dashboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movementDAO.setConnection(connection);
        int pages = (int) Math.ceil((float) movementDAO.total()/maxItems);
        pages = pages != 0 ? pages : 1;
        pagination.setPageCount(pages);
        anchorPanePageView.getChildren().remove(tableViewMovements);
        pagination.setPageFactory(pageIndex -> {
            movements = movementDAO.list(maxItems, pageIndex*maxItems);
            loadTableMovements();
            return createPage();
        });
        //updateTimes();
    }

    private Node createPage() {
        anchorPanePageView.getChildren().remove(pagination);
        AnchorPane.setTopAnchor(pagination, 0.0);
        AnchorPane.setRightAnchor(pagination, 0.0);
        AnchorPane.setBottomAnchor(pagination, 5.0);
        AnchorPane.setLeftAnchor(pagination, 0.0);
        anchorPanePageView.getChildren().add(pagination);
        return tableViewMovements;
    }

    private void loadTableMovements() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idMovement"));
        tableColumnLicencePlate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVehicle().getLicencePlate()));
        tableColumnType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVehicle().getTypeName()));
        tableColumnEntryTime.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
        tableColumnEntryTime.setCellFactory(getColumnTableTimeCallback());
        tableColumnExitTime.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
        tableColumnExitTime.setCellFactory(getColumnTableTimeCallback());
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("statusName"));
        tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        ObservableList<Movement> observableList = FXCollections.observableList(movements);
        tableViewMovements.setItems(observableList);
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

    public void setDashboard(DashboardController dashboard) {
        this.dashboard = dashboard;
    }

    @FXML
    void handleButtonVehicleEntrance() {
        dashboard.handleMenuItemReleaseEntry();
    }

    @FXML
    void handleButtonVehicleExit() {
        dashboard.handleMenuItemReleaseExit();
    }
}
