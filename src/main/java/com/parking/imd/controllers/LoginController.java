package com.parking.imd.controllers;

import com.parking.imd.dao.EmployeeDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.util.Objects;

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

    //Atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;
    {
        assert database != null;
        connection = database.connect();
    }

    private EmployeeDAO employeeDAO = new EmployeeDAO();


    @FXML
    void handleButtonLogin() {
        if(validateLogin(textFieldEmail.getText(), textFieldPassword.getText())){
            startController.goToDashBoard();
        }else{
            msgPane.setVisible(true);
            msgText.setText("Erro ao logar: dados não conferem!");
        }
    }

    private boolean validateLogin(String email, String password){

        if(Objects.equals(email, "") || Objects.equals(password, "")){
            email = "jucielima@gmail.com";
            password = "123456";
        }

        employeeDAO.setConnection(connection);
        Employee employee = employeeDAO.login(email);
        if(employee == null){
            return  false;
        }else return employee.getPassword().equals(password);
    }

    public void setStartController(StartController startController) {
        this.startController = startController;
    }
}
