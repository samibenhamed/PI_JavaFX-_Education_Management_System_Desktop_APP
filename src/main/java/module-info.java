module main.educationmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens main to javafx.fxml;
    exports main;
    exports controllers;
    exports entities ;
    opens controllers to javafx.fxml;
    opens test to javafx.graphics;

}