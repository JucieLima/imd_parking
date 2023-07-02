package com.parking.imd.dao;

import com.parking.imd.domain.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDAO implements DAO{
    Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void insert(Account account) {
        String sql = "INSERT INTO account (balance, client) VALUES (?, ?) ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, account.getBalance());
            statement.setInt(2, account.getIdClient());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
