package com.parking.imd.controllers;

import com.parking.imd.data.StartDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartController{

    Stage stage;
    public void startDatabase(Stage stage) {
        this.stage = stage;
        new StartDatabase(this);
    }

    public void goToLogin() {
        stage.close();
        Stage newStage = new Stage();
        this.stage = newStage;
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/parking/imd/views/login.fxml"));
        try {
            Parent page = loader.load();
            LoginController controller = loader.getController();
            controller.setStartController(this);
            Scene scene = new Scene(page, 600, 400);
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setResizable(false);
            newStage.setTitle("IMD Parking");
            scene.getStylesheets().add(String.valueOf(StartController.class.getResource("/com/parking/imd/assets/style.css")));
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToDashBoard() {
        stage.close();
        Stage newStage = new Stage();
        this.stage = newStage;
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/parking/imd/views/dashboard.fxml"));
        try {
            Parent page = loader.load();
            DashboardController controller = loader.getController();
            Scene scene = new Scene(page, 900, 600);
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setResizable(true);
            newStage.setTitle("IMD Parking");
            scene.getStylesheets().add(String.valueOf(StartController.class.getResource("/com/parking/imd/assets/style.css")));
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}