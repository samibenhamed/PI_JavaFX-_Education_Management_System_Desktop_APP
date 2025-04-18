package utils;
import java.sql.Connection;

public class DataBase {
    public static final String URL = "jdbc:mysql://localhost:3306/education_management_system";
    public static final String USER = "root";
    public static final String PASSWORD = "root";
    private Connection connection;
    private static DataBase instance;
    private DataBase(){
        try {
            connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To Data Base !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataBase getInstance(){
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }


}
