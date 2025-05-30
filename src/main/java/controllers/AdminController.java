package controllers;

import entities.*;
import enums.Gender;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.HelloApplication;
import services.AdminService;
import services.ClassesServices;
import services.UserService;
import utils.Validators;
import java.io.IOException;
import java.util.List;
import utils.UIUtils;

public class AdminController {
   // Add User Fields
     // common users fields
    @FXML
    private TextField firstName,  lastName , email  , address , phone , nationalId  , specialty ;
    @FXML
    private PasswordField  password ;
    @FXML
    private DatePicker birthdate ;
    @FXML
    private ComboBox<UserType> userTypeComboBox ;
    @FXML
    private ComboBox<StudentClass> studentClass ;
    @FXML
    private ComboBox<Gender> gender ;
    @FXML
    private Label specialtyLabel , classLabel ;
    private void resetForm() {
        // Clear text fields
        firstName.clear();
        lastName.clear();
        email.clear();
        address.clear();
        phone.clear();
        nationalId.clear();
        specialty.clear();

        // Clear password field (reset style as well)
        password.clear();

        // Reset combo boxes
        userTypeComboBox.setValue(null);
        gender.setValue(null);
        studentClass.setValue(null);

        // Reset DatePicker
        birthdate.setValue(null);

        // Reset styles (remove red border for validation)
        firstName.setStyle("");
        lastName.setStyle("");
        email.setStyle("");
        address.setStyle("");
        phone.setStyle("");
        nationalId.setStyle("");
        specialty.setStyle("");
        password.setStyle("");
        userTypeComboBox.setStyle("");
        gender.setStyle("");
        studentClass.setStyle("");
        birthdate.setStyle("");
    }
    @FXML
    private void handleMatieresButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Matieres/MatiereView.fxml"));
            Parent root = loader.load();

            // Get current stage from button event
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Matières");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleProfesseursButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurView.fxml"));
            Parent root = loader.load();

            // Get current stage from button event
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Matières");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean addUserFormRequiredFieldsAreEmpty() {
        boolean hasEmptyField = false;
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(firstName);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(lastName);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(email);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(password);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(nationalId);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(address);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(phone);
        hasEmptyField |= UIUtils.highlightDatePickerIfNull(birthdate);
        hasEmptyField |=  UIUtils.highlightComboBoxIfNull(userTypeComboBox);
        hasEmptyField |=  UIUtils.highlightComboBoxIfNull(gender);
        return hasEmptyField  ;
    }
    public boolean addUserFormIsValid(){
        boolean requiredFieldsAreEmplt = addUserFormRequiredFieldsAreEmpty() ;
        if(requiredFieldsAreEmplt == true ){
            UIUtils.showErrorAlert("Error", "Please fill in all required fields.");
            return false ;
        }
        if(!Validators.isEmailValid(email.getText())){
            email.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Error", "Please enter a valid email.");
            return false ;
        }
        if(!Validators.isPasswordStrong(password.getText())){
            password.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Weak Password", "Your password must be at least 8 characters long and include a mix of uppercase letters, lowercase letters, numbers, and special characters.");
            return false ;
        }
        if(!Validators.isValidNationalId(nationalId.getText().replaceAll("\\s+", "") ) ){
            nationalId.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Error", "Please enter a valid national ID.");
            return false ;
        }

        UserService  userService = new UserService();
        if(userService.isEmailUnique(email.getText())==false){
            email.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Email Error", "This email is already in use. Please choose another one.");
            return false ;
        }

        if(userService.isNationalIdUnique(nationalId.getText().replaceAll("\\s+", ""))==false){
            nationalId.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Duplicate National ID", "This national ID is already registered. Please enter a unique and valid national ID.");
            return false ;
        }


        return true ;
    }
    /////////////////////////////////////////////////////////////////
    @FXML
    public void initialize() {
        if(gender !=null){
            gender.getItems().addAll(Gender.MALE, Gender.FEMALE);
        }else {
            System.err.println("gender is null – check your FXML fx:id binding.");
        }

        if(studentClass !=null){
            ClassesServices classesServices = new ClassesServices();
            List<StudentClass> studentClasses = classesServices.getAllClasses();
            studentClasses.forEach(studentClass1 -> {
                studentClass.getItems().add(studentClass1);
            });
        }
        else {
            System.err.println("studentClass is null – check your FXML fx:id binding.");
        }
        if (userTypeComboBox != null) {
            userTypeComboBox.getItems().addAll(UserType.ADMIN, UserType.STUDENT, UserType.TEACHER);
        } else {
            System.err.println("userTypeComboBox is null – check your FXML fx:id binding.");
        }
    }

    // button Handlers
    public void onClasses(ActionEvent event) {
        UIUtils.switchScene(event, "/main/admin_views/showClasses.fxml");
    }
//
    public void OnUsersClick(ActionEvent event){
        UIUtils.switchScene(event, "/main/admin_views/users.fxml");
    }
    public void onSubjectsClick(ActionEvent event ){
        UIUtils.switchScene(event, "/main/admin_views/matiere_view.fxml");
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
    public void onSubmitAddUserClick(ActionEvent event) {
        if(addUserFormIsValid() == false){return ;}
        User user = new User(
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                password.getText(),
                birthdate.getValue(),
                address.getText(),
                phone.getText(),
                nationalId.getText().replaceAll("\\s+", ""),
                userTypeComboBox.getValue() ,
                gender.getValue()
        );
        AdminService adminService = new AdminService();
        boolean succeess = false ;
        if(user.getType() == UserType.STUDENT){
            Student student = new Student(user) ;
            if(studentClass.getValue() != null){
                student.setStudentClass(studentClass.getValue());
            }
            succeess = adminService.addUser(student);
        }else if(user.getType() == UserType.TEACHER){
            Teacher teacher = new Teacher(user) ;
            teacher.setSpecialty(specialty.getText());
            succeess = adminService.addUser(teacher);

        }else if(user.getType() == UserType.ADMIN){
            Admin admin = new Admin(user) ;
            succeess =adminService.addUser(admin);
        }
        if(succeess){
            UIUtils.showSuccessAlert("User Added", "The user has been successfully added!") ;
            resetForm();
        }else {
            UIUtils.showErrorAlert("Error", "An error occurred while adding the user. ");
        }
    }
}
