package com.parking.imd;

import com.parking.imd.controllers.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("views/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 171);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        StartController controller = fxmlLoader.getController();
        controller.startDatabase(stage);
        stage.setTitle("IMD Parking");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}