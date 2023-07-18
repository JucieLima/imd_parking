package com.parking.imd.controllers;

import com.parking.imd.dao.impl.EmployeeDaoImpl;
import com.parking.imd.dao.interfaces.EmployeeDAO;
import com.parking.imd.data.DataBaseConfig;
import com.parking.imd.data.Database;
import com.parking.imd.data.DatabaseFactory;
import com.parking.imd.domain.Employee;
import com.parking.imd.util.ValidateFields;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RegisterEmployeeController implements Initializable {

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldConfirm;
    @FXML
    private ComboBox<String> comboBoxFunction;

    @FXML
    private TextField textFieldCPF;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldName;

    private DashboardController dashboardController;

    private final Database database = DatabaseFactory.getDatabase(DataBaseConfig.DATABASE);
    private final Connection connection;

    {
        assert database != null;
        connection = database.connect();
    }

    EmployeeDAO employeeDAO = new EmployeeDaoImpl();

    @FXML
    void handleButtonRegister() {
        clearCPF();
        if(validateData()){
            setStringCPF();
            Employee employee = new Employee();
            employee.setNome(textFieldName.getText());
            employee.setEmail(textFieldEmail.getText());
            employee.setCPF(textFieldCPF.getText());
            employeeDAO.insert(employee);
        }
    }



    private void clearCPF() {
        String cpf = textFieldCPF.getText().replace(".","").replace("-","");
        textFieldCPF.setText(cpf);
    }

    private boolean validateData() {
        String error =  "";
        if(textFieldName.getText().equals("")){
            error += "Digite o nome do funcionário\n";
        }

        if(textFieldCPF.getText().equals("")){
            error += "Digite o CPF do funcionário\n";
        }

        if(ValidateFields.isCPF(textFieldCPF.getText())){
            error += "O CPF informado não é valido\n";
        }

        if(textFieldEmail.getText().equals("")){
            error += "Digite o email do funcionário\n";
        }

        if(ValidateFields.isEmail(textFieldEmail.getText())){
            error += "O Email informado não é valido\n";
        }

        if(passwordField.getLength() > 6){
            error += "A senha precisa ter pelo menos 6 caracteres!\n";
        }

        if(!passwordField.equals(passwordFieldConfirm)){
            error += "A senha de confirmação não bate";
        }


        if(!error.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erroa ao cadastrar funcionário");
            alert.setHeaderText("Verifique os seguintes erros");
            alert.setContentText(error);
            alert.setResizable(false);
            alert.initOwner(textFieldName.getScene().getWindow());
            alert.initModality(Modality.WINDOW_MODAL);
            alert.show();
            return false;
        }
        return true;
    }

    private void setStringCPF() {
        StringBuilder cpf = new StringBuilder();
        for (int i = 0 ; i < textFieldCPF.getText().length() ; i++ ){
            if(i == 3 || i == 6){
                cpf.append(".");
            }else if(i == 9){
                cpf.append("-");
            }
            cpf.append(textFieldCPF.getText().charAt(i));
        }
        textFieldCPF.setText(cpf.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxFunction.getItems().add("Operador");
        comboBoxFunction.getItems().add("Gerente");
        employeeDAO.setConnection(connection);
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
