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
}
