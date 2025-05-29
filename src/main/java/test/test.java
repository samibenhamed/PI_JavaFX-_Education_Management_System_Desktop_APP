package test;

import entities.StudentClass;
import entities.User;
import services.AdminService;
import services.ClassesServices;
import services.UserService;
import utils.Validators;

import java.util.List;

public class test {
    public static void main(String[] args) {
        ClassesServices classesServices = new ClassesServices() ;
        StudentClass testClass = new StudentClass(
                "Software Engineering",
                "engineering degree",
                "3rd Year",
                "IT",
                "Web Development",
                "2024/2025",
                "Focuses on backend and frontend development"
        );

//        testClass.showClassDetails();
        int id = classesServices.addClass(testClass);
        System.out.println("The New Class ID : "+ id);


    }
}
