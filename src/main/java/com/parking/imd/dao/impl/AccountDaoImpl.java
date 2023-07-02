package com.parking.imd.dao.impl;

import com.parking.imd.dao.interfaces.AccountDAO;
import com.parking.imd.domain.Account;
import com.parking.imd.domain.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountDaoImpl implements AccountDAO {
    Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
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

    @Override
    public Client find(Account account) {
        return null;
    }

    @Override
    public void update(Account account) {

    }

    @Override
    public void delete(int idAccount) {

    }
}
