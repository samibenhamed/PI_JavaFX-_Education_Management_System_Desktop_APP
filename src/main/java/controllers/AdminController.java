package controllers;

import entities.Admin;
import entities.Student;
import entities.Teacher;
import entities.User;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.HelloApplication;
import services.AdminService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminController {
   // Add User Fields
     // common users fields
    @FXML
    private TextField firstName,  lastName , email  , address , phone , nationalId  , specialty , studentClass;
    @FXML
    private PasswordField  password ;
    @FXML
    private DatePicker birthdate ;
    @FXML
    private ComboBox<UserType> userTypeComboBox;
    @FXML
    private Label specialtyLabel , classLabel ;

     // Student Specifics
     // Teacher Specifics

    // usefull functions
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        if (userTypeComboBox != null) {
            userTypeComboBox.getItems().addAll(UserType.ADMIN, UserType.STUDENT, UserType.TEACHER);
        } else {
            System.err.println("userTypeComboBox is null – check your FXML fx:id binding.");
        }
    }

    // button Handlers
    public void onAddUserClick(ActionEvent event) {
        switchScene(event, "/main/admin_views/add-user.fxml");
    }
    public void onHomeClick(ActionEvent event) {
        switchScene(event, "/main/admin-home-view.fxml");
    }
    @FXML
    private void onSelectedUserType(ActionEvent event){
        UserType selectedUserType = userTypeComboBox.getValue();
        if(selectedUserType == UserType.TEACHER){
            classLabel.setVisible(false);
            studentClass.setVisible(false);
            specialtyLabel.setVisible(true);
            specialty.setVisible(true);
        }else  if(selectedUserType == UserType.STUDENT){
            specialtyLabel.setVisible(false);
            specialty.setVisible(false);
            classLabel.setVisible(true);
            studentClass.setVisible(true);
        }else {
            specialtyLabel.setVisible(false);
            specialty.setVisible(false);
            classLabel.setVisible(false);
            studentClass.setVisible(false);
        }

    }

    @FXML
    public void onSubmitAddUserClick(ActionEvent event){

//        if (firstName.getText().isEmpty()) {
//            System.out.println("First name is required.");
//        }
//        System.out.println("First Name : " + firstName.getText() );
//        System.out.println("First Name : " + lastName.getText() );
//
//        System.out.println("Email : " + email.getText() );
//        System.out.println("Password : " + password.getText() );
//        System.out.println("birthdate : " + birthdate.getValue());
//
//        System.out.println("adress  : " + address.getText());
//        System.out.println("phone : " + phone.getText());
//        System.out.println("nationalId : " + nationalId.getText() );
//
//        System.out.println("User Type : " + userTypeComboBox.getValue());
//        System.out.println("teacher Specialty : " + specialty.getText() );
//        System.out.println("Student Class : " + specialty.getText() );
        if (userTypeComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user type.");
            alert.showAndWait();
            return;
        }

        User user = new User(
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                password.getText(),
                birthdate.getValue(),
                address.getText(),
                phone.getText(),
                nationalId.getText(),
                userTypeComboBox.getValue()
        );
        System.out.println(user);
        AdminService adminService = new AdminService();
        if(user.getType() == UserType.STUDENT){
            Student student = new Student(user) ;
//            student.setStudentClass(studentClass.getText());
            adminService.addUser(student);
        }else if(user.getType() == UserType.TEACHER){
            Teacher teacher = new Teacher(user) ;
            teacher.setSpecialty(specialty.getText());
            adminService.addUser(teacher);

        }else if(user.getType() == UserType.ADMIN){
            Admin admin = new Admin(user) ;
            adminService.addUser(admin);
        }


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Added");
        alert.setHeaderText(null);
        alert.setContentText("The user has been successfully added!");
        alert.showAndWait();
    }




}
