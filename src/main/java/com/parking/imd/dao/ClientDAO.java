package com.parking.imd.dao;

import com.parking.imd.domain.Client;

import java.sql.*;
import java.time.LocalDateTime;

public class ClientDAO implements DAO{

    Connection connection;
    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Integer insert(Client client) {
        Timestamp now = new Timestamp(new java.util.Date().getTime());
        String sql = "INSERT INTO clients (name, CPF, created_at, updated_at) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, client.getNome());
            statement.setString(2, client.getCPF());
            statement.setTimestamp(3, now);
            statement.setTimestamp(4, now);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean checkClientCPF(String cpf) {
        String sql = "SELECT clients.CPF FROM clients WHERE CPF = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
