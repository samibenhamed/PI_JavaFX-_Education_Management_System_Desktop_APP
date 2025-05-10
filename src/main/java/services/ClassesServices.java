package services;

import entities.Student;
import entities.StudentClass;
import enums.Gender;
import enums.UserType;
import utils.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassesServices {
    private final  Connection connection ;
    public ClassesServices() {
        this.connection = DataBase.getInstance().getConnection();
    }

    public int addClass(StudentClass studentClass) {
        String query = "INSERT INTO classes (name, type, level, field, speciality, academic_year, description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, studentClass.getName());
            pst.setString(2, studentClass.getType());
            pst.setString(3, studentClass.getLevel());
            pst.setString(4, studentClass.getField());
            pst.setString(5, studentClass.getSpeciality());
            pst.setString(6, studentClass.getAcademicYear());
            pst.setString(7, studentClass.getDescription());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting class failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("Inserting class failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error adding class: " + e.getMessage());
            return -1; // Indicate failure
        }
    }

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

    public boolean isClassNameUnique(String className) {
        String query = "SELECT COUNT(*) FROM classes WHERE name = ?";

        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, className);
            ResultSet rs = pst.executeQuery() ;
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // true if class name is unique (not found)
                }
        } catch (SQLException e) {
            System.err.println("Error checking class name uniqueness: " + e.getMessage());
        }

        return false; // return false in case of error
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

    public Set<Student> getStudentsByClassId(int classId) {
        String query = "SELECT * FROM users WHERE type ='STUDENT'  and class_id=? " ;
        Set<Student> students = new HashSet<Student>();
        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, classId);
            ResultSet rs = pst.executeQuery() ;

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("birthdate").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("national_id"),
                        UserType.valueOf(rs.getString("type").toUpperCase()),
                        Gender.valueOf(rs.getString("gender").toUpperCase())
                );
                students.add(student);
            }


        } catch (SQLException e) {
            System.err.println("Error getting students by class id: " + e.getMessage());
        }
        return students;
    }


    public boolean updateStudentClass(StudentClass studentClass) {
        String query = "UPDATE classes SET name = ?, type = ?, level = ?, field = ?, speciality = ?, academic_year = ?, description = ? WHERE id = ?";

        try
          {
              PreparedStatement stmt = connection.prepareStatement(query );
            stmt.setString(1, studentClass.getName());
            stmt.setString(2, studentClass.getType());
            stmt.setString(3, studentClass.getLevel());
            stmt.setString(4, studentClass.getField());
            stmt.setString(5, studentClass.getSpeciality());
            stmt.setString(6, studentClass.getAcademicYear());
            stmt.setString(7, studentClass.getDescription());
            stmt.setInt(8, studentClass.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isNewClassNameUnique(String className, int id) {
        String query = "SELECT COUNT(*) FROM classes WHERE name = ? AND id != ?";

        try {
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, className);
            pst.setInt(2, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking class name uniqueness: " + e.getMessage());
        }

        return false; // return false in case of error
    }



}
