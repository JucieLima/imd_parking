package com.parking.imd.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    AnchorPane dashboardPane;
    @FXML
    public void handleMenuItemMovements(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/movements.fxml"));
        BorderPane rootPane;
        try {
            rootPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MovementsController controller = loader.getController();
        controller.setDashboard(this);
        AnchorPane.setLeftAnchor(rootPane,0.0);
        AnchorPane.setRightAnchor(rootPane,0.0);
        AnchorPane.setTopAnchor(rootPane,0.0);
        AnchorPane.setBottomAnchor(rootPane,0.0);
        dashboardPane.getChildren().setAll(rootPane);
    }

    @FXML
    public void handleMenuItemReleaseEntry(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/entries.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dashboardPane.getScene().getWindow());
        stage.setTitle("Liberar Entrada de Veículo");
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        EntriesController controller = loader.getController();
        controller.setStage(stage);
        controller.setDashboard(this);
        stage.show();
    }

    public void handleMenuItemClientRegister() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/registerClient.fxml"));
        AnchorPane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("IMD Parking - Registro de Novo cliente");
        stage.setResizable(false);
        stage.initOwner(dashboardPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        RegisterClientController controller = loader.getController();
        controller.setStage(stage);
        stage.show();
    }

    @FXML
    void handleMenuItemClientsOpen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/clients.fxml"));
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
        dashboardPane.getChildren().setAll(root);
    }


    @FXML
    void handleMenuItemReleaseExit() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/exit.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dashboardPane.getScene().getWindow());
        stage.setTitle("Liberar Saída de Veículo");
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        ExitController controller = loader.getController();
        controller.setStage(stage);
        controller.setDashboard(this);
        stage.show();
    }

    public AnchorPane getDashboardPane() {
        return dashboardPane;
    }

    @FXML
    public void handleMenuItemEmployeeRegister(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/registerEmployee.fxml"));
        BorderPane rootPane;
        try {
            rootPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RegisterEmployeeController controller = loader.getController();
        controller.setDashboardController(this);
        AnchorPane.setLeftAnchor(rootPane,0.0);
        AnchorPane.setRightAnchor(rootPane,0.0);
        AnchorPane.setTopAnchor(rootPane,0.0);
        AnchorPane.setBottomAnchor(rootPane,0.0);
        dashboardPane.getChildren().setAll(rootPane);
    }
}
