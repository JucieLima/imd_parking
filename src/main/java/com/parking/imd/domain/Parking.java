package com.parking.imd.domain;

import java.util.List;

public class Parking {
    private List<String> paymentMethods;
    private Integer availableVacancies;
    private Double balance;
    private Double yardOccupancyRate;
    private int entrances;
    private int exits;
    private int vacancies;

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Integer getAvailableVacancies() {
        return availableVacancies;
    }

    public void setAvailableVacancies(Integer availableVacancies) {
        this.availableVacancies = availableVacancies;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getYardOccupancyRate() {
        return yardOccupancyRate;
    }

    public void setYardOccupancyRate(Double yardOccupancyRate) {
        this.yardOccupancyRate = yardOccupancyRate;
    }

    public int getEntrances() {
        return entrances;
    }

    public void setEntrances(int entrances) {
        this.entrances = entrances;
    }

    public int getExits() {
        return exits;
    }

    public void setExits(int exits) {
        this.exits = exits;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }
}
