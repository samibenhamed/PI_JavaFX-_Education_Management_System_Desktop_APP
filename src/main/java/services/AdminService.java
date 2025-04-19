package services;

import entities.Admin;
import entities.Student;
import entities.Teacher;
import entities.User;
import enums.Gender;
import enums.UserType;
import utils.DataBase;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.HashUtil;

public class AdminService {



    public boolean addUser(User user )  {
        try {
            String insertUserQuery = "INSERT INTO users " +
                    "(firstname, lastname, email, password, birthdate, address, phone, national_id, type, teacher_specialty, class_id,gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            Connection connection = DataBase.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(insertUserQuery ) ;
            // Set common fields for all users
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());


            stmt.setString(4, HashUtil.hashPassword(user.getPassword()));
            stmt.setDate(5, Date.valueOf(user.getBirthdate()));
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getPhone());
            stmt.setString(8, user.getNationalId());
            stmt.setString(9, user.getType().name());
            stmt.setString(12, user.getGender().name());


            if(user instanceof  Teacher  ){
                Teacher teacher = (Teacher) user ;
                stmt.setString(10, teacher.getSpecialty());
                stmt.setString(11, null);

            }
            else if(user instanceof Student){
                Student student = (Student) user ;
                stmt.setString(10, null);
                if(student.getStudentClass() != null){
                    stmt.setInt(11, student.getStudentClass().getId());
                }else {
                    stmt.setString(11, null);

                }
            }else if(user instanceof Admin ) {
                stmt.setString(10, null);
                stmt.setString(11, null);
            }
            stmt.executeUpdate() ;
            stmt.close() ;
            return true ;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false ;

        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * FROM users";

        try {
            Connection conn = DataBase.getInstance().getConnection();

             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()  ;

            while (rs.next()) {
                UserType type = UserType.valueOf(rs.getString("type"));
                User user;

                switch (type) {
                    case ADMIN:
                        user = new Admin();
                        user.setType(UserType.ADMIN);
                        break;
                    case STUDENT:
                        Student student = new Student();
                        student.setStudentClass(new ClassesServices().getClassById(rs.getInt("class_id")));
                        user = student;
                        user.setType(UserType.STUDENT);

                        break;
                    case TEACHER:
                        Teacher teacher = new Teacher();
                        teacher.setSpecialty(rs.getString("teacher_specialty"));
                        user = teacher;
                        user.setType(UserType.TEACHER);

                        break;
                    default:
                        continue;
                }

                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setBirthdate(rs.getDate("birthdate").toLocalDate());
                user.setGender(Gender.valueOf(rs.getString("gender")));
                user.setAddress(rs.getString("address"));
                user.setPhone(rs.getString("phone"));
                user.setNationalId(rs.getString("national_id"));

                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }

        return users;
    }

    public boolean deleteUser(int userId) {
        try {
            Connection connection = DataBase.getInstance().getConnection();
            String query = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = connection.prepareStatement(query) ;
                stmt.setInt(1, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
