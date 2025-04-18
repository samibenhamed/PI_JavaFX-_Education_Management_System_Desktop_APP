package services;

import entities.Student;
import entities.Teacher;
import entities.User;
import utils.DataBase;

import java.security.MessageDigest;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import utils.HashUtil;

public class AdminService {



    public boolean addUser(User user )  {
        try {
            String insertUserQuery = "INSERT INTO users " +
                    "(firstname, lastname, email, password, birthdate, address, phone, national_id, type, teacher_specialty, class_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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


            if(user instanceof  Teacher  ){
                Teacher teacher = (Teacher) user ;
                stmt.setString(10, teacher.getSpecialty());
                stmt.setString(11, null);

            }
            else if(user instanceof Student){
                Student student = (Student) user ;
                stmt.setString(10, null);
                stmt.setString(11, null);
            }else {
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
}
