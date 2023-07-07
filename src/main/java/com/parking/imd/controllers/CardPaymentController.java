package com.parking.imd.controllers;

import com.parking.imd.util.CardAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

public class CardPaymentController {

    @FXML
    private Label labelValue;
    private Stage stage;
    boolean confirm = false;
    private Double payment;


    public Double getPayment() {
        return payment;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isConfirmed() {
        return confirm;
    }

    public void startPayment(Stage stage, Double value){
        this.stage = stage;
        this.payment = value;
        Locale ptBr = new Locale("pt", "BR");
        labelValue.setText(NumberFormat.getCurrencyInstance(ptBr).format(value));
        new CardAPI(this);
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

}
