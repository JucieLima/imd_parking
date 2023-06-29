package com.parking.imd.dao;

import com.parking.imd.domain.Movement;
import com.parking.imd.domain.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovementDAO implements DAO {

    Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getMovementListCount(){
        int result = 0;
        String sql = "SELECT COUNT(*) AS total FROM movements";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Movement> getMovementList(Integer limit, Integer offset){
        List<Movement> movementList = new ArrayList<>();
        String sql = "SELECT movements.*, vehicles.id AS id_vehicle, vehicles.licence_plate FROM movements " +
                "INNER JOIN vehicles ON movements.vehicle = vehicles.id LIMIT ? OFFSET ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Vehicle vehicle = new Vehicle();
                vehicle.setIdVehicle(resultSet.getInt("id_vehicle"));
                vehicle.setLicencePlate(resultSet.getString("licence_plate"));
                Movement movement = new Movement();
                movement.setIdMovement(resultSet.getInt("id"));
                movement.setVehicle(vehicle);
                movement.setEntryTime(resultSet.getTimestamp("entry_time"));
                movement.setExitTime(resultSet.getTimestamp("exit_time"));
                movement.setValue(resultSet.getDouble("value"));
                movementList.add(movement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movementList;
    }

    public void insert(Movement movement) {
        String sql = "INSERT INTO movements (vehicle, entry_time, exit_time, status, value) VALUES (?,?,?,?,?)";
        Date entry = new Date(movement.getEntryTime().getTime());
        Date exit = new Date(movement.getExitTime().getTime());
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, movement.getVehicle().getIdVehicle());
            statement.setDate(2, entry);
            statement.setDate(3, exit);
            statement.setInt(4, movement.getStatus());
            statement.setDouble(5, movement.getValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
