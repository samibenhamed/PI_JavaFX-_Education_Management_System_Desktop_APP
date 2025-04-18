package test;

import entities.Admin;
import entities.Student;
import entities.Teacher;
import enums.UserType;

import java.time.LocalDate;

public class addUser {
    public static void main(String[] args) {
        services.AdminService adminService = new services.AdminService();
        Student student = new Student(
                "student1firstname",                      // firstName
                "student1lastname",                      // lastName
                "student1@example.com",          // email
                "student1",               // password
                LocalDate.of(2000, 1, 1),    // birthdate
                "123 Test Street",           // address
                "1234567890",                // phone
                "student1nid",                  // nationalId
                UserType.STUDENT         // enum for student class
        );

        Teacher teacher  = new Teacher(
                "teacher1firstname",                      // firstName
                "teacher1lastname",                      // lastName
                "teacher1@example.com",          // email
                "teacher1",               // password
                LocalDate.of(2000, 1, 1),    // birthdate
                "123 Test Street",           // address
                "1234567890",                // phone
                "teacher1nid",                  // nationalId
                UserType.TEACHER         // enum for student class
                ,"spetialty"
        );


        Admin admin = new Admin(
                "admin1firstname",                      // firstName
                "admin1lastname",                      // lastName
                "admin1@example.com",          // email
                "admin1",               // password
                LocalDate.of(2000, 1, 1),    // birthdate
                "123 Test Street",           // address
                "1234567890",                // phone
                "adminn1nid",                  // nationalId
                UserType.ADMIN         // enum for student class
        ) ;

        boolean response = adminService.addUser(admin);
        if (response) {
            System.out.println("User added successfully");
        } else {
            System.out.println("User not added");
        }
        response = adminService.addUser(student);
        if (response) {
            System.out.println("User added successfully");
        } else {
            System.out.println("User not added");
        }
        response = adminService.addUser(teacher);
        if (response) {
            System.out.println("User added successfully");
        } else {
            System.out.println("User not added");
        }
    }
}
