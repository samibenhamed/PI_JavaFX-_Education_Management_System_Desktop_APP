package controllers;

import entities.*;
import enums.Gender;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ClassesServices;
import services.UserService;
import utils.Validators;
import utils.UIUtils;

import java.util.List;

public class UpdateUserController {

    @FXML
    private TextField firstName, lastName, email, address, phone, nationalId, specialty;

    @FXML
    private PasswordField password;

    @FXML
    private DatePicker birthdate;
    @FXML
    private ComboBox<StudentClass> studentClass;

    @FXML
    private ComboBox<Gender> gender;

    @FXML
    private Label specialtyLabel, classLabel;

    private User user;

    //
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
    }


    // Initialize form with the data of the user to update
    public void initData(User user) {
        this.user = user;

        if (user instanceof Teacher) {
            specialty.setText(((Teacher) user).getSpecialty());
        } else if (user instanceof Student) {
            studentClass.setValue(((Student) user).getStudentClass());
        }

        // fill  form fields with selected  user data
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
        phone.setText(user.getPhone());
        nationalId.setText(user.getNationalId());
        birthdate.setValue(user.getBirthdate());
        gender.setValue(user.getGender());

        // Toggle dynamic fields
        updateDynamicFields(user.getType());
    }

    private void updateDynamicFields(UserType type) {
        if (type == UserType.TEACHER) {
            specialtyLabel.setVisible(true);
            specialty.setVisible(true);
            classLabel.setVisible(false);
            studentClass.setVisible(false);
        } else if (type == UserType.STUDENT) {
            specialtyLabel.setVisible(false);
            specialty.setVisible(false);
            classLabel.setVisible(true);
            studentClass.setVisible(true);
        } else {
            specialtyLabel.setVisible(false);
            specialty.setVisible(false);
            classLabel.setVisible(false);
            studentClass.setVisible(false);
        }
    }

    // Method to save updated user information
    @FXML
    private void saveUser() {
        if (!isFormValid()) {
            return;
        }
        // Update user information
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setAddress(address.getText());
        user.setPhone(phone.getText());
        user.setNationalId(nationalId.getText());
        user.setBirthdate(birthdate.getValue());
        user.setGender(gender.getValue());
        if(password.getText() != null ){
            user.setPassword(password.getText());
        }

        if (user instanceof Teacher) {
            ((Teacher) user).setSpecialty(specialty.getText());
        } else if (user instanceof Student) {
            ((Student) user).setStudentClass(studentClass.getValue());
        }
        System.out.println(user);
        UserService userService = new UserService();
        boolean success = userService.updateUser(user);
        if (success) {
            UIUtils.showSuccessAlert("User Updated", "The user has been successfully updated!");
        } else {
            UIUtils.showErrorAlert("Error", "An error occurred while updating the user.");
        }

    }

    private boolean isFormValid() {
        boolean hasEmptyField = false;
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(firstName);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(lastName);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(email);
//        hasEmptyField |= UIUtils.highlightTextFieldIfNull(password);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(nationalId);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(address);
        hasEmptyField |= UIUtils.highlightTextFieldIfNull(phone);
        hasEmptyField |= UIUtils.highlightDatePickerIfNull(birthdate);
        hasEmptyField |= UIUtils.highlightComboBoxIfNull(gender);

        if (hasEmptyField) {
            UIUtils.showErrorAlert("Error", "Please fill in all required fields.");
            return false;
        }

        // Additional validation checks
        if (!Validators.isEmailValid(email.getText())) {
            email.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Error", "Please enter a valid email.");
            return false;
        }

        if ( !password.getText().isEmpty() && !Validators.isPasswordStrong(password.getText())) {
            password.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Weak Password", "Your password must be at least 8 characters long and include a mix of uppercase letters, lowercase letters, numbers, and special characters.");
            return false;
        }else {
            password.setStyle("");
        }

        if (!Validators.isValidNationalId(nationalId.getText().replaceAll("\\s+", ""))) {
            nationalId.setStyle("-fx-border-color: red;");
            UIUtils.showErrorAlert("Error", "Please enter a valid national ID.");
            return false;
        }

        return true;
    }

    public  void  setClassInvisible(){
        classLabel.setVisible(false);
        studentClass.setVisible(false);
    }
    public  void  setSpecialityInvisible(){
        specialtyLabel.setVisible(false);
        specialty.setVisible(false);
    }
}
