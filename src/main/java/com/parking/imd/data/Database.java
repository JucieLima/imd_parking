package com.parking.imd.data;

import java.sql.Connection;

public interface Database {
    public Connection connect();
    public void disconnect(Connection conn);
}
