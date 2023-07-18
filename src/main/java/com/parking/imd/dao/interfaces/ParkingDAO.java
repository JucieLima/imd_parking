package com.parking.imd.dao.interfaces;

import com.parking.imd.dao.DAO;
import com.parking.imd.domain.Parking;

import java.time.LocalDate;
import java.util.List;

public interface ParkingDAO extends DAO {
    Parking read(Parking parking);
    void  update(Parking parking);
    List<String> getPaymentMethods();
    Double getTypeValue(int type);
    Parking getDayBalance(LocalDate day);
}
