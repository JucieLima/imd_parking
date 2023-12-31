package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Movement;
import com.parking.imd.domain.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovementDAO extends DAO {
    void insert(Movement movement);
    Movement find(Movement movement);
    void  update(Movement movement);
    void delete(int idAccount);
    List<Movement> list(Integer limit, Integer offset);
    int total();
    String getTypeName(int i);
    List<Movement> vehicleList(Vehicle vehicle);
}
