package com.parking.imd.util;

import javafx.scene.control.TextField;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateFields {

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException error) {
            return(false);
        }
    }

    public static boolean validatePlate(String plate) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]{3}\\d[a-zA-Z\\d]\\d{2}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(plate);
        return matcher.find();
    }

    public static TextField setTextFieldLicencePlate(TextField textFieldLicencePlate) {
        textFieldLicencePlate.setOnKeyReleased(e -> {
            if (textFieldLicencePlate.getText().length() > 7) {
                textFieldLicencePlate.setText(textFieldLicencePlate.getText().substring(0, 7));
            }
            textFieldLicencePlate.setText(textFieldLicencePlate.getText().toUpperCase());
            textFieldLicencePlate.positionCaret(textFieldLicencePlate.getText().length());
        });
        return textFieldLicencePlate;
    }

    public static boolean isEmail(String emailAddress){
        return EmailValidator.getInstance().isValid(emailAddress.trim());
    }
}