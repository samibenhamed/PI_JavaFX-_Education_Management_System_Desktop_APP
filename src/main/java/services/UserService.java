package services;

import entities.User;
import org.mindrot.jbcrypt.BCrypt;
import org.mindrot.jbcrypt.BCrypt;
import utils.DataBase;
import utils.HashUtil;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


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
                        enums.UserType.valueOf(rs.getString("type"))
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
}
