module main.educationmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt ;


    opens main to javafx.fxml;
    exports main;
    exports controllers;
    opens controllers to javafx.fxml;
}