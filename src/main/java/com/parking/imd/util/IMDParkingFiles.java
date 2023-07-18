package com.parking.imd.util;

import com.parking.imd.domain.Movement;

import java.io.*;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IMDParkingFiles {
    public static void addPaymentFile(List<Movement> movements){
        movements.forEach(movement -> {
            File file = new File("." + File.separator + "files" + File.separator + YearMonth.now() + "-vehicles_payments.csv");
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                String str = movement.getVehicle().getLicencePlate() + ","
                        + movement.getValue() + ","
                        + movement.getEntryTime() + ","
                        + movement.getExitTime() + "\n";
                writer.write(str);
                writer.close();
            } catch (IOException e) {
                System.out.println("nao foi possivel ler o arquivo informado!");
            }
        });
    }

    //Este método cria um Map no qual a chave é a placa do veículo e o valor é um Double que corresponde ao total gasto por
    //cada veículo
    public static Map<String, Double> getMapPerVehicle(YearMonth yearMonth){
        Map<String, Double> map = new HashMap<>();
        try{
            FileReader file = new FileReader("." + File.separator + "files" + File.separator + yearMonth + "-vehicles_payments.csv");
            BufferedReader reader = new BufferedReader(file);
            String line = reader.readLine();
            while (line != null){
                String[] strings = line.split(",");
                String plate = strings[0];
                Double value = Double.parseDouble(strings[1]);

                /*if(map.get(plate) == null){
                    map.put(plate, value);
                }else{
                    map.put(plate, value + map.get(plate));
                } */
                map.merge(plate, value, Double::sum);
                line = reader.readLine();
             }
        }catch (IOException e){
            System.out.println("nao foi possivel ler o arquivo informado!");
        }
        return map;
    }

    public static void addVehiclePaymentMap(Map<String, Double> map){
        clearOldRanking();
        File file = new File("." + File.separator + "files" + File.separator + "vehicles_ranking.csv");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.flush();
            BufferedWriter writer = new BufferedWriter(fileWriter);
            map.forEach((key, value) -> {
                String line = key + "," + value + "\n";
                try {
                    writer.write(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void clearOldRanking() {
        File file = new File("." + File.separator + "files" + File.separator + "vehicles_ranking.csv");
        if(file.exists()){
            file.delete();
        }
    }
}
