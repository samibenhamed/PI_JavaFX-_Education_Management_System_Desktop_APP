package services;

import entities.Student;
import entities.Teacher;
import entities.User;
import enums.Gender;
import utils.DataBase;
import utils.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserService {

    public User authenticate(String email, String password) {
        String hashedPassword = HashUtil.hashPassword(password);
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try {
            Connection connection = DataBase.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, hashedPassword);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                System.out.println("Authentication successful!");
                // Populate and return the User object
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("birthdate").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("national_id"),
                        enums.UserType.valueOf(rs.getString("type")),
                        enums.Gender.valueOf(rs.getString("gender"))
                );

                return user;
            } else {
                System.out.println("Authentication failed.");
                return null;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean isEmailUnique(String email) {
        try {
            String SQL = "SELECT COUNT(*) FROM users WHERE email = ?";
            Connection conn = DataBase.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL)  ;
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // true if count == 0 → email is unique
            }
        } catch (SQLException e) {
            System.err.println("Error checking email uniqueness: " + e.getMessage());
        }
        return false; // Default to false in case of error
    }

    public boolean isNationalIdUnique(String nationalId) {
        String SQL = "SELECT COUNT(*) FROM users WHERE national_id = ?";
        try  { Connection conn = DataBase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)  ;

            stmt.setString(1, nationalId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking national ID uniqueness: " + e.getMessage());
        }
        return false;
    }


    public boolean updateUser(User user) {
        String sql = "UPDATE users SET " +
                "firstName = ?, lastName = ?, email = ?, password = ?, birthdate = ?, " +
                "gender = ?, address = ?, phone = ?, national_id = ?, type = ?, " +
                "teacher_specialty = ?, class_id = ? " +
                "WHERE id = ?";

        try {
            Connection conn = DataBase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql) ;

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, HashUtil.hashPassword(user.getPassword()) ); // Make sure to hash password before saving
            stmt.setDate(5, java.sql.Date.valueOf(user.getBirthdate()));
            stmt.setString(6, user.getGender().toString());
            stmt.setString(7, user.getAddress());
            stmt.setString(8, user.getPhone());
            stmt.setString(9, user.getNationalId());
            stmt.setString(10, user.getType().toString());

            // Handle user-type specific fields
            if (user instanceof Teacher) {
                stmt.setString(11, ((Teacher) user).getSpecialty());
                stmt.setNull(12, java.sql.Types.INTEGER);
            } else if (user instanceof Student) {
                stmt.setNull(11, java.sql.Types.VARCHAR);
                Integer classId = ((Student) user).getStudentClass().getId();
                if(classId != null){
                    stmt.setInt(12, ((Student) user).getStudentClass().getId());
                }else {
                    stmt.setNull(12, java.sql.Types.INTEGER);
                }

            } else {
                stmt.setNull(11, java.sql.Types.VARCHAR);
                stmt.setNull(12, java.sql.Types.INTEGER);
            }

            stmt.setInt(13, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
