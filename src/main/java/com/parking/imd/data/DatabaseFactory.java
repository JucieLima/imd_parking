package com.parking.imd.data;

public class DatabaseFactory {
    public static Database getDatabase(String database){
        if(database.equals("mysql")){
            return DatabaseMySQL.getInstance();
        }
        return null;
    }
}
