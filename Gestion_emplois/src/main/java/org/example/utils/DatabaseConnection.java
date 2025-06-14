package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:50217;databaseName=Schedule_Managment;integratedSecurity=true;encrypt=false;\n";
    //private static final String USER = "postgres";
    //private static final String PASSWORD = "abc";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}