package com.parking.imd.util;

import com.parking.imd.controllers.CardPaymentController;
import javafx.concurrent.Task;

import java.util.concurrent.TimeUnit;

public class CardAPI {
    public CardAPI(CardPaymentController controller) {
        CardAPITask startTask = new CardAPITask(controller.getPayment());
        Thread thread = new Thread(startTask);
        startTask.setOnSucceeded(event->{
            controller.setConfirm(true);
            controller.getStage().close();
        });
        thread.start();
    }

    private static class CardAPITask extends Task<Double> {

        Double paymentValue;

        public CardAPITask(Double paymentValue) {
            this.paymentValue = paymentValue;
        }

        @Override
        protected Double call(){
            try {
                System.out.println("Aguardano confirmação da máquina de catão de crédito");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Pagamento realizado!");
            }
            catch (Exception e) {
                System.out.println("Erro ao tentar realizar pagamento!");
            }
            return paymentValue;
        }
    }
}
