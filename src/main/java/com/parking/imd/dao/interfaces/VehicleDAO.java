package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Vehicle;
import javafx.scene.control.TextField;

public interface VehicleDAO extends DAO {
    Integer insert(Vehicle vehicle);
    Vehicle find(String plate, int type);
    void  update(Vehicle vehicle);
    void delete(int idVehicle);
    void setVehicleOwner(Integer idVehicle, Integer client);
    boolean checkAlreadyOwned(Vehicle v);
    String getTypeName(int i);
    Vehicle findOnYard(String plate);
}
