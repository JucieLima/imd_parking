package com.parking.imd.domain;

import java.util.List;

public class Client extends Person {
    Account account;
    List<Vehicle> vehicles;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
