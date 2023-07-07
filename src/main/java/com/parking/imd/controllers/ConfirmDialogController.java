package com.parking.imd.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConfirmDialogController {

    @FXML
    private Text textConfirm;
    Stage stage;
    boolean confirm;

    public void setTextConfirm(String textConfirm) {
        this.textConfirm.setText(textConfirm);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isConfirmed() {
        return confirm;
    }

    @FXML
    void handleButtonCancel() {
        stage.close();
    }

    @FXML
    void handleButtonConfirm() {
        confirm = true;
        stage.close();
    }

}
