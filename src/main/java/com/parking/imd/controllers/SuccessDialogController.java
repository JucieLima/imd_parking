package com.parking.imd.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SuccessDialogController {

    Stage stage;
    @FXML
    private Text textMsg;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg.setText(textMsg);
    }

    @FXML
    public void handleButtonCloseDialog(){
        this.stage.close();
    }

}
