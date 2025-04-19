package services;

import entities.StudentClass;
import utils.DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClassesServices {

    public List<StudentClass> getAllClasses() {
        List<StudentClass> classes = new ArrayList<>();
        String SQL = "SELECT * FROM classes";
        Connection connection = DataBase.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                StudentClass studentClass = new StudentClass(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("level"),
                        resultSet.getString("field"),
                        resultSet.getString("speciality"),
                        resultSet.getString("academic_year"),
                        resultSet.getString("description")
                );
                classes.add(studentClass);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return classes;
    }


    public StudentClass getClassById(int id) {
        String SQL = "SELECT * FROM classes WHERE id = " + id;
        Connection connection = DataBase.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            if (resultSet.next()) {
                return new StudentClass(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("level"),
                        resultSet.getString("field"),
                        resultSet.getString("speciality"),
                        resultSet.getString("academic_year"),
                        resultSet.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return null;
    }
}
