package com.parking.imd.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LoginController {

    @FXML
    private TextFlow msgPane;

    @FXML
    private Text msgText;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private PasswordField textFieldPassword;

    StartController startController;

    boolean logged = false;

    @FXML
    void handleButtonLogin() {
        if(textFieldEmail.getText().equals("jucielima@gmail.com") && textFieldPassword.getText().equals("123456")){
            logged = true;
        }

        if(logged){
            startController.goToDashBoard();
        }else{
            msgPane.setVisible(true);
            msgText.setText("Erro ao logar: dados não conferem!");
        }
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }
}
