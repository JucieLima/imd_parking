package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.MovementDAO;
import com.parking.imd.domain.Movement;
import com.parking.imd.domain.Parking;
import com.parking.imd.domain.Vehicle;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovementDaoImpl implements MovementDAO {

    Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public int total(){
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

    public List<Movement> list(Integer limit, Integer offset){
        List<Movement> movementList = new ArrayList<>();
        String sql = "SELECT movements.*, vehicles.id AS id_vehicle, vehicles.licence_plate, vehicles.type FROM movements " +
                "INNER JOIN vehicles ON movements.vehicle = vehicles.id ORDER BY entry_time DESC LIMIT ? OFFSET ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){

                Vehicle vehicle = new Vehicle();

                vehicle.setIdVehicle(resultSet.getInt("id_vehicle"));
                vehicle.setLicencePlate(resultSet.getString("licence_plate"));
                vehicle.setType(resultSet.getInt("type"));
                vehicle.setTypeName(getTypeName(resultSet.getInt("type")));

                Movement movement = new Movement();

                movement.setIdMovement(resultSet.getInt("id"));
                movement.setVehicle(vehicle);
                movement.setEntryTime(resultSet.getTimestamp("entry_time").toLocalDateTime());
                movement.setExitTime(resultSet.getTimestamp("exit_time") != null ? resultSet.getTimestamp("exit_time").toLocalDateTime() : null);
                movement.setValue(resultSet.getDouble("value"));
                movement.setStatusName(getStatusName(resultSet.getInt("status")));
                movement.setStatus(resultSet.getInt("status"));
                movementList.add(movement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movementList;
    }

    public void insert(Movement movement) {
        String sql = "INSERT INTO movements (vehicle, entry_time, status) VALUES (?,?,?)";
        java.sql.Timestamp entry = Timestamp.valueOf(movement.getEntryTime());
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, movement.getVehicle().getIdVehicle());
            statement.setTimestamp(2, entry);
            statement.setInt(3, movement.getStatus());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Movement find(Movement movement) {
        return null;
    }

    public void update(Movement movement){
        String sql = "UPDATE movements SET vehicle = ?, entry_time = ?, exit_time = ?, status = ?, value = ? " +
                "WHERE id = ?";
        Timestamp entry = Timestamp.valueOf(movement.getEntryTime());
        Timestamp exit = Timestamp.valueOf(movement.getExitTime());
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, movement.getVehicle().getIdVehicle());
            statement.setTimestamp(2, entry);
            statement.setTimestamp(3, exit);
            statement.setInt(4, movement.getStatus());
            statement.setDouble(5, movement.getValue());
            statement.setInt(6, movement.getIdMovement());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int idAccount) {

    }

    public String getTypeName(int type){
        VehicleDaoImpl vehicleDAO = new VehicleDaoImpl();
        return vehicleDAO.getTypeName(type);
    }

    @Override
    public List<Movement> vehicleList(Vehicle vehicle) {
        List<Movement> movements = new ArrayList<>();
        String sql = "SELECT movements.*, vehicles.id AS id_vehicle, vehicles.licence_plate, vehicles.type FROM movements " +
                "INNER JOIN vehicles ON movements.vehicle = vehicles.id WHERE vehicles.id = ? AND movements.status = 0 ORDER BY entry_time ASC ";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vehicle.getIdVehicle());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Vehicle v = new Vehicle();
                v.setIdVehicle(resultSet.getInt("id_vehicle"));
                v.setType(resultSet.getInt("type"));
                v.setTypeName(VehicleDaoImpl.getTypename(resultSet.getInt("type")));
                v.setLicencePlate(resultSet.getString("licence_plate"));

                Movement movement = new Movement();

                movement.setIdMovement(resultSet.getInt("id"));
                movement.setVehicle(v);
                movement.setEntryTime(resultSet.getTimestamp("entry_time").toLocalDateTime());
                movement.setExitTime(resultSet.getTimestamp("exit_time") != null ? resultSet.getTimestamp("exit_time").toLocalDateTime() : null);
                movement.setValue(resultSet.getDouble("value"));
                movement.setStatusName(getStatusName(resultSet.getInt("status")));
                movement.setStatus(resultSet.getInt("status"));
                movements.add(movement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movements;
    }

    public String getStatusName(int type){
        return switch (type) {
            case 0 -> "Veículo no Pátio";
            case 1 -> "Aguardando Pagamento";
            case 2 -> "Finalizado";
            default -> "Desconhecido";
        };
    }

}
