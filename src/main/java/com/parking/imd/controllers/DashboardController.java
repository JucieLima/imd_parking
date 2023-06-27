package com.parking.imd.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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
        AnchorPane.setLeftAnchor(rootPane,0.0);
        AnchorPane.setRightAnchor(rootPane,0.0);
        AnchorPane.setTopAnchor(rootPane,0.0);
        AnchorPane.setBottomAnchor(rootPane,0.0);
        dashboardPane.getChildren().setAll(rootPane);
    }
}
