package com.parking.imd.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juciê Geraldo de Lima
 */
public class DatabaseMySQL implements Database {

    private static DatabaseMySQL instance;
    Connection connection;

    public static DatabaseMySQL getInstance() {
        if (instance == null) {
            instance = new DatabaseMySQL();
        }
        return instance;
    }

    private DatabaseMySQL() {}

    @Override
    public Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1/" + DataBaseConfig.DB_NAME + "?characterEncoding=utf8",
                        DataBaseConfig.DB_USER,
                        DataBaseConfig.DB_PASSWORD
                );
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return connection;
    }

    @Override
    public void disconnect(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}