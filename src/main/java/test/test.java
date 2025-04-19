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
        AdminService adminService = new AdminService();
        List<User> users = adminService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }



    }
}
