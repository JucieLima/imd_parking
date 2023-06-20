package com.parking.imd.controllers;

import com.parking.imd.data.StartDatabase;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    Stage stage;
    public void startDatabase(Stage stage) {
        this.stage = stage;
        new StartDatabase(this);
    }

    public void goToDashboard() {
        stage.hide();
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/parking/imd/views/login.fxml"));
        try {
            Parent page = loader.load();
            Scene scene = new Scene(page, 600, 400);
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setResizable(false);
            newStage.setTitle("IMD Parking");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
