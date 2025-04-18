package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String url = "jdbc:mysql://localhost:3306/1cinfo3";
    private final String username = "root";
    private final String password = "";
    private Connection cnx;

    private static MyDatabase instance;

    private MyDatabase(){
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance(){
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
