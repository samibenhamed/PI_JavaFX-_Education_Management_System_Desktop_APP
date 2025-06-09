package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://LAPTOP-JBT79SP8\\MSSQLSERVER01;databaseName=Schedule_Managment;TrustServerCertificate=true;integratedSecurity=true;\n";
    //private static final String USER = "postgres";
    //private static final String PASSWORD = "abc";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}