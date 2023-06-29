package com.parking.imd.dao;

import com.parking.imd.domain.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleDAO implements DAO{
    Connection connection;
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void insertVehicle(Vehicle vehicle){
        String sql = "INSERT INTO vehicles (licence_plate) VALUE(?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, vehicle.getLicencePlate());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
