package com.parking.imd.data;

import com.parking.imd.controllers.StartController;
import com.parking.imd.util.IMDParkingFiles;
import javafx.concurrent.Task;

import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StartDatabase {

    public StartDatabase(StartController startController) {
        StartDatabaseTask startTask = new StartDatabaseTask();
        Thread thread = new Thread(startTask);
        startTask.setOnSucceeded(event->startController.goToLogin());
        thread.start();
    }

    private class StartDatabaseTask extends Task<Void> {

        @Override
        protected Void call(){
            try {
                System.out.println("Salvando ranking de pagamento de veículos...");
                savePaymentRanking();
                System.out.println("limpando registros vencidos...");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Salvando notificacoes...");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Realizando backup...");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Iniciando aplicacao");
            }
            catch (Exception e) {
                System.out.println("Erro ao inicializar banco de dados!");
            }
            return null;
        }

        private void savePaymentRanking() {
            Map<String, Double> ranking = IMDParkingFiles.getMapPerVehicle(YearMonth.now());
            IMDParkingFiles.addVehiclePaymentMap(ranking);
        }
    }
}


