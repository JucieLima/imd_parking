package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.ParkingDAO;
import com.parking.imd.domain.Parking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkingDaoImpl implements ParkingDAO {
    Connection connection;
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Parking read(Parking parking) {
        return null;
    }

    @Override
    public void update(Parking parking) {

    }

    @Override
    public List<String> getPaymentMethods() {
        return new ArrayList<>(Arrays.asList("Dinheiro", "Cartão", "PIX", "Conta"));
    }

    @Override
    public Double getTypeValue(int type) {
        String sql = "SELECT price FROM vehicles_types WHERE type = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, type);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
