package services;
import entities.Student;
import entities.StudentClass;
import enums.Gender;
import enums.UserType;
import utils.DataBase;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentService {
    private  Connection connection;
    public StudentService() {
        connection = DataBase.getInstance().getConnection();
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String studentQuery = "SELECT * FROM users WHERE type = 'STUDENT'";
        String classQuery = "SELECT * FROM classes WHERE id = ?";

        try {
            PreparedStatement studentStmt = connection.prepareStatement(studentQuery);
            PreparedStatement classStmt = connection.prepareStatement(classQuery);
            ResultSet studentRs = studentStmt.executeQuery() ;
            while (studentRs.next()) {
                int id = studentRs.getInt("id");
                String firstName = studentRs.getString("firstName");
                String lastName = studentRs.getString("lastName");
                String email = studentRs.getString("email");
                String password = studentRs.getString("password");
                LocalDate birthdate = studentRs.getDate("birthdate").toLocalDate();
                String address = studentRs.getString("address");
                String phone = studentRs.getString("phone");
                String nationalId = studentRs.getString("national_id");
                UserType type = UserType.valueOf(studentRs.getString("type"));
                Gender gender = Gender.valueOf(studentRs.getString("gender"));
                Integer classId = studentRs.getInt("class_id");

                StudentClass studentClass = null;

                // Check if class_id is not null (i.e., != 0)
                if (classId != null ) {
                    try {
                        classStmt.setInt(1, classId);

                        ResultSet classRs = classStmt.executeQuery();
                        if (classRs.next()) {
                            studentClass = new StudentClass(
                                    classRs.getInt("id"),
                                    classRs.getString("name"),
                                    classRs.getString("type"),
                                    classRs.getString("level"),
                                    classRs.getString("field"),
                                    classRs.getString("speciality"),
                                    classRs.getString("academic_year"),
                                    classRs.getString("description")
                            );
                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                Student student = new Student(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type, studentClass, gender);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }

        return students;
    }

    public  boolean  updateStudentsClass(Set<Student> students, int classId) {
        String updateQuery = "UPDATE users SET class_id = ? WHERE id = ? AND type = 'STUDENT'";

        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            for (Student student : students) {
                preparedStatement.setInt(1, classId); // Set class_id parameter
                preparedStatement.setInt(2, student.getId()); // Set student id parameter
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            System.out.println("Students' class_id updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating students' class_id: " + e.getMessage());
            return false ;
        }
        return  true ;
    }

    public void setStudentClassIdToNull(int studentID){

        String updateQuery = "UPDATE users SET class_id = NULL WHERE id = ? AND type = 'STUDENT'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, studentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  void setStudentClassId(int studentID, int classID){

        String updateQuery = "UPDATE users SET class_id = ? WHERE id = ? AND type = 'STUDENT'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, classID);
            preparedStatement.setInt(2, studentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
