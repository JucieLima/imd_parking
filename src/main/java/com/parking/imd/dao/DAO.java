package com.parking.imd.dao;

import java.sql.Connection;
import java.util.Objects;

public interface DAO {
    void setConnection(Connection connection);
}
