package test;

import entities.User;
import services.UserService;

public class Authetification {
    public static void  main(String[] args) {
        UserService userService = new UserService();
//        User user = userService.authenticate( "admin1@example.com" , "admin1");
//        User user = userService.authenticate( "student1@example.com" , "student1");
        User user = userService.authenticate( "teacher1@example.com" , "teacher1");

        if(user != null){
            System.out.println(user);
        }

    }
}
