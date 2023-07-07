package com.parking.imd.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class CashPaymentController implements Initializable {

    private Double payment;
    private Stage stage;
    boolean confirm = false;
    @FXML
    TextField textFieldValue;
    @FXML
    private Label labelValue;


    public void setValue(Double value) {
        Locale ptBr = new Locale("pt", "BR");
        labelValue.setText(NumberFormat.getCurrencyInstance(ptBr).format(value));
    }

    public Double getPayment() {
        return payment;
    }

    public boolean isConfirmed() {
        return confirm;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleButtonCancel() {
        this.stage.close();
    }

    @FXML
    void handleButtonConfirm() {
        if(confirmDialog()){
            textFieldValue.setText(textFieldValue.getText().replaceAll("\\.",""));
            textFieldValue.setText(textFieldValue.getText().replaceAll(",","."));
            try {
                payment = Double.parseDouble(textFieldValue.getText());
                confirm = true;
                stage.close();
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro!");
                alert.setHeaderText("Número inválido");
                alert.setContentText("Erro ao tentar realizar conversão");
            }
        }
    }

    private boolean confirmDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/parking/imd/views/confirmDialog.fxml"));
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(labelValue.getScene().getWindow());
        stage.setTitle("Confirmar pagamento!");
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        ConfirmDialogController controller = loader.getController();
        controller.setStage(stage);
        controller.setTextConfirm("Confirma o recebimento do valor?");
        stage.showAndWait();
        return controller.isConfirmed();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldValue.setOnKeyReleased(
                keyEvent -> {
                    String newText = textFieldValue.getText();
                    if (!newText.matches("\\d*")) {
                        textFieldValue.setText(newText.replaceAll("\\D", ""));
                    }

                    String text = textFieldValue.getText();


                    if (text.length() > 2) {
                        StringBuilder builder = new StringBuilder();
                        String start = textFieldValue.getText().substring(0, text.length() - 2);
                        String end = textFieldValue.getText().substring(text.length() - 2);

                        int aux = 0;
                        for (int i = start.length(); i > 0 ; i--){
                            if(aux %3==0 && start.length() > aux && aux >=3){
                                builder.insert(0,".");
                            }
                            builder.insert(0, text.charAt(i-1));
                            aux++;
                        }

                        builder.append(",");
                        builder.append(end);
                        textFieldValue.setText(builder.toString());
                    }

                    textFieldValue.positionCaret(textFieldValue.getLength());
                }
        );
    }
}
