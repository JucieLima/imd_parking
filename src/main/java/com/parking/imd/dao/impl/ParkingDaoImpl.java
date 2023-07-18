package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.ParkingDAO;
import com.parking.imd.domain.Parking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            if (resultSet.next()) {
                return resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Parking getDayBalance(LocalDate day) {
        Parking parking = new Parking();
        parking.setBalance(getBalanceDay(day));
        parking.setEntrances(getDayEhtrances(day));
        parking.setExits(getDayExits(day));
        parking.setVacancies(parkingVacancies());
        parking.setAvailableVacancies(parkingAvailableVacancies(parking));
        parking.setYardOccupancyRate(
                (
                        ((double) parking.getVacancies() - (double) parking.getAvailableVacancies())
                        / (double) parking.getVacancies()
                ) * 100.0
        );
        return parking;
    }

    private int parkingAvailableVacancies(Parking parking) {
        String sql = "SELECT COUNT(status) AS total FROM movements WHERE status = 0";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parking.getVacancies() - resultSet.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int parkingVacancies() {
        String sql = "SELECT vacancies FROM parking WHERE id = 1";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("vacancies");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int getDayExits(LocalDate day) {
        LocalDateTime timeStart = LocalDateTime.of(day, LocalTime.MIN);
        LocalDateTime timeEnd = LocalDateTime.of(day, LocalTime.MAX);
        Timestamp start = Timestamp.valueOf(timeStart);
        Timestamp end = Timestamp.valueOf(timeEnd);
        String sql = "SELECT COUNT(exit_time) AS exits FROM movements WHERE status != 0 AND exit_time BETWEEN ? AND ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("exits");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private int getDayEhtrances(LocalDate day) {
        LocalDateTime timeStart = LocalDateTime.of(day, LocalTime.MIN);
        LocalDateTime timeEnd = LocalDateTime.of(day, LocalTime.MAX);
        Timestamp start = Timestamp.valueOf(timeStart);
        Timestamp end = Timestamp.valueOf(timeEnd);
        String sql = "SELECT COUNT(entry_time) AS entrances FROM movements WHERE status = 0 AND entry_time BETWEEN ? AND ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("entrances");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Double getBalanceDay(LocalDate day) {
        LocalDateTime timeStart = LocalDateTime.of(day, LocalTime.MIN);
        LocalDateTime timeEnd = LocalDateTime.of(day, LocalTime.MAX);
        Timestamp start = Timestamp.valueOf(timeStart);
        Timestamp end = Timestamp.valueOf(timeEnd);
        String sql = "SELECT SUM(value) AS balance FROM movements WHERE status = 2 AND exit_time BETWEEN ? AND ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, start);
            statement.setTimestamp(2, end);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}
