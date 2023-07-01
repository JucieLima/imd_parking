package com.parking.imd.dao;

import com.parking.imd.domain.Vehicle;

import java.sql.*;

public class VehicleDAO implements DAO {
    Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Integer insertVehicle(Vehicle vehicle) {
        Integer result = 0;
        String sql = "INSERT INTO vehicles (licence_plate, type) VALUES(?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, vehicle.getLicencePlate());
            statement.setInt(2, vehicle.getType());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Vehicle find(String plate, int type) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate(plate);
        vehicle.setType(type);
        String sql = "SELECT id, licence_plate, type FROM vehicles WHERE vehicles.licence_plate = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, plate);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                vehicle.setIdVehicle(resultSet.getInt("id"));
                vehicle.setLicencePlate(resultSet.getString("licence_plate"));
                vehicle.setType(resultSet.getInt("type"));
            }else{
               vehicle.setIdVehicle(insertVehicle(vehicle));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vehicle;
    }
}
